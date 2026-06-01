package com.example.maxlish.data.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.maxlish.data.helper.NotificationHelper
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.google.firebase.firestore.FirebaseFirestore

class DueWordsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repo = FirebaseProgressRepository(FirebaseFirestore.getInstance())

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        val userId = inputData.getString("userId")
            ?: return Result.failure()

        return try {

            val dueWords = repo.getDueWords(userId)

            if (dueWords.isNotEmpty()) {
                NotificationHelper.show(
                    applicationContext,
                    "⏰ Đến giờ ôn tập",
                    "Bạn có ${dueWords.size} từ cần học hôm nay"
                )
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}