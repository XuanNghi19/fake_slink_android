package com.example.fake_slink.notification

import java.time.LocalDateTime
import java.util.Date

data class Notification (
    val notificationID: Long?,
    val title: String,
    val createAt: Date,
    val content: String,
    val lastReadDate: LocalDateTime?,
    val status: Boolean
)