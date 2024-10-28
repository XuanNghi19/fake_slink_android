package com.example.fake_slink.model.response

data class CreditClassResponse(
    var semesterResponseList: List<SemesterResponse>,
    var classSubjectResponseList: List<ClassSubjectResponse>
)
