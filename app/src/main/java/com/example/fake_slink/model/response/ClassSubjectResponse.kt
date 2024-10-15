package com.example.fake_slink.model.response

import java.util.Date

data class ClassSubjectResponse(
    var classSubjectID: Int,

    var semesterResponse: SemesterResponse,

    var teacherResponse: TeacherResponse,

    var subjectResponse: SubjectResponse,

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
