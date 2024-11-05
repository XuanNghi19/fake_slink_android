package com.example.fake_slink.model.response

import java.util.Date

data class GradeResponse (
    val classSubjectResponse: ClassSubjectResponse,

    val diemCC: Float,

    val diemBT: Float,

    val diemTH: Float,

    val diemKT: Float,

    val diemCK: Float,

    val diemTK: Float,

    val status: String,

    val appealsDateline: Date
)