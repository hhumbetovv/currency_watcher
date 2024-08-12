package com.theternal.currencywatcher

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.theternal.domain.usecases.CheckWatchersUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WatcherWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkWatchersUseCase: CheckWatchersUseCase,
) : CoroutineWorker(context, workerParams) {

    private val notificationManager: NotificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESC
        }

        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun doWork(): Result {
        return try {
            val recordCount = checkWatchersUseCase()

            if(recordCount != 0) {
                sendNotification(
                    "$recordCount new records available",
                )
            }

            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    private fun sendNotification(desc: String) {
        createChannel()

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(com.theternal.watcher_list.R.id.watcher_list_nav_graph)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Threshold Exceeded")
            .setContentText(desc)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            ))
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, notification.build())
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "watcher_notifications"
        const val CHANNEL_NAME = "Watcher Notifications"
        const val CHANNEL_DESC = "Channel for Listening Currency Changes"
        const val WORK_NAME = "WatcherWorker"
    }
}