package com.example.fake_slink.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemNotificationAdapter
import com.example.fake_slink.helpers.DatabaseHelper
import com.example.fake_slink.repository.NotificationRepository

class NotificationActivity : AppCompatActivity() {

    private val TAG = "NOTIFICATION"
    private val dbHelper = DatabaseHelper(this@NotificationActivity)
    private val notificationRepository = NotificationRepository(dbHelper)
    private lateinit var back: CardView
    private lateinit var checkAll: ImageButton
    private lateinit var list_notification: ListView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)

        back = findViewById(R.id.back)
        checkAll = findViewById(R.id.checkAll)
        list_notification = findViewById(R.id.list_notification)

        back.setOnClickListener {
            back()
        }
        checkAll.setOnClickListener {
            checkAll()
        }
        loadData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        val notificationList = notificationRepository.getAllNotifications()
        list_notification.adapter = ItemNotificationAdapter(this, notificationList)

    }

    private fun back() {
        val intent = Intent(this@NotificationActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAll() {
        notificationRepository.checkAlRead()
        val notificationList = notificationRepository.getAllNotifications()
        list_notification.adapter = ItemNotificationAdapter(this, notificationList)
    }
}