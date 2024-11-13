package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ReviewFormResponse

object ReviewFormList {
    private var reviewFormList: MutableList<ReviewFormResponse>? = null

    fun login(reviewFormList: List<ReviewFormResponse>?) {
        this.reviewFormList = reviewFormList?.toMutableList()
    }

    fun logout() {
        this.reviewFormList = null
    }

    fun get(): List<ReviewFormResponse>? {
        return this.reviewFormList
    }

    fun update(reviewFormResponse: ReviewFormResponse) {
        if (this.reviewFormList != null) {
            val tmpReviewFormList = this.reviewFormList ?: mutableListOf()

            val idx = this.reviewFormList?.indexOfFirst {
                it.classSubjectId == reviewFormResponse.classSubjectId
            } ?: 0

            if(idx != -1) {
                tmpReviewFormList[idx] = reviewFormResponse
            }

            this.reviewFormList = tmpReviewFormList
        }
    }
}