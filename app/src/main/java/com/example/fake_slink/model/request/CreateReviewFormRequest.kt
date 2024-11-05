package com.example.fake_slink.model.request

data class CreateReviewFormRequest(
    val studentIdNum: String,

    val classSubjectID: Int,

    val content: String,

    val attachedFile: String
)
