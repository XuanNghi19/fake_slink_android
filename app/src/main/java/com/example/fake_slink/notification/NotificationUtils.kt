package com.example.fake_slink.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import com.example.fake_slink.enums.ChanelID
import com.example.fake_slink.enums.ClassifyNotification
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.ExamScheduleResponse
import com.example.fake_slink.model.response.TimeTableResponse

object NotificationUtils {
    fun createServerNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = ChanelID.SERVER_NOTIFICATION_CHANEL.toString()
            val channelName = ChanelID.SERVER_NOTIFICATION_CHANEL.toString()
            val description = "Notification chanel server side"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            val existingChannels = notificationManager?.notificationChannels
            val channelExists = existingChannels?.any { it.id == channelId } ?: false
            Log.e("NotificationChannel", "Channel exists: $channelExists")
        }
    }

    fun createLocalNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = ChanelID.LOCAL_NOTIFICATION_CHANEL.toString()
            val channelName = ChanelID.LOCAL_NOTIFICATION_CHANEL.toString()
            val description = "Notification chanel server side"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            val existingChannels = notificationManager?.notificationChannels
            val channelExists = existingChannels?.any { it.id == channelId } ?: false
            Log.e("NotificationChannel", "Channel exists: $channelExists")
        }
    }

    fun createClassScheduleNotification(context: Context, calendar: Calendar, timeTableResponse: TimeTableResponse) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val classify = ClassifyNotification.SCHEDULE.toString()
        val subject = timeTableResponse.classSubjectResponse.subjectResponse.subjectName
        val location = timeTableResponse.classSubjectResponse.location
        val time = timeTableResponse.startTime

        val notifyTime =  calendar.apply {
            add(Calendar.HOUR_OF_DAY, - 1)
        }.timeInMillis

        val requestCode = (
                calendar.get(Calendar.YEAR) * 10000
                + calendar.get(Calendar.MONTH) * 100
                + calendar.get(Calendar.DAY_OF_MONTH)
                )

        if(notifyTime > System.currentTimeMillis()) {
            val intent = Intent(context, LocalNotificationReceiver::class.java).apply {
                putExtra("classify", classify)
                putExtra("subject", subject)
                putExtra("location", location)
                putExtra("time", time)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if(pendingIntent == null) {
                val newPendingIntent = PendingIntent.getBroadcast(
                    context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, newPendingIntent)
            }
        }
    }

    fun createExamScheduleNotification(context: Context, examScheduleResponse: ExamScheduleResponse) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val classify = ClassifyNotification.EXAM_SCHEDULE.toString()
        val subject = examScheduleResponse.classSubjectResponse.subjectResponse.subjectName
        val location = examScheduleResponse.location
        val time = examScheduleResponse.startTime
        val dateOfEvent = AppHelper.formatDateToDay(examScheduleResponse.dateOfEvent)

        val calendar = Calendar.getInstance()
        calendar.time = examScheduleResponse.dateOfEvent
        val notifyTime =  calendar.apply {
            add(Calendar.HOUR_OF_DAY, - 1)
        }.timeInMillis

        val requestCode = (
                calendar.get(Calendar.YEAR) * 10000
                        + calendar.get(Calendar.MONTH) * 100
                        + calendar.get(Calendar.DAY_OF_MONTH)
                )

        if(notifyTime > System.currentTimeMillis()) {
            val intent = Intent(context, LocalNotificationReceiver::class.java).apply {
                putExtra("classify", classify)
                putExtra("subject", subject)
                putExtra("location", location)
                putExtra("time", time)
                putExtra("dateOfEvent", dateOfEvent)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if(pendingIntent == null) {
                val newPendingIntent = PendingIntent.getBroadcast(
                    context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, newPendingIntent)
            }
        }
    }
}





























