package com.example.fake_slink.model.request

import java.time.LocalDateTime
import java.time.OffsetDateTime

data class UpdateStudentDeviceRequest(
    val fcmToken: String,

    val lastUpdated: String
)
