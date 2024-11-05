package com.example.fake_slink.model.response

import java.util.Date

data class ExamScheduleResponse(
    val classSubjectResponse: ClassSubjectResponse,

    val dateOfEvent : Date,

    val location : String,

    val numberOfStudent: Int,

    val startTime: String,

    val minute: String,

    val format: String
)
