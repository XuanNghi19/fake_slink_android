package com.example.fake_slink.model.response

import java.util.Date

data class TeacherResponse(
    var idNum: String,

    var name: String,

    var dob: Date,

    var email: String,

    var cccd: String,

    var phone1: String,

    var phone2: String,

    var sex: String,

    var address: String,

    var department: String
)
