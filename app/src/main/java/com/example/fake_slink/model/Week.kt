package com.example.fake_slink.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fake_slink.model.response.SemesterResponse
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters

class Week(
    val weekName: String,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun createWeeks(semesterResponse: SemesterResponse): List<Week> {
            val weeks = mutableListOf<Week>()

            val startDateSemester = semesterResponse.startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            val endDateSemester = semesterResponse.endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()

            var weekStart = startDateSemester
            var weekCount = 1
            while (weekStart.isBefore(endDateSemester) || weekStart.equals(endDateSemester)) {
                val weekEnd = weekStart.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))

                var dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val weekStartStr = weekStart.format(dateFormat)
                val weekEndtStr = weekEnd.format(dateFormat)
                val weekNameStr = "Tuần $weekCount: $weekStartStr -> $weekEndtStr"

                weeks.add(Week(weekNameStr, weekStart, weekEnd))
                weekStart = weekEnd.plusDays(1)
                weekCount++
            }

            return weeks
        }
    }
}