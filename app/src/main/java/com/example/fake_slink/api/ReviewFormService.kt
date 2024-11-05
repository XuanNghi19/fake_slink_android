package com.example.fake_slink.api

import com.example.fake_slink.model.request.CreateReviewFormRequest
import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.ReviewFormResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewFormService {
    @POST("reviewForm")
    suspend fun createReviewForm(
        @Header("Authorization") authorizationHeader: String,
        @Body createReviewFormRequest: CreateReviewFormRequest
    ) : ApiResponse<Boolean>

    @GET("reviewForm/by_student/{student_id_num}")
    suspend fun getAllReviewFormByStudent(
        @Header("Authorization") authorizationHeader: String,
        @Path("student_id_num") studentIdNum: String
    ) : ApiResponse<List<ReviewFormResponse>>
}