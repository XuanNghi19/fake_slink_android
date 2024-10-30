package com.example.fake_slink.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.activities.GradeDetailActivity
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.singleton.GradeDetail

class ItemGradeAdapter(private val gradeList: List<GradeResponse>) : RecyclerView.Adapter<ItemGradeAdapter.ItemGradeViewHolder>(){
    inner class ItemGradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val row: TableRow = view.findViewById(R.id.row)
        val col1: TextView = view.findViewById(R.id.col1)
        val col2: TextView = view.findViewById(R.id.col2)
        val col3: TextView = view.findViewById(R.id.col3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_kqht, parent, false)

        return ItemGradeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gradeList.size
    }

    override fun onBindViewHolder(holder: ItemGradeViewHolder, position: Int) {
        holder.col1.text = gradeList[position].classSubjectResponse?.subjectResponse?.subjectName
        holder.col2.text = gradeList[position].classSubjectResponse?.subjectResponse?.credits.toString()
        if (gradeList[position].status == "Đã hoàn thành môn học") {
            holder.col3.text = AppHelper.convertScoreToGrade(gradeList[position].diemTK)
        } else if(gradeList[position].status == "Chưa hoàn thành môn học") {
            holder.col3.text = "__"
        }
        holder.row.setOnClickListener {
            GradeDetail.login(gradeList[position])
            holder.row.context.startActivity(Intent(holder.row.context, GradeDetailActivity::class.java))
        }

    }
}