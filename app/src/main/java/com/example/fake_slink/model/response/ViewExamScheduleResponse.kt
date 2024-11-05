package com.example.fake_slink.model.response

data class ViewExamScheduleResponse(
    val semesterResponseList: List<SemesterResponse>,

    val examScheduleResponseList: List<ExamScheduleResponse>
)
