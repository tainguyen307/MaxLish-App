package com.example.maxlish.data.repository

import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.Review

interface LearningRepository {

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