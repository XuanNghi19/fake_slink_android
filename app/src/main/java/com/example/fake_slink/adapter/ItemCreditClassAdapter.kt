package com.example.fake_slink.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.activities.ClassSubjectDetailActivity
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.singleton.ClassSubjectDetails
import kotlin.math.ceil

class ItemCreditClassAdapter(private val classSubjectList: List<ClassSubjectResponse>) : RecyclerView.Adapter<ItemCreditClassAdapter.ItemCreditClassViewHolder>(){
    inner class ItemCreditClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item_1: CardView = view.findViewById(R.id.item_1)
        val credits_1: TextView = view.findViewById(R.id.credits_1)
        val text_name_1: TextView = view.findViewById(R.id.text_name_1)
        val item_2: CardView = view.findViewById(R.id.item_2)
        val credits_2: TextView = view.findViewById(R.id.credits_2)
        val text_name_2: TextView = view.findViewById(R.id.text_name_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCreditClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class_subject, parent, false)
        return ItemCreditClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ceil(classSubjectList.size / 2.0).toInt()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemCreditClassViewHolder, position: Int) {
        val adjustedPosition = position * 2
        val classsubject1 = classSubjectList[adjustedPosition]
        holder.credits_1.text = classsubject1.subjectResponse.credits.toString() + " tín chỉ"
        holder.text_name_1.text = classsubject1.subjectResponse.subjectName
        holder.item_1.setOnClickListener {
            ClassSubjectDetails.login(classsubject1)
            holder.item_1.context.startActivity(Intent(holder.item_1.context, ClassSubjectDetailActivity::class.java))
        }
        if(adjustedPosition + 1 < classSubjectList.size) {
            val classsubject2 = classSubjectList[adjustedPosition + 1]
            holder.credits_2.text = classsubject2.subjectResponse.credits.toString() + " tín chỉ"
            holder.text_name_2.text = classsubject2.subjectResponse.subjectName
            holder.item_2.setOnClickListener {
                ClassSubjectDetails.login(classsubject2)
                holder.item_2.context.startActivity(Intent(holder.item_2.context, ClassSubjectDetailActivity::class.java))
            }
        } else {
            holder.item_2.visibility = View.INVISIBLE
        }
    }
}