package com.example.fake_slink.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.activities.ExamScheduleDetailActivity
import com.example.fake_slink.model.response.ExamScheduleResponse
import com.example.fake_slink.model.singleton.ExamScheduleDetail
import java.text.SimpleDateFormat
import java.util.Locale

class ItemExamScheduleAdapter(private val examScheduleList: List<ExamScheduleResponse>)
    : RecyclerView.Adapter<ItemExamScheduleAdapter.ItemExamScheduleHolder>()
{
    inner class ItemExamScheduleHolder(view: View) : RecyclerView.ViewHolder(view) {
        val row: TableRow = view.findViewById(R.id.row)
        val col1: TextView = view.findViewById(R.id.col1)
        val col2: TextView = view.findViewById(R.id.col2)
        val col3: TextView = view.findViewById(R.id.col3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemExamScheduleHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_exam_schedule, parent, false)

        return ItemExamScheduleHolder(view)
    }

    override fun getItemCount(): Int {
        return examScheduleList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemExamScheduleHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(examScheduleList[position].dateOfEvent)
        holder.col1.text = formattedDate
        holder.col2.text = examScheduleList[position].classSubjectResponse.subjectResponse.subjectName
        holder.col3.text = "${examScheduleList[position].minute} phút"

        holder.row.setOnClickListener {
            ExamScheduleDetail.login(examScheduleList[position])
            holder.row.context.startActivity(Intent(holder.row.context, ExamScheduleDetailActivity::class.java))
        }
    }
}