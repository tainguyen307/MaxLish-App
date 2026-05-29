package com.example.maxlish.data.repository

import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseProgressRepository(instance: FirebaseFirestore) : ProgressRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override fun getUserStats(userId: String): Flow<User?> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(User::class.java))
                } else {
                    // Trả về một User mặc định nếu chưa có document trong Firestore
                    trySend(User(uid = userId))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getStudySessions(userId: String): Flow<List<StudySession>> = callbackFlow {
        val subscription = firestore.collection("study_sessions")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Có thể log lỗi ở đây nhưng không nhất thiết đóng stream nếu muốn giữ UI
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(StudySession::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun saveStudySession(session: StudySession): Result<Unit> {
        return try {
            firestore.collection("study_sessions").add(session).await()
            // Cập nhật tổng quát trong User profile (streak, total words, accuracy)
            // Việc này có thể thực hiện thông qua Cloud Functions hoặc trực tiếp ở đây
            // Ở đây tạm thời giả định client cập nhật.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
