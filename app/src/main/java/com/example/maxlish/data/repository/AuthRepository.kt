package com.example.maxlish.data.repository

import com.example.maxlish.data.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, displayName: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): User?
    suspend fun updateProfile(displayName: String, learningGoal: String, level: String): Result<Unit>
}
