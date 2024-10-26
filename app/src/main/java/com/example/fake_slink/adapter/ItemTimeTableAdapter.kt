package com.example.fake_slink.adapter

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.fake_slink.R
import com.example.fake_slink.model.Week
import com.example.fake_slink.model.response.TimeTableResponse
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ItemTimeTableAdapter(
    private val context: Activity,
    private val list: List<TimeTableResponse>,
    private val week: Week
) : ArrayAdapter<TimeTableResponse>(
    context,
    R.layout.item_tkb_mini,
    list
) {
    data class DateTimeTablePair(
        val dateStr: String,
        val timeTable: TimeTableResponse,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private val timeTableInWeek = getTimeTableInWeek()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCount(): Int {
        return timeTableInWeek.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_tkb_mini, null, true)

        val classSubjectResponse = timeTableInWeek[position]
            .timeTable.classSubjectResponse
        val timeTable = timeTableInWeek[position].timeTable

        val day = rowView.findViewById<TextView>(R.id.day)
        day.text = timeTableInWeek[position].dateStr

        val time = rowView.findViewById<TextView>(R.id.time)
        time.text = timeTable.startTime + " -> " + timeTable.endTime

        val subject_name = rowView.findViewById<TextView>(R.id.subject_name)
        subject_name.text = classSubjectResponse.subjectResponse.subjectName

        val text_subject_id = rowView.findViewById<TextView>(R.id.text_subject_id)
        text_subject_id.text = classSubjectResponse.subjectResponse.idNum

        val text_teacher = rowView.findViewById<TextView>(R.id.text_teacher)
        text_teacher.text = classSubjectResponse.teacherResponse.name

        val text_location = rowView.findViewById<TextView>(R.id.text_location)
        text_location.text = classSubjectResponse.location

        return rowView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeTableInWeek(): MutableList<DateTimeTablePair>{
        var timeTableInWeekTmp = mutableListOf<DateTimeTablePair>()
        var currentDay = week.startDate
        var dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        while (!currentDay.isAfter(week.endDate)) {
            for (it in list) {
                var tmpDayOfWeek = 0;
                if(it.dayOfWeek == 1) {
                    tmpDayOfWeek = 7
                } else if(it.dayOfWeek > 1) {
                    tmpDayOfWeek = it.dayOfWeek - 1
                }
                if (currentDay.dayOfWeek.value == tmpDayOfWeek) {
                    timeTableInWeekTmp.add(
                        DateTimeTablePair(
                            currentDay.format(dateFormat),
                            it
                        )
                    )
                }
            }

            currentDay = currentDay.plusDays(1)
        }
        timeTableInWeekTmp.sortWith(compareBy(
            { LocalDate.parse(it.dateStr, dateFormat) },
            { LocalTime.parse(it.timeTable.startTime, timeFormat) }
        ))

        return timeTableInWeekTmp
    }
}