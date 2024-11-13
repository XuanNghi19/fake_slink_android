package com.example.fake_slink

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.notification.NotificationUtils
import com.example.fake_slink.repository.NotificationRepository

class MyApplication : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val dbHelper = DatabaseHelper(this@MyApplication)
        val notificationRepository = NotificationRepository(dbHelper)
        notificationRepository.eraseOldNotification()
        NotificationUtils.createServerNotificationChannel(this@MyApplication)
        NotificationUtils.createLocalNotificationChannel(this@MyApplication)
    }
}