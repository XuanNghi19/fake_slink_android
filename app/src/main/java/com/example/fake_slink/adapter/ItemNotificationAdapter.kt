package com.example.fake_slink.adapter

import com.example.fake_slink.R
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.fake_slink.activities.NotificationDetailActivity
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.model.singleton.NotificationDetail
import com.example.fake_slink.notification.Notification
import com.example.fake_slink.repository.NotificationRepository
import java.time.LocalDateTime

class ItemNotificationAdapter(
    private val context: Activity,
    private val notificationList: List<Notification>
) : ArrayAdapter<Notification>(
    context,
    R.layout.item_list_notification,
    notificationList
) {

    private val db = DatabaseHelper(context)
    private val notificationRepository = NotificationRepository(db)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = context.layoutInflater.inflate(R.layout.item_list_notification, null, true)

        val title = view.findViewById<TextView>(R.id.title_notification)
        title.text = notificationList[position].title

        val content = view.findViewById<TextView>(R.id.content_notification)
        content.text = notificationList[position].content

        val time = view.findViewById<TextView>(R.id.time_notification)
        time.text = "Ngày ${AppHelper.formatDateToDay(notificationList[position].createAt)}"

        val notRead = view.findViewById<ImageView>(R.id.label_not_read)
        if(!notificationList[position].status) {
            notRead.visibility = View.VISIBLE
        } else {
            notRead.visibility = View.INVISIBLE
        }

        val notification = view.findViewById<CardView>(R.id.notification)
        notification.setOnClickListener {
            NotificationDetail.login(notificationList[position])
            context.startActivity(Intent(context, NotificationDetailActivity::class.java))
            notificationList[position].notificationID?.let {
                notificationRepository.updateLastReadDate(it, LocalDateTime.now())
            }
        }
        return view
    }
}