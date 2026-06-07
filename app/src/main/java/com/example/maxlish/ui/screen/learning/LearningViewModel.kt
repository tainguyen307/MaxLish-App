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
import kotlinx.coroutines.flow.first
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
    private val initialNewWordIds = mutableSetOf<String>()

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
                // 1. Get user profile and goals (use .first() to avoid re-triggering
                //    the entire session when user stats are updated later)
                val userProfile = progressRepository.getUserStats(fbUser.uid).first()
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
                // Guard against 0-values that occur when the User document in Firestore
                // was created before dailyNewWords/dailyReviewWords fields were added
                // (Firestore returns 0 for missing fields, but the Kotlin default is 20/50).
                val limitNew = if (currentUser.dailyNewWords > 0) currentUser.dailyNewWords else 20
                val limitReview = if (currentUser.dailyReviewWords > 0) currentUser.dailyReviewWords else 50

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

                // If queue is still empty (e.g. no new or due words), show all words
                // so the user never sees a blank screen when the set has content.
                if (finalQueue.isEmpty() && words.isNotEmpty() && mode == "all") {
                    finalQueue.addAll(words.take(limitNew.coerceAtLeast(10)))
                }

                sessionQueue.clear()
                sessionQueue.addAll(finalQueue)

                initialNewWordIds.clear()
                finalQueue.forEach { word ->
                    if (progressMap[word.wordId] == null) {
                        initialNewWordIds.add(word.wordId)
                    }
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        setName = setName,
                        queue = ArrayList(sessionQueue),
                        // Capture size BEFORE any AGAIN cards are re-added.
                        // Used by UI so progress display stays "X/N" not "X/(N+againCount)".
                        initialQueueSize = finalQueue.size,
                        progressMap = progressMap,
                        errorMessage = if (finalQueue.isEmpty()) "Không có từ vựng nào cần học lúc này" else null
                    )
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
            progressId = "${userId}_${word.wordId}",
            userId = userId,
            setId = word.setId,
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

        // Always update progressMap immediately so subsequent ratings for
        // the same word reuse the same progressId instead of generating a new UUID
        val updatedProgressMap = state.value.progressMap + (word.wordId to updatedProgress)
        _state.update { it.copy(progressMap = updatedProgressMap) }

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
                        isFlipped = false
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
        val timeSinceLastReview = now - lastReviewed
        val newStreak = when {
            currentUser.streak == 0 -> 1 // First time studying
            timeSinceLastReview >= 172800000L -> 1 // > 48h: missed a day, reset streak
            timeSinceLastReview >= 86400000L -> currentUser.streak + 1 // 24-48h: new day, increment
            else -> currentUser.streak // Same day, keep current streak
        }

        // Count how many new words were completed in this session
        val newWordsCompleted = initialNewWordIds.size
        
        // Fetch actual learning progress size to get exact count of unique learned words, self-correcting any legacy bloat
        val progressRes = learningRepository.getLearningProgress(userId)
        val totalLearnedCount = progressRes.getOrNull()?.size ?: (currentUser.totalWordsLearned + newWordsCompleted)

        val updatedUser = currentUser.copy(
            xp = currentUser.xp + xpEarned,
            correctAnswers = currentUser.correctAnswers + correctCount,
            wrongAnswers = currentUser.wrongAnswers + wrongCount,
            streak = newStreak,
            totalWordsLearned = totalLearnedCount,
            updatedAt = System.currentTimeMillis()
        )

        progressRepository.updateUserStats(updatedUser)
    }
}
