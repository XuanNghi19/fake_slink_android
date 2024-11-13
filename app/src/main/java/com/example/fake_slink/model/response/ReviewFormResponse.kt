package com.example.fake_slink.model.response

import com.example.fake_slink.enums.ReviewFormStatus

data class ReviewFormResponse(

    val classSubjectId: Int,

    val subjectName: String,

    val credits: String,

    val status: ReviewFormStatus
)
