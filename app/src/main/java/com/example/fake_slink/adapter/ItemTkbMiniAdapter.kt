package com.example.fake_slink.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.response.TimeTableResponse
import java.util.ArrayList

class ItemTkbMiniAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<TimeTableResponse>
) : ArrayAdapter<TimeTableResponse>(
    context,
    R.layout.item_tkb_mini,
    arrayList as List<TimeTableResponse>
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_tkb_mini, null, true)

        val classSubjectResponse = arrayList[position].classSubjectResponse

        val day = rowView.findViewById<TextView>(R.id.day)
        day.text = AppHelper.getSpecificDate(arrayList[position].dayOfWeek)
        val time = rowView.findViewById<TextView>(R.id.time)
        time.text = arrayList[position].startTime + " -> " + arrayList[position].endTime
        val subject_name = rowView.findViewById<TextView>(R.id.subject_name)
        subject_name.text = classSubjectResponse.subjectResponse.subjectName
        val text_subject_id = rowView.findViewById<TextView>(R.id.text_subject_id)
        text_subject_id.text = classSubjectResponse.subjectResponse.idNum
        val text_teacher = rowView.findViewById<TextView>(R.id.text_teacher)
        text_teacher.text = classSubjectResponse.teacherResponse.name
        val text_location = rowView.findViewById<TextView>(R.id.text_location)
        text_location.text = classSubjectResponse.location


        return super.getView(position, convertView, parent)
    }
}