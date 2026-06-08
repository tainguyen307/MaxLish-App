package com.example.maxlish.data.repository

import com.example.maxlish.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private suspend fun checkAndCreateUserInFirestore(uid: String, email: String, displayName: String) {
        val userDocRef = firestore.collection("users").document(uid)
        val doc = userDocRef.get().await()
        if (!doc.exists()) {
            val newUser = User(
                uid = uid,
                email = email,
                displayName = displayName,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            userDocRef.set(newUser).await()
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Đăng nhập thất bại"))
            checkAndCreateUserInFirestore(firebaseUser.uid, firebaseUser.email ?: "", firebaseUser.displayName ?: "")
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
            )
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Đăng ký thất bại"))
            // Cập nhật displayName sau khi tạo tài khoản
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            firebaseUser.updateProfile(profileUpdates).await()
            checkAndCreateUserInFirestore(firebaseUser.uid, firebaseUser.email ?: "", displayName)
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = displayName
                )
            )
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Đăng nhập Google thất bại"))
            checkAndCreateUserInFirestore(firebaseUser.uid, firebaseUser.email ?: "", firebaseUser.displayName ?: "")
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
            )
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: ""
        )
    }

    override suspend fun getUserProfile(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return try {
            val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
            if (doc.exists()) {
                doc.toObject(User::class.java)
            } else {
                // Fallback: trả về thông tin cơ bản từ Firebase Auth
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
            }
        } catch (e: Exception) {
            // Fallback nếu lỗi mạng
            User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: ""
            )
        }
    }

    override suspend fun updateProfile(
        displayName: String,
        learningGoal: String,
        level: String,
        dailyNewWords: Int
    ): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(Exception("Người dùng chưa đăng nhập"))
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            firebaseUser.updateProfile(profileUpdates).await()

            // Save to Firestore 'users' collection
            val userDocRef = firestore.collection("users").document(firebaseUser.uid)
            val docSnapshot = userDocRef.get().await()

            val updatedUser = if (docSnapshot.exists()) {
                val existingUser = docSnapshot.toObject(User::class.java)!!
                existingUser.copy(
                    displayName = displayName,
                    learningGoal = learningGoal,
                    level = level,
                    dailyNewWords = dailyNewWords,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = displayName,
                    learningGoal = learningGoal,
                    level = level,
                    dailyNewWords = dailyNewWords,
                    updatedAt = System.currentTimeMillis()
                )
            }

            userDocRef.set(updatedUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun mapFirebaseError(e: Exception): Exception {
        val message = e.message ?: ""
        return when {
            message.contains("auth credential") ->
                Exception("Email hoặc mật khẩu không đúng.")
            message.contains("already in use") ->
                Exception("Email này đã được đăng ký.")
            message.contains("email") ->
                Exception("Địa chỉ email không hợp lệ.")
            message.contains("at least") ->
                Exception("Mật khẩu quá yếu, cần ít nhất 6 ký tự.")
            message.contains("network") ->
                Exception("Không có kết nối mạng, vui lòng thử lại")
            else -> Exception("Đã có lỗi xảy ra: ${e.message}")
        }
    }
}
