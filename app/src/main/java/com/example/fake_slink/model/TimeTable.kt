package com.example.fake_slink.model

data class TimeTable(
    var id: Int,

    var classSubject: ClassSubject,
    
    var dayOfWeek: Int,
    
    var startTime: String,
    
    var endTime: String,
)
