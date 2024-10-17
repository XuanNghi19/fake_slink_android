package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.TimeTableResponse
import retrofit2.Call
import retrofit2.http.*

interface TimeTableService {
    // Lấy thời khóa biểu
    @GET("timeTables/{studentIDNum}")
    suspend fun getTimeTable(
        @Header("Authorization") authorizationHeader: String,
        @Path("studentIDNum") studentIDNum: String
    ): ApiResponse<List<TimeTableResponse>>
}