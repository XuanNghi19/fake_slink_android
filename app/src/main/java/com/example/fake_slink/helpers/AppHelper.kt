package com.example.fake_slink.helpers

import android.content.Context
import android.icu.util.Calendar
import android.util.TypedValue
import java.text.SimpleDateFormat

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
    }
}