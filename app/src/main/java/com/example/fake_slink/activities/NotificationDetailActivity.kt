package com.example.fake_slink.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.singleton.NotificationDetail

class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var back: CardView
    private lateinit var title: TextView
    private lateinit var timeDate: TextView
    private lateinit var content: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_detail)

        back = findViewById(R.id.back)
        title = findViewById(R.id.title)
        timeDate = findViewById(R.id.timeDate)
        content = findViewById(R.id.content)

        back.setOnClickListener {
            back()
        }
        loadData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        NotificationDetail.get()?.let {
            title.text = it.title
            timeDate.text = "${AppHelper.formatDateToTime(it.createAt)} ${AppHelper.formatDateToDay(it.createAt)}"
            content.text = it.content
        }
    }

    private fun back() {
        val intent = Intent(this@NotificationDetailActivity, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        finish()
    }
}