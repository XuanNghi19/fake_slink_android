package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ExamScheduleResponse
import com.example.fake_slink.model.response.ViewExamScheduleResponse

object ExamScheduleDetail {
    private var examScheduleResponse: ExamScheduleResponse? = null

    fun login(examScheduleResponse: ExamScheduleResponse) {
        this.examScheduleResponse = examScheduleResponse
    }

    fun logout() {
        this.examScheduleResponse = null
    }

    fun get(): ExamScheduleResponse? {
        return this.examScheduleResponse
    }
}