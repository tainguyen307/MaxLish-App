package com.example.maxlish.data.repository

import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseLearningRepository(
    private val firestore: FirebaseFirestore
) : LearningRepository {

    private val progressCollection =
        firestore.collection("learning_progress")

    private val reviewCollection =
        firestore.collection("reviews")

    override fun observeLearningProgress(
        userId: String
    ): Flow<List<LearningProgress>> = callbackFlow {
        val listener = progressCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val progressList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LearningProgress::class.java)?.copy(
                        progressId = doc.id
                    )
                } ?: emptyList()

                trySend(progressList)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getLearningProgress(
        userId: String
    ): Result<List<LearningProgress>> {

        return try {

            val snapshot =
                progressCollection
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

            val result =
                snapshot.documents.mapNotNull {
                    it.toObject(
                        LearningProgress::class.java
                    )
                }

            Result.success(result)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun getDueReviews(
        userId: String
    ): Result<List<LearningProgress>> {

        return try {

            val now =
                System.currentTimeMillis()

            val snapshot =
                progressCollection
                    .whereEqualTo("userId", userId)
                    .whereLessThanOrEqualTo(
                        "nextReviewAt",
                        now
                    )
                    .get()
                    .await()

            val result =
                snapshot.documents.mapNotNull {
                    it.toObject(
                        LearningProgress::class.java
                    )
                }

            Result.success(result)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun saveLearningProgress(
        progress: LearningProgress
    ): Result<Unit> {

        return try {

            progressCollection
                .document(progress.progressId)
                .set(progress)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateLearningProgress(
        progress: LearningProgress
    ): Result<Unit> {

        return try {

            progressCollection
                .document(progress.progressId)
                .set(progress)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun saveReview(
        review: Review
    ): Result<Unit> {

        return try {

            val docRef = if (review.reviewId.isBlank()) {
                reviewCollection.document()
            } else {
                reviewCollection.document(review.reviewId)
            }

            val finalReview = review.copy(reviewId = docRef.id)

            docRef.set(finalReview).await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}