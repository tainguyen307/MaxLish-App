package com.example.maxlish.data.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.maxlish.data.helper.NotificationHelper

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        NotificationHelper.show(
            applicationContext,
            "📚 Học hôm nay chưa?",
            "Duy trì 10 phút mỗi ngày để nhớ lâu hơn"
        )

        return Result.success()
    }
}