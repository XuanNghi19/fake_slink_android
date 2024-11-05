package com.example.fake_slink.model.response

data class LearningOutcomesResponse (
    var semesterResponseList: List<SemesterResponse>? = null,
    var gradeResponseList: List<GradeResponse>? = null
)