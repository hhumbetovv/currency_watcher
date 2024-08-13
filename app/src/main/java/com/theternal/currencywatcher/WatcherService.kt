package com.theternal.currencywatcher

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.theternal.domain.usecases.CheckWatchersUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WatcherService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var checkWatchersUseCase: CheckWatchersUseCase

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESC
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(desc: String) {

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(com.theternal.watcher_list.R.id.watcher_list_nav_graph)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Threshold Exceeded")
            .setContentText(desc)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(
                RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
            ))
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@WatcherService,
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
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            1,
            NotificationCompat.Builder(this, CHANNEL_ID).setPriority(
                NotificationCompat.PRIORITY_HIGH,
            )
                .setContentTitle("Watcher Service")
                .setContentText("Currencies Observing")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
        )


        scope.launch {
            while(true) {
                delay(900000)
                val recordCount = checkWatchersUseCase()

                if(recordCount != 0) {
                    sendNotification(
                        "$recordCount new records available",
                    )
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}