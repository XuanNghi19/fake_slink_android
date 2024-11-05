package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ViewExamScheduleResponse

object ViewExamSchedule {
    private var viewExamSchedule: ViewExamScheduleResponse? = null

    fun login(viewExamScheduleResponse: ViewExamScheduleResponse) {
        viewExamSchedule = viewExamScheduleResponse
    }

    fun logout() {
        viewExamSchedule = null
    }

    fun get(): ViewExamScheduleResponse? {
        return viewExamSchedule
    }
}