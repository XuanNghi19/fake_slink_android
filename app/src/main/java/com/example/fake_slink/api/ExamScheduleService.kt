package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.ViewExamScheduleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ExamScheduleService {
    @GET("examSchedule/{student_id_num}")
    suspend fun getAllExamScheduleByStudent(
        @Header("Authorization") authorizationHeader: String,
        @Path("student_id_num") studentIdNum: String
    ) : ApiResponse<ViewExamScheduleResponse>
}