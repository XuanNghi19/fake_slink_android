package com.example.fake_slink.helpers

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.time.DayOfWeek

class AppHelper {
    companion object {
        fun getSpecificDate(dayOfWeek: Int): String {
            val calendar = Calendar.getInstance()
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val delta = dayOfWeek - currentDayOfWeek
            calendar.add(Calendar.DAY_OF_MONTH, delta)

            val dateFormat = SimpleDateFormat("dd/MM")
            return dateFormat.format(calendar.time)
        }
    }
}