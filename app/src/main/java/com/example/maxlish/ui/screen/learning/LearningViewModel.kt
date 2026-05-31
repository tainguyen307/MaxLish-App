package com.example.maxlish.ui.screen.learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxlish.data.model.*
import com.example.maxlish.data.repository.AuthRepository
import com.example.maxlish.data.repository.LearningRepository
import com.example.maxlish.data.repository.ProgressRepository
import com.example.maxlish.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.ceil

class LearningViewModel(
    private val setId: String,
    private val mode: String,
    private val authRepository: AuthRepository,
    private val learningRepository: LearningRepository,
    private val progressRepository: ProgressRepository,
    private val vocabularyRepository: VocabularyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LearningState(mode = mode, sessionStartTime = System.currentTimeMillis()))
    val state: StateFlow<LearningState> = _state.asStateFlow()

    // Temporary list that holds the dynamic queue including cards re-added on AGAIN
    private val sessionQueue = mutableListOf<VocabularyWord>()
    private var currentUser: User = User()

    init {
        loadSessionData()
    }

    private fun loadSessionData() {
        val fbUser = authRepository.getCurrentUser()
        if (fbUser == null) {
            _state.update { it.copy(errorMessage = "Người dùng chưa đăng nhập") }
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. Get user profile and goals
                progressRepository.getUserStats(fbUser.uid).collect { userProfile ->
                    if (userProfile != null) {
                        currentUser = userProfile
                    }

                    // 2. Fetch vocabulary set name
                    val setRes = vocabularyRepository.getVocabularySetById(setId)
                    val setName = setRes.getOrNull()?.title ?: "Vocabulary Set"

                    // 3. Fetch words in this set
                    val wordsRes = vocabularyRepository.getWordsBySetId(setId)
                    val words = wordsRes.getOrNull() ?: emptyList()

                    // 4. Fetch learning progress for these words
                    val progressRes = learningRepository.getLearningProgress(fbUser.uid)
                    val progressList = progressRes.getOrNull() ?: emptyList()
                    val progressMap = progressList.associateBy { it.wordId }

                    // 5. Partition words into New vs Due based on SM-2
                    val now = System.currentTimeMillis()
                    val newWords = mutableListOf<VocabularyWord>()
                    val dueWords = mutableListOf<VocabularyWord>()

                    words.forEach { word ->
                        val progress = progressMap[word.wordId]
                        if (progress == null) {
                            newWords.add(word)
                        } else if (progress.nextReviewAt <= now) {
                            dueWords.add(word)
                        }
                    }

                    // 6. Apply Daily Learning Plan limits
                    val limitNew = currentUser.dailyNewWords
                    val limitReview = currentUser.dailyReviewWords

                    val finalQueue = mutableListOf<VocabularyWord>()
                    when (mode) {
                        "new" -> {
                            finalQueue.addAll(newWords.take(limitNew))
                        }
                        "review" -> {
                            finalQueue.addAll(dueWords.take(limitReview))
                        }
                        else -> { // "all"
                            finalQueue.addAll(dueWords.take(limitReview))
                            finalQueue.addAll(newWords.take(limitNew))
                        }
                    }

                    // If queue is empty and we have no due words, but user requested study,
                    // we can add some new words to not show empty screen.
                    if (finalQueue.isEmpty() && words.isNotEmpty() && mode == "all") {
                        finalQueue.addAll(words.take(5))
                    }

                    sessionQueue.clear()
                    sessionQueue.addAll(finalQueue)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            setName = setName,
                            queue = ArrayList(sessionQueue),
                            progressMap = progressMap,
                            errorMessage = if (finalQueue.isEmpty()) "Không có từ vựng nào cần học lúc này" else null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Đã xảy ra lỗi khi tải dữ liệu học tập"
                    )
                }
            }
        }
    }

    fun onEvent(event: LearningEvent) {
        when (event) {
            LearningEvent.FlipCard -> {
                _state.update { it.copy(isFlipped = !it.isFlipped) }
            }
            is LearningEvent.RateCard -> {
                handleCardRating(event.quality)
            }
            LearningEvent.FinishSession -> {
                // Handled in Navigation, but we double-check saves
            }
        }
    }

    private fun handleCardRating(quality: ReviewQuality) {
        val word = state.value.currentWord ?: return
        val userId = authRepository.getCurrentUser()?.uid ?: return

        val isAgain = quality == ReviewQuality.AGAIN
        val existingProgress = state.value.progressMap[word.wordId] ?: LearningProgress(
            progressId = UUID.randomUUID().toString(),
            userId = userId,
            wordId = word.wordId
        )

        // 1. Calculate new Spaced Repetition values using SM-2
        val sm2Result = Sm2Algorithm.calculate(
            quality = quality,
            previousRepetition = existingProgress.repetition,
            previousInterval = existingProgress.interval,
            previousEaseFactor = existingProgress.easeFactor
        )

        // 2. Map new progress stats
        val correctInc = if (isAgain) 0 else 1
        val wrongInc = if (isAgain) 1 else 0
        val isFirstTimeMastered = !isAgain && !existingProgress.mastered && sm2Result.repetition >= 3

        val updatedProgress = existingProgress.copy(
            repetition = sm2Result.repetition,
            interval = sm2Result.interval,
            easeFactor = sm2Result.easeFactor,
            lastReviewedAt = System.currentTimeMillis(),
            nextReviewAt = sm2Result.nextReviewAt,
            correctCount = existingProgress.correctCount + correctInc,
            wrongCount = existingProgress.wrongCount + wrongInc,
            mastered = isFirstTimeMastered || existingProgress.mastered,
            updatedAt = System.currentTimeMillis()
        )

        // 3. Create review log history
        val reviewLog = Review(
            reviewId = UUID.randomUUID().toString(),
            userId = userId,
            wordId = word.wordId,
            quality = quality,
            previousInterval = existingProgress.interval,
            newInterval = sm2Result.interval,
            previousEaseFactor = existingProgress.easeFactor,
            newEaseFactor = sm2Result.easeFactor,
            reviewTime = System.currentTimeMillis()
        )

        viewModelScope.launch {
            // Save to Firestore background tasks
            learningRepository.updateLearningProgress(updatedProgress)
            learningRepository.saveReview(reviewLog)

            // Update stats accumulators in memory
            val xpGain = if (isAgain) 0 else 10
            val newCorrectCount = state.value.correctCount + correctInc
            val newWrongCount = state.value.wrongCount + wrongInc
            val newXpEarned = state.value.xpEarned + xpGain

            // Handle queue transition
            if (isAgain) {
                // Append the card to the end of the queue for re-learning
                sessionQueue.add(word)
            }

            val nextIndex = state.value.currentIndex + 1
            val isSessionOver = nextIndex >= sessionQueue.size

            if (isSessionOver) {
                // Session is completed, sync final user stats and study session details to Firestore
                saveFinalSessionStats(userId, newCorrectCount, newWrongCount, newXpEarned)
                _state.update {
                    it.copy(
                        correctCount = newCorrectCount,
                        wrongCount = newWrongCount,
                        xpEarned = newXpEarned,
                        isFlipped = false,
                        isFinished = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        queue = ArrayList(sessionQueue),
                        currentIndex = nextIndex,
                        correctCount = newCorrectCount,
                        wrongCount = newWrongCount,
                        xpEarned = newXpEarned,
                        isFlipped = false,
                        progressMap = state.value.progressMap + (word.wordId to updatedProgress)
                    )
                }
            }
        }
    }

    private suspend fun saveFinalSessionStats(
        userId: String,
        correctCount: Int,
        wrongCount: Int,
        xpEarned: Int
    ) {
        val durationMs = System.currentTimeMillis() - state.value.sessionStartTime
        val durationMinutes = ceil(durationMs.toDouble() / 60000.0).toInt().coerceAtLeast(1)

        val studySession = StudySession(
            sessionId = UUID.randomUUID().toString(),
            userId = userId,
            reviewedWords = correctCount + wrongCount,
            correctAnswers = correctCount,
            wrongAnswers = wrongCount,
            durationMinutes = durationMinutes,
            startedAt = state.value.sessionStartTime,
            endedAt = System.currentTimeMillis()
        )

        // Save study session
        progressRepository.saveStudySession(studySession)

        // Update User Profile stats
        // Compute streak increment if studying on a new day
        val lastReviewed = currentUser.updatedAt
        val now = System.currentTimeMillis()
        val isNewDay = now - lastReviewed >= 86400000L // Simplistic daily check
        val newStreak = if (currentUser.streak == 0 || isNewDay) currentUser.streak + 1 else currentUser.streak

        // Count how many new words were mastered in this session
        // (words that didn't exist before or had rep == 0 and now completed rep >= 1)
        val newWordsLearned = correctCount // Standard estimate

        val updatedUser = currentUser.copy(
            xp = currentUser.xp + xpEarned,
            correctAnswers = currentUser.correctAnswers + correctCount,
            wrongAnswers = currentUser.wrongAnswers + wrongCount,
            streak = newStreak,
            totalWordsLearned = currentUser.totalWordsLearned + newWordsLearned,
            updatedAt = System.currentTimeMillis()
        )

        progressRepository.updateUserStats(updatedUser)
    }
}
