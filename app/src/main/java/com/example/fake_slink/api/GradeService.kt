package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.LearningOutcomesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GradeService {
    @GET("students/{id_num}")
    suspend fun getLearningOutcomes(
        @Header("Authorization") authorizationHeader: String,
        @Path("id_num") idNum: String
    ) : ApiResponse<LearningOutcomesResponse>
}