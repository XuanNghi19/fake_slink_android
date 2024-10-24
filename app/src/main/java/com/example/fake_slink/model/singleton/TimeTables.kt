package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.TimeTableResponse

object TimeTables {
    private var timeTables: List<TimeTableResponse>? = null

    fun loginTimeTables(timeTables: List<TimeTableResponse>) {
        this.timeTables = timeTables
    }

    fun logoutTimeTables() {
        timeTables = null
    }

    fun getTimeTables(): List<TimeTableResponse>? {
        return timeTables
    }
}