package com.example.fake_slink.helpers

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.TypedValue
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.UUID

class AppHelper {
    companion object {
        fun getSpecificDate(dayOfWeek: Int, weeksToAdd: Int): String {
            val calendar = Calendar.getInstance()
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val delta = dayOfWeek - currentDayOfWeek + (weeksToAdd * 7)
            if(delta < 0) return ""
            calendar.add(Calendar.DAY_OF_MONTH, delta)

            val dateFormat = SimpleDateFormat("dd/MM")
            return dateFormat.format(calendar.time)
        }
        fun convertScoreToGrade(score: Float): String {
            return when {
                score >= 9.0 -> "A+"
                score >= 8.5 -> "A"
                score >= 8.0 -> "B+"
                score >= 7.0 -> "B"
                score >= 6.5 -> "C+"
                score >= 5.5 -> "C"
                score >= 5.0 -> "D+"
                score >= 4.0 -> "D"
                else -> "F"
            }
        }
        fun convertScoreToGradeNum(score: Float): String {
            return when {
                score >= 9.0 -> "4.0"
                score >= 8.5 -> "3.7"
                score >= 8.0 -> "3.5"
                score >= 7.0 -> "3.0"
                score >= 6.5 -> "2.5"
                score >= 5.5 -> "2.0"
                score >= 5.0 -> "1.5"
                score >= 4.0 -> "1"
                else -> "F"
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getFileName(context: Context, uri: Uri): String? {
            var name: String? = null
            val contentResolver: ContentResolver = context.contentResolver

            val cursor: Cursor? = contentResolver.query(uri, null, null, null)
            cursor?.use {
                if(it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    if(nameIdx != -1) {
                        name = it.getString(nameIdx)
                    }
                }
            }

            return name
        }

        fun getFileExtension(context: Context, uri: Uri): String? {
            val contentResolver: ContentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri)

            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }

        fun generateUniqueStringUsingUUID(): String {
            return UUID.randomUUID().toString()
        }
    }
}