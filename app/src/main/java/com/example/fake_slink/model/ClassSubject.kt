package com.example.fake_slink.model

import androidx.core.view.ContentInfoCompat.Flags
import java.util.Date

data class ClassSubject(
    var classSubjectID: Int,

    var semester: Semester,

    var teacher: Teacher,

    var subject: Subject,

    var location: String,

    var group: Int,

    var className: String,

    var registrationDate: Date,

    var percentDiemCC: Float,

    var percentDiemBT: Float,

    var percentDiemTH: Float,

    var percentDiemKT: Float,

    var percentDiemCK: Float
)
