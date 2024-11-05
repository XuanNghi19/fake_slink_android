package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.GradeResponse

object GradeAppealsList {
    private var gradeResponseList: List<GradeResponse>? = null

    fun login(gradeResponseList: List<GradeResponse>?) {
        this.gradeResponseList = gradeResponseList
    }

    fun logout() {
        this.gradeResponseList = null
    }

    fun get() : List<GradeResponse>? {
        return this.gradeResponseList
    }
}