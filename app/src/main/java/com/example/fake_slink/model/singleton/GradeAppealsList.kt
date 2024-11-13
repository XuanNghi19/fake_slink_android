package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.GradeResponse

object GradeAppealsList {
    private var gradeResponseList: MutableList<GradeResponse>? = null

    fun login(gradeResponseList: List<GradeResponse>?) {
        this.gradeResponseList = gradeResponseList?.toMutableList()
    }

    fun logout() {
        this.gradeResponseList = null
    }

    fun get() : List<GradeResponse>? {
        return this.gradeResponseList
    }

    fun add(gradeResponse: GradeResponse) {
        if(this.gradeResponseList != null) {
            this.gradeResponseList?.add(gradeResponse)
        }
    }
}