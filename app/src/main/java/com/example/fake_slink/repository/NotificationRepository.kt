package com.example.fake_slink.repository

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.notification.Notification
import java.time.LocalDateTime

class NotificationRepository(private val dbHelper: DatabaseHelper) {

    // Them thong bao
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNotification(notification: Notification): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("notification_id", notification.notificationID)
            put("status", notification.status)
            put("title", notification.title)
            put("create_at", AppHelper.fromDate(notification.createAt))
            put("content", notification.content)
        }

        return db.insert("notification", null, contentValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllNotifications(): List<Notification> {
        val db = dbHelper.readableDatabase
        val notifications = mutableListOf<Notification>()
        val cursor = db.query(
            "notification",
            null,
            null,
            null,
            null,
            null,
            null
        )

        if(cursor.moveToFirst()) {
            with(cursor) {
                while (moveToNext()) {
                    val id = getLong(getColumnIndexOrThrow("notification_id"))
                    val status = getInt(getColumnIndexOrThrow("status")) != 0
                    val title = getString(getColumnIndexOrThrow("title"))
                    val content = getString(getColumnIndexOrThrow("content"))
                    val createAt = AppHelper.toDate(
                        getString(getColumnIndexOrThrow("create_at"))
                    )
                    val lastReadDate = getString(getColumnIndexOrThrow("last_read_date"))?.let {
                        AppHelper.toLocalDateTime(it)
                    }


                    notifications.add(
                        Notification(id, title, createAt, content, lastReadDate, status)
                    )
                }
            }
        } else {
            Log.d("CursorCheck", "Không có dữ liệu trong bảng notification.")
        }

        cursor.close()
        return notifications.reversed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLastReadDate(notificationID: Long, lastReadDate: LocalDateTime): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("last_read_date", AppHelper.fromLocalDateTime(lastReadDate))
            put("status", 1)
        }
        return db.update(
            "notification",
            contentValues,
            "notification_id = ?",
            arrayOf(notificationID.toString())
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun eraseOldNotification() {
        val db = dbHelper.writableDatabase
        val currentDate = LocalDateTime.now()
        val thresholdDateString = currentDate.minusDays(15).toString()

        val whereClause = "last_read_date <= ? AND status = 1"
        val whereArgs = arrayOf(thresholdDateString)

        val deletedRows = db.delete("notification", whereClause, whereArgs)

        if(deletedRows > 0) {
            Log.d("Database", "$deletedRows rows deleted.")
        } else {
            Log.d("Database", "No rows were deleted.")
        }

        db.close()
    }

    fun checkAlRead(): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("status", 1)
        }

        return db.update(
            "notification",
            contentValues,
            "status = 0",
            null
        )
    }
}