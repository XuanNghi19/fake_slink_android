package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.GradeResponse

object GradeDetail {
    private var grade: GradeResponse? = null

    fun login(grade: GradeResponse) {
        this.grade = grade
    }

    fun logout() {
        grade = null
    }

    fun get(): GradeResponse? {
        return grade
    }
}