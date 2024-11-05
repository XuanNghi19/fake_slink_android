package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ReviewFormResponse

object ReviewFormList {
    private var reviewFormList: List<ReviewFormResponse>? = null

    fun login(reviewFormList: List<ReviewFormResponse>?) {
        this.reviewFormList = reviewFormList
    }

    fun logout() {
        this.reviewFormList = null
    }

    fun get(): List<ReviewFormResponse>? {
        return this.reviewFormList
    }
}