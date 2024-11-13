package com.example.fake_slink.notification

import android.app.NotificationManager
import android.app.PendingIntent
import com.example.fake_slink.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.fake_slink.activities.HomeActivity
import com.example.fake_slink.enums.ChanelID
import com.example.fake_slink.enums.ClassifyNotification
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.repository.NotificationRepository
import java.util.Date

class LocalNotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val dbHelper = DatabaseHelper(context!!)
        val notificationRepository = NotificationRepository(dbHelper)

        val classify = intent?.getStringExtra("classify") ?: ""
        val subject = intent?.getStringExtra("subject") ?: "Học phần"
        val location = intent?.getStringExtra("location") ?: "101A2"
        val time = intent?.getStringExtra("time") ?: "08:00"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = System.currentTimeMillis().toInt()

        val notificationIntent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        when(classify) {
            ClassifyNotification.SCHEDULE.toString() -> {

                val title = "Thông bao lịch học"
                val messageContent = "Bạn có lịch học $subject, địa điểm $location vào lúc $time."

                val notify = NotificationCompat.Builder(context, ChanelID.LOCAL_NOTIFICATION_CHANEL.toString())
                    .setSmallIcon(R.drawable.mdi_notifications)
                    .setContentTitle(title)
                    .setContentText(messageContent)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(messageContent)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                val notification = Notification(
                    null,
                    title,
                    Date(),
                    messageContent,
                    null,
                    false
                )
                notificationRepository.insertNotification(notification)

                notificationManager.notify(notificationId, notify)
            }
            ClassifyNotification.EXAM_SCHEDULE.toString() -> {
                val dateOfEvent = intent?.getStringExtra("dateOfEvent") ?: "00/00/0000"
                val title = "Thông bao lịch thi $subject"
                val messageContent = "Bạn có lịch thi $subject, địa điểm $location vào lúc $time, ngày $dateOfEvent."

                val notify = NotificationCompat.Builder(context, ChanelID.LOCAL_NOTIFICATION_CHANEL.toString())
                    .setSmallIcon(R.drawable.mdi_notifications)
                    .setContentTitle(title)
                    .setContentText(messageContent)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(messageContent)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                val notification = Notification(
                    null,
                    title,
                    Date(),
                    messageContent,
                    null,
                    false
                )
                notificationRepository.insertNotification(notification)

                notificationManager.notify(notificationId, notify)
            }
        }
    }
}