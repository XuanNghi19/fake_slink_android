package com.example.fake_slink.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.response.TimeTableResponse
import java.text.SimpleDateFormat

class ItemTkbMiniAdapter(
    private val context: Activity,
    private val list: List<TimeTableResponse>
) : ArrayAdapter<TimeTableResponse>(
    context,
    R.layout.item_tkb_mini,
    list
) {

    data class DateTimeTablePair(
        val dateStr: String,
        val timeTable: TimeTableResponse,
    )
    private val next7LearningDates: List<DateTimeTablePair> = getNext7LearningDays(list)

    override fun getCount(): Int {
        return next7LearningDates.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_tkb_mini, null, true)

        val classSubjectResponse = next7LearningDates[position]
            .timeTable.classSubjectResponse
        val timeTable = next7LearningDates[position].timeTable

        val day = rowView.findViewById<TextView>(R.id.day)
        day.text = next7LearningDates[position].dateStr

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

    private fun getNext7LearningDays(timetableList: List<TimeTableResponse>): List<DateTimeTablePair> {
        val dateTimeTablePair = mutableListOf<DateTimeTablePair>()

        // số tuần cần duyet
        var weekToAdd = 0

        while (dateTimeTablePair.size < 7) {
            for(timeTable in timetableList) {
                val dateStr = AppHelper.getSpecificDate(timeTable.dayOfWeek, weekToAdd)

                val pair = DateTimeTablePair(dateStr, timeTable)
                if(dateTimeTablePair.size < 7 &&
                    !dateTimeTablePair.any{it.dateStr == dateStr}) {
                    dateTimeTablePair.add(pair)
                }
            }
            weekToAdd++
        }

        return dateTimeTablePair.sortedBy {
            SimpleDateFormat("dd/MM").parse(it.dateStr)
        }
    }
}