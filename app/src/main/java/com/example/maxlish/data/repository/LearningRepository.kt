package com.example.maxlish.data.repository

import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.Review
import kotlinx.coroutines.flow.Flow

interface LearningRepository {

    fun observeLearningProgress(
        userId: String
    ): Flow<List<LearningProgress>>

    suspend fun getLearningProgress(
        userId: String
    ): Result<List<LearningProgress>>

    suspend fun getDueReviews(
        userId: String
    ): Result<List<LearningProgress>>

    suspend fun saveLearningProgress(
        progress: LearningProgress
    ): Result<Unit>

    suspend fun updateLearningProgress(
        progress: LearningProgress
    ): Result<Unit>

    suspend fun saveReview(
        review: Review
    ): Result<Unit>
}