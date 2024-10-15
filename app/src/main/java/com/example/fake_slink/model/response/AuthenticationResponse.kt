package com.example.fake_slink.model.response

data class AuthenticationResponse(
    var token: String,

    var authenticated: Boolean
)
