package com.example.fake_slink.model.response

data class ApiResponse<T>(
    var code: Int,

    var message: String,

    var result: T
)
