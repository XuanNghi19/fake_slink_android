package com.example.fake_slink.model.request

data class UpdatePasswordRequest(
    var oldPassword: String,
    var newPassword: String
)
