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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID

object AppHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    fun getSpecificDate(dayOfWeek: Int, weeksToAdd: Int): String {
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val delta = dayOfWeek - currentDayOfWeek + (weeksToAdd * 7)
        if (delta < 0) return ""
        calendar.add(Calendar.DAY_OF_MONTH, delta)

        val dateFormat = SimpleDateFormat("dd/MM")
        return dateFormat.format(calendar.time)
    }

    fun getSpecificDateByCalendar(dayOfWeek: Int, weeksToAdd: Int, startTime: String): Calendar {
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val delta = dayOfWeek - currentDayOfWeek + (weeksToAdd * 7)
        if (delta < 0) return calendar
        calendar.add(Calendar.DAY_OF_MONTH, delta)

        val timePair = startTime.split(":")
        if(timePair.size == 2) {
            val hour = timePair[0].toIntOrNull() ?: 0
            val minute = timePair[1].toIntOrNull() ?: 0
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }

        return calendar
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
            if (it.moveToFirst()) {
                val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                if (nameIdx != -1) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fromDate(date: Date): String {
        return simpleDateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDate(dateString: String): Date {
        return simpleDateFormat.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatLocalDateTimeToDate(localDateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatLocalDateTimeToTime(localDateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return localDateTime.format(formatter)
    }

    fun formatDateToDay(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(date)
    }

    fun formatDateToTime(date: Date): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(date)
    }
}