package com.example.fake_slink.model.singleton

import com.example.fake_slink.model.response.CreditClassResponse
import com.example.fake_slink.model.response.StudentResponse

object CreditClass {
    private var creditClassResponse: CreditClassResponse? = null

    fun loginCreditClassResponse(creditClassResponse: CreditClassResponse) {
        this.creditClassResponse = creditClassResponse
    }

    fun logoutCreditClassResponse() {
        creditClassResponse = null
    }

    fun getCreditClassResponse(): CreditClassResponse? {
        return creditClassResponse
    }
}