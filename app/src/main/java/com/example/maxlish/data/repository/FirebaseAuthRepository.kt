package com.example.maxlish.data.repository

import com.example.maxlish.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Đăng nhập thất bại"))
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

    override suspend fun updateProfile(
        displayName: String,
        learningGoal: String,
        level: String
    ): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser
                ?: return Result.failure(Exception("Người dùng chưa đăng nhập"))
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            firebaseUser.updateProfile(profileUpdates).await()
            // Lưu learningGoal và level vào SharedPreferences (vì Firebase Auth không có custom fields)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapFirebaseError(e: Exception): Exception {
        val message = e.message ?: ""
        return when {
            message.contains("INVALID_LOGIN_CREDENTIALS") ||
            message.contains("wrong-password") ||
            message.contains("user-not-found") ->
                Exception("Email hoặc mật khẩu không đúng")
            message.contains("email-already-in-use") ->
                Exception("Email này đã được đăng ký")
            message.contains("invalid-email") ->
                Exception("Địa chỉ email không hợp lệ")
            message.contains("weak-password") ->
                Exception("Mật khẩu quá yếu, cần ít nhất 6 ký tự")
            message.contains("network") ->
                Exception("Không có kết nối mạng, vui lòng thử lại")
            else -> Exception("Đã có lỗi xảy ra: ${e.message}")
        }
    }
}
