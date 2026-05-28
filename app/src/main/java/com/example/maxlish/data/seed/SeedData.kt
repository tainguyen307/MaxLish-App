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

            seedUsers()
            Log.d("SEED", "Users seeded")

            seedVocabularySets()
            Log.d("SEED", "Vocabulary sets seeded")

            seedVocabularyWords()
            Log.d("SEED", "Vocabulary words seeded")

            seedLearningProgress()
            Log.d("SEED", "Learning progress seeded")

            seedReviews()
            Log.d("SEED", "Reviews seeded")

            seedStudySessions()
            Log.d("SEED", "Study sessions seeded")

            seedNotifications()
            Log.d("SEED", "Notifications seeded")

            Log.d("SEED", "All seed completed")

        } catch (e: Exception) {

            Log.e("SEED", "Seed failed: ${e.message}")
        }
    }

    // =========================
    // USERS
    // =========================

    private suspend fun seedUsers() {

        val users = listOf(

            User(
                uid = "u1",

                email = "tai@gmail.com",

                displayName = "Nguyen Tan Tai",

                learningGoal = LearningGoal.IELTS,

                level = UserLevel.B1,

                streak = 12,

                totalWordsLearned = 120,

                correctAnswers = 320,

                wrongAnswers = 40,

                xp = 2500
            ),

            User(
                uid = "u2",

                email = "bach@gmail.com",

                displayName = "Dang Thien Bach",

                learningGoal = LearningGoal.TOEIC,

                level = UserLevel.A2,

                streak = 5,

                totalWordsLearned = 60,

                correctAnswers = 120,

                wrongAnswers = 25,

                xp = 1000
            )
        )

        users.forEach { user ->

            db.collection("users")
                .document(user.uid)
                .set(user)
                .await()
        }
    }

    // =========================
    // VOCABULARY SETS
    // =========================

    private suspend fun seedVocabularySets() {

        val sets = listOf(

            VocabularySet(
                setId = "set_ielts_01",

                ownerId = "u1",

                title = "IELTS Academic Vocabulary",

                description = "Common academic IELTS words",

                tags = listOf(
                    "IELTS",
                    "Academic"
                ),

                wordCount = 3,

                isPublic = true
            ),

            VocabularySet(
                setId = "set_toeic_01",

                ownerId = "u2",

                title = "TOEIC Business Vocabulary",

                description = "Business English vocabulary",

                tags = listOf(
                    "TOEIC",
                    "Business"
                ),

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

    private suspend fun seedVocabularyWords() {

        val words = listOf(

            VocabularyWord(
                wordId = "w1",

                setId = "set_ielts_01",

                ownerId = "u1",

                word = "enhance",

                pronunciation = "/ɪnˈhɑːns/",

                meaning = "cải thiện",

                description = "to improve something",

                example = "Reading books enhances vocabulary.",

                collocations = listOf(
                    "enhance skills",
                    "enhance performance"
                ),

                relatedWords = listOf(
                    "improve",
                    "boost"
                ),

                difficulty = "Medium"
            ),

            VocabularyWord(
                wordId = "w2",

                setId = "set_ielts_01",

                ownerId = "u1",

                word = "allocate",

                pronunciation = "/ˈæləkeɪt/",

                meaning = "phân bổ",

                description = "to distribute resources",

                example = "The company allocated more budget.",

                collocations = listOf(
                    "allocate resources",
                    "allocate budget"
                ),

                relatedWords = listOf(
                    "assign",
                    "distribute"
                ),

                difficulty = "Hard"
            ),

            VocabularyWord(
                wordId = "w3",

                setId = "set_ielts_01",

                ownerId = "u1",

                word = "maintain",

                pronunciation = "/meɪnˈteɪn/",

                meaning = "duy trì",

                description = "to keep something at the same level",

                example = "Exercise helps maintain good health.",

                collocations = listOf(
                    "maintain balance",
                    "maintain quality"
                ),

                relatedWords = listOf(
                    "preserve",
                    "sustain"
                ),

                difficulty = "Easy"
            ),

            VocabularyWord(
                wordId = "w4",

                setId = "set_toeic_01",

                ownerId = "u2",

                word = "negotiate",

                pronunciation = "/nɪˈɡoʊʃieɪt/",

                meaning = "đàm phán",

                description = "to discuss to reach agreement",

                example = "They negotiated a better contract.",

                collocations = listOf(
                    "negotiate contract",
                    "negotiate price"
                ),

                relatedWords = listOf(
                    "discuss",
                    "bargain"
                ),

                difficulty = "Medium"
            ),

            VocabularyWord(
                wordId = "w5",

                setId = "set_toeic_01",

                ownerId = "u2",

                word = "revenue",

                pronunciation = "/ˈrevənuː/",

                meaning = "doanh thu",

                description = "income generated by business",

                example = "The company increased its revenue.",

                collocations = listOf(
                    "annual revenue",
                    "generate revenue"
                ),

                relatedWords = listOf(
                    "income",
                    "profit"
                ),

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

    private suspend fun seedLearningProgress() {

        val progresses = listOf(

            LearningProgress(
                progressId = "p1",

                userId = "u1",

                wordId = "w1",

                repetition = 3,

                interval = 7,

                easeFactor = 2.6,

                lastReviewedAt = System.currentTimeMillis(),

                nextReviewAt =
                    System.currentTimeMillis() + 86400000,

                correctCount = 5,

                wrongCount = 1,

                mastered = false
            ),

            LearningProgress(
                progressId = "p2",

                userId = "u1",

                wordId = "w2",

                repetition = 1,

                interval = 1,

                easeFactor = 2.3,

                correctCount = 1,

                wrongCount = 2,

                mastered = false
            ),

            LearningProgress(
                progressId = "p3",

                userId = "u2",

                wordId = "w4",

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

    private suspend fun seedReviews() {

        val reviews = listOf(

            Review(
                reviewId = "r1",

                userId = "u1",

                wordId = "w1",

                quality = ReviewQuality.GOOD,

                previousInterval = 3,

                newInterval = 7,

                previousEaseFactor = 2.5,

                newEaseFactor = 2.6
            ),

            Review(
                reviewId = "r2",

                userId = "u1",

                wordId = "w2",

                quality = ReviewQuality.HARD,

                previousInterval = 1,

                newInterval = 1,

                previousEaseFactor = 2.5,

                newEaseFactor = 2.3
            ),

            Review(
                reviewId = "r3",

                userId = "u2",

                wordId = "w4",

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

    private suspend fun seedStudySessions() {

        val sessions = listOf(

            StudySession(
                sessionId = "s1",

                userId = "u1",

                reviewedWords = 20,

                correctAnswers = 18,

                wrongAnswers = 2,

                durationMinutes = 15,

                startedAt =
                    System.currentTimeMillis() - 900000,

                endedAt =
                    System.currentTimeMillis()
            ),

            StudySession(
                sessionId = "s2",

                userId = "u2",

                reviewedWords = 35,

                correctAnswers = 30,

                wrongAnswers = 5,

                durationMinutes = 25,

                startedAt =
                    System.currentTimeMillis() - 1500000,

                endedAt =
                    System.currentTimeMillis()
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

    private suspend fun seedNotifications() {

        val notifications = listOf(

            AppNotification(
                notificationId = "n1",

                userId = "u1",

                title = "Daily Reminder",

                message = "You have 20 words to review today."
            ),

            AppNotification(
                notificationId = "n2",

                userId = "u1",

                title = "Great Job",

                message = "You reached a 12-day streak."
            ),

            AppNotification(
                notificationId = "n3",

                userId = "u2",

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