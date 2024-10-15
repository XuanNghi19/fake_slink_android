package com.example.fake_slink.model.response

data class TimeTableResponse(
    var id: Int,

    var classSubjectResponse: ClassSubjectResponse,

    var dayOfWeek: Int,

    var startTime: String,

    var endTime: String,
)
