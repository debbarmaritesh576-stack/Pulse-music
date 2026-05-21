package com.pulse.music.backup

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun schedulePeriodicBackup(intervalHours: Int = 24) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val request = PeriodicWorkRequestBuilder<BackupWorker>(intervalHours.toLong(), TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag("pulse_backup")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "pulse_backup",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancelScheduledBackup() {
        WorkManager.getInstance(context).cancelAllWorkByTag("pulse_backup")
    }
}

class BackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}