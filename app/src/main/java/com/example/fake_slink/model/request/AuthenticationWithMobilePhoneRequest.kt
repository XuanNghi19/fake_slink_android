package com.example.fake_slink.model.request

data class AuthenticationWithMobilePhoneRequest (
    val idNum : String,

    val password: String,

    val updateStudentDeviceRequest: UpdateStudentDeviceRequest
)
