package com.example.maxlish.data.helper

import android.content.Context
import androidx.work.*
import com.example.maxlish.data.worker.DailyReminderWorker
import com.example.maxlish.data.worker.DueWordsWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun schedule(context: Context, userId: String) {

        // DAILY REMINDER
        val dailyWork = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            1, TimeUnit.DAYS
        ).build()
        // TEST FEAT WHEN OPEN APP
//        val work = OneTimeWorkRequestBuilder<DailyReminderWorker>()
//            .build()
//
//        WorkManager.getInstance(context).enqueue(work)

        // DUE WORDS CHECK
        val dueWork = PeriodicWorkRequestBuilder<DueWordsWorker>(
            6, TimeUnit.HOURS
        )
            .setInputData(workDataOf("userId" to userId))
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWork
        )

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "due_words",
            ExistingPeriodicWorkPolicy.UPDATE,
            dueWork
        )
    }
}