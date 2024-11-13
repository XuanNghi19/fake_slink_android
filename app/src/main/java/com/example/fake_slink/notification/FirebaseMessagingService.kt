package com.example.fake_slink.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.fake_slink.R
import com.example.fake_slink.activities.HomeActivity
import com.example.fake_slink.activities.MainActivity
import com.example.fake_slink.adapter.DateAdapter
import com.example.fake_slink.enums.ChanelID
import com.example.fake_slink.enums.ClassifyNotification
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.response.ReviewFormResponse
import com.example.fake_slink.model.singleton.GradeAppealsList
import com.example.fake_slink.model.singleton.LearningOutcomes
import com.example.fake_slink.model.singleton.ReviewFormList
import com.example.fake_slink.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import java.util.Date

class FirebaseMessagingService : FirebaseMessagingService(){
    private val TAG = "FIREBASE_MESSENGER"
    val dbHelper = DatabaseHelper(this@FirebaseMessagingService)
    val notificationRepository = NotificationRepository(dbHelper)
    val gson = GsonBuilder().registerTypeAdapter(Date::class.java, DateAdapter()).create()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val TAG = "FCM"
        if(message.data.isNotEmpty()) {
            Log.d(TAG, "Nhan duoc data thong bao: ${message.data}")

            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)

            val classify = message.data["classify"] ?: ""
            val title = message.data["title"] ?: "Thông báo mới"
            val messageContent = message.data["message"] ?: "Bạn có một thông báo mới"
            val createAtJson = message.data["createAt"]
            val createAt =  gson.fromJson(createAtJson, Date::class.java)

            // tạo một thông báo và lưu vào database
            val notification = Notification(
                null,
                title,
                createAt,
                messageContent,
                null,
                false
            )
            val c1 = notificationRepository.insertNotification(notification)

            if(classify == ClassifyNotification.grade.toString()) {
                val gradeResponseJson = message.data["gradeResponse"]
                val gradeResponse = gson.fromJson(gradeResponseJson, GradeResponse::class.java)

                // cập nhật lại dữ liệu điểm số
                if(token != null) {
                    // cập nhật lại singleton GradeAppealsList
                    if(gradeResponse.appealsDateline != null &&
                        (gradeResponse.appealsDateline.before(Date())) || gradeResponse.appealsDateline == Date()) {
                        GradeAppealsList.add(gradeResponse)
                    }
                    // cập nhật lại singleton LearningOutcomes
                    LearningOutcomes.update(gradeResponse)
                    val intent = Intent(this@FirebaseMessagingService, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    }

                    // hiển thị thông báo
                    showNotification(title, messageContent, intent)
                } else {
                    // hiển thị thông báo
                    val intent = Intent(this@FirebaseMessagingService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    showNotification(title, messageContent, intent)
                }

            } else if(classify == ClassifyNotification.reviewForm.toString()) {
                val reviewFormResponseJson = message.data["reviewFormResponse"]
                val reviewFormResponse = gson.fromJson(reviewFormResponseJson, ReviewFormResponse::class.java)

                // cập nhật lại dữ liệu đơn phúc khảo
                if(token != null) {
                    // cập nhật lại singleton LearningOutcomes
                    ReviewFormList.update(reviewFormResponse)
                    val intent = Intent(this@FirebaseMessagingService, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    // hiển thị thông báo
                    showNotification(title, messageContent, intent)
                } else {
                    // hiển thị thông báo
                    val intent = Intent(this@FirebaseMessagingService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    }
                    showNotification(title, messageContent, intent)
                }
            }
        }
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
    private fun showNotification(title: String, message: String, intent: Intent) {

        val pendingIntent = PendingIntent.getActivity(
            this@FirebaseMessagingService, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, ChanelID.SERVER_NOTIFICATION_CHANEL.toString())
            .setSmallIcon(R.drawable.mdi_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        notificationManager.notify(notificationId, builder.build())
    }
}