package com.example.maxlish.data.seed

import android.util.Log
import com.example.maxlish.data.model.AppNotification
import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.Review
import com.example.maxlish.data.model.ReviewQuality
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.model.User
import com.example.maxlish.data.model.UserLevel
import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.data.model.VocabularyWord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SeedData {

    private val db = FirebaseFirestore.getInstance()

    suspend fun seedAll() {
        try {
            seedForUser("u1", "tai@gmail.com")
            Log.d("SEED", "All seed completed via legacy seedAll")
        } catch (e: Exception) {
            Log.e("SEED", "Legacy seedAll failed: ${e.message}")
        }
    }

    suspend fun seedForUser(userId: String, email: String) {
        try {
            seedUser(userId, email)
            Log.d("SEED", "User profile seeded/updated for $userId")

            seedVocabularySets(userId)
            Log.d("SEED", "Vocabulary sets seeded for $userId")

            seedVocabularyWords(userId)
            Log.d("SEED", "Vocabulary words seeded for $userId")

            seedLearningProgress(userId)
            Log.d("SEED", "Learning progress seeded for $userId")

            seedReviews(userId)
            Log.d("SEED", "Reviews seeded for $userId")

            seedStudySessions(userId)
            Log.d("SEED", "Study sessions seeded for $userId")

            seedNotifications(userId)
            Log.d("SEED", "Notifications seeded for $userId")

            Log.d("SEED", "All seed completed for $userId")
        } catch (e: Exception) {
            Log.e("SEED", "Seed failed: ${e.message}")
            throw e
        }
    }

    // =========================
    // USERS
    // =========================

    private suspend fun seedUser(userId: String, email: String) {
        val user = User(
            uid = userId,
            email = email,
            displayName = email.substringBefore("@"),
            learningGoal = LearningGoal.IELTS,
            level = UserLevel.B1,
            streak = 12,
            totalWordsLearned = 120,
            correctAnswers = 320,
            wrongAnswers = 40,
            xp = 2500
        )
        db.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }

    // =========================
    // VOCABULARY SETS
    // =========================

    private suspend fun seedVocabularySets(userId: String) {
        val sets = listOf(
            VocabularySet(
                setId = "set_ielts_${userId}",
                ownerId = userId,
                title = "IELTS Academic Vocabulary",
                description = "Common academic IELTS words",
                tags = listOf("IELTS", "Academic"),
                wordCount = 3,
                isPublic = true
            ),
            VocabularySet(
                setId = "set_toeic_${userId}",
                ownerId = userId,
                title = "TOEIC Business Vocabulary",
                description = "Business English vocabulary",
                tags = listOf("TOEIC", "Business"),
                wordCount = 2,
                isPublic = true
            )
        )

        sets.forEach { set ->
            db.collection("vocabulary_sets")
                .document(set.setId)
                .set(set)
                .await()
        }
    }

    // =========================
    // VOCABULARY WORDS
    // =========================

    private suspend fun seedVocabularyWords(userId: String) {
        val words = listOf(
            VocabularyWord(
                wordId = "w1_${userId}",
                setId = "set_ielts_${userId}",
                ownerId = userId,
                word = "enhance",
                pronunciation = "/ɪnˈhɑːns/",
                meaning = "cải thiện",
                description = "to improve something",
                example = "Reading books enhances vocabulary.",
                collocations = listOf("enhance skills", "enhance performance"),
                relatedWords = listOf("improve", "boost"),
                difficulty = "Medium"
            ),
            VocabularyWord(
                wordId = "w2_${userId}",
                setId = "set_ielts_${userId}",
                ownerId = userId,
                word = "allocate",
                pronunciation = "/ˈæləkeɪt/",
                meaning = "phân bổ",
                description = "to distribute resources",
                example = "The company allocated more budget.",
                collocations = listOf("allocate resources", "allocate budget"),
                relatedWords = listOf("assign", "distribute"),
                difficulty = "Hard"
            ),
            VocabularyWord(
                wordId = "w3_${userId}",
                setId = "set_ielts_${userId}",
                ownerId = userId,
                word = "maintain",
                pronunciation = "/meɪnˈteɪn/",
                meaning = "duy trì",
                description = "to keep something at the same level",
                example = "Exercise helps maintain good health.",
                collocations = listOf("maintain balance", "maintain quality"),
                relatedWords = listOf("preserve", "sustain"),
                difficulty = "Easy"
            ),
            VocabularyWord(
                wordId = "w4_${userId}",
                setId = "set_toeic_${userId}",
                ownerId = userId,
                word = "negotiate",
                pronunciation = "/nɪˈɡoʊʃieɪt/",
                meaning = "đàm phán",
                description = "to discuss to reach agreement",
                example = "They negotiated a better contract.",
                collocations = listOf("negotiate contract", "negotiate price"),
                relatedWords = listOf("discuss", "bargain"),
                difficulty = "Medium"
            ),
            VocabularyWord(
                wordId = "w5_${userId}",
                setId = "set_toeic_${userId}",
                ownerId = userId,
                word = "revenue",
                pronunciation = "/ˈrevənuː/",
                meaning = "doanh thu",
                description = "income generated by business",
                example = "The company increased its revenue.",
                collocations = listOf("annual revenue", "generate revenue"),
                relatedWords = listOf("income", "profit"),
                difficulty = "Medium"
            )
        )

        words.forEach { word ->
            db.collection("vocabulary_words")
                .document(word.wordId)
                .set(word)
                .await()
        }
    }

    // =========================
    // LEARNING PROGRESS
    // =========================

    private suspend fun seedLearningProgress(userId: String) {
        val progresses = listOf(
            LearningProgress(
                progressId = "p1_${userId}",
                userId = userId,
                wordId = "w1_${userId}",
                repetition = 3,
                interval = 7,
                easeFactor = 2.6,
                lastReviewedAt = System.currentTimeMillis(),
                nextReviewAt = System.currentTimeMillis() + 86400000,
                correctCount = 5,
                wrongCount = 1,
                mastered = false
            ),
            LearningProgress(
                progressId = "p2_${userId}",
                userId = userId,
                wordId = "w2_${userId}",
                repetition = 1,
                interval = 1,
                easeFactor = 2.3,
                correctCount = 1,
                wrongCount = 2,
                mastered = false
            ),
            LearningProgress(
                progressId = "p3_${userId}",
                userId = userId,
                wordId = "w4_${userId}",
                repetition = 5,
                interval = 14,
                easeFactor = 2.8,
                correctCount = 12,
                wrongCount = 1,
                mastered = true
            )
        )

        progresses.forEach { progress ->
            db.collection("learning_progress")
                .document(progress.progressId)
                .set(progress)
                .await()
        }
    }

    // =========================
    // REVIEWS
    // =========================

    private suspend fun seedReviews(userId: String) {
        val reviews = listOf(
            Review(
                reviewId = "r1_${userId}",
                userId = userId,
                wordId = "w1_${userId}",
                quality = ReviewQuality.GOOD,
                previousInterval = 3,
                newInterval = 7,
                previousEaseFactor = 2.5,
                newEaseFactor = 2.6
            ),
            Review(
                reviewId = "r2_${userId}",
                userId = userId,
                wordId = "w2_${userId}",
                quality = ReviewQuality.HARD,
                previousInterval = 1,
                newInterval = 1,
                previousEaseFactor = 2.5,
                newEaseFactor = 2.3
            ),
            Review(
                reviewId = "r3_${userId}",
                userId = userId,
                wordId = "w4_${userId}",
                quality = ReviewQuality.EASY,
                previousInterval = 7,
                newInterval = 14,
                previousEaseFactor = 2.6,
                newEaseFactor = 2.8
            )
        )

        reviews.forEach { review ->
            db.collection("reviews")
                .document(review.reviewId)
                .set(review)
                .await()
        }
    }

    // =========================
    // STUDY SESSIONS
    // =========================

    private suspend fun seedStudySessions(userId: String) {
        val sessions = listOf(
            StudySession(
                sessionId = "s1_${userId}",
                userId = userId,
                reviewedWords = 20,
                correctAnswers = 18,
                wrongAnswers = 2,
                durationMinutes = 15,
                startedAt = System.currentTimeMillis() - 900000,
                endedAt = System.currentTimeMillis()
            ),
            StudySession(
                sessionId = "s2_${userId}",
                userId = userId,
                reviewedWords = 35,
                correctAnswers = 30,
                wrongAnswers = 5,
                durationMinutes = 25,
                startedAt = System.currentTimeMillis() - 1500000,
                endedAt = System.currentTimeMillis()
            )
        )

        sessions.forEach { session ->
            db.collection("study_sessions")
                .document(session.sessionId)
                .set(session)
                .await()
        }
    }

    // =========================
    // NOTIFICATIONS
    // =========================

    private suspend fun seedNotifications(userId: String) {
        val notifications = listOf(
            AppNotification(
                notificationId = "n1_${userId}",
                userId = userId,
                title = "Daily Reminder",
                message = "You have 20 words to review today."
            ),
            AppNotification(
                notificationId = "n2_${userId}",
                userId = userId,
                title = "Great Job",
                message = "You reached a 12-day streak."
            ),
            AppNotification(
                notificationId = "n3_${userId}",
                userId = userId,
                title = "Review Time",
                message = "Your TOEIC vocabulary is ready for review."
            )
        )

        notifications.forEach { notification ->
            db.collection("notifications")
                .document(notification.notificationId)
                .set(notification)
                .await()
        }
    }
}