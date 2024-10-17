package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.StudentResponse

object Student {
    private var student: StudentResponse? = null

    fun loginStudent(student: StudentResponse) {
        this.student = student
    }

    fun logoutStudent() {
        student = null
    }

    fun getStudent(): StudentResponse? {
        return student
    }
}