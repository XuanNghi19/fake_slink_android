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
    }
}