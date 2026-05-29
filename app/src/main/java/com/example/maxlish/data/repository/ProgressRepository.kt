package com.example.maxlish.data.repository

import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.StudySession
import com.example.maxlish.data.model.User
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getUserStats(userId: String): Flow<User?>
    fun getStudySessions(userId: String): Flow<List<StudySession>>
    suspend fun saveStudySession(session: StudySession): Result<Unit>
}
