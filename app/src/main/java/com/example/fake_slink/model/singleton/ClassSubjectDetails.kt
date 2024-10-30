package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.CreditClassResponse

object ClassSubjectDetails {
    private var classSubjectResponse: ClassSubjectResponse? = null

    fun login(classSubjectResponse: ClassSubjectResponse) {
        this.classSubjectResponse = classSubjectResponse
    }

    fun logout() {
        classSubjectResponse = null
    }

    fun get(): ClassSubjectResponse? {
        return classSubjectResponse
    }
}