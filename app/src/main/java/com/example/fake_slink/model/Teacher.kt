package com.example.fake_slink.model

import java.util.Date

data class Teacher(
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