package com.example.fake_slink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.enums.ReviewFormStatus
import com.example.fake_slink.model.response.ReviewFormResponse

class ItemReviewFormAdapter(private val reviewFormList: List<ReviewFormResponse>)
    :  RecyclerView.Adapter<ItemReviewFormAdapter.ItemReviewFormHolder>(){
    inner class ItemReviewFormHolder(view: View): RecyclerView.ViewHolder(view) {
        val col1: TextView = view.findViewById(R.id.col1)
        val col2: TextView = view.findViewById(R.id.col2)
        val col3: TextView = view.findViewById(R.id.col3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemReviewFormHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_list, parent, false)

        return ItemReviewFormHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewFormList.size
    }

    override fun onBindViewHolder(holder: ItemReviewFormHolder, position: Int) {
        holder.col1.text = reviewFormList[position].subjectName
        holder.col2.text = reviewFormList[position].credits
        holder.col3.apply {
            text = when(reviewFormList[position].status) {
                ReviewFormStatus.SUBMITTED -> {
                    holder.col3.setBackgroundResource(R.color.SUBMITTED)
                    "Đã gửi"
                }
                ReviewFormStatus.IN_PROGRESS -> {
                    holder.col3.setBackgroundResource(R.color.IN_PROGRESS)
                    "Đang xử lý"
                }
                ReviewFormStatus.REVIEWED -> {
                    holder.col3.setBackgroundResource(R.color.REVIEWED)
                    "Đã xem xét"
                }
                ReviewFormStatus.APPROVED -> {
                    holder.col3.setBackgroundResource(R.color.APPROVED)
                    "Chấp nhận"
                }
                ReviewFormStatus.REJECTED -> {
                    holder.col3.setBackgroundResource(R.color.REJECTED)
                    "Từ chối"
                }

                else -> ""
            }
        }
    }
}