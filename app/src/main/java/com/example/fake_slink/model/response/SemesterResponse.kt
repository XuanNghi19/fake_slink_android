package com.example.fake_slink.model.response

import java.util.Date

data class SemesterResponse(
    var semesterID: Int,

    var semesterName: String,

    var startDate: Date,

    var endDate: Date
)
