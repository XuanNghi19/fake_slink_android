package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.request.AuthenticationRequest
import com.example.fake_slink.model.request.UpdatePasswordRequest
import com.example.fake_slink.model.request.UpdateStudentRequest
import com.example.fake_slink.model.response.AuthenticationResponse
import com.example.fake_slink.model.response.StudentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface StudentService {
    // xác thực sinh viên
    @POST("students/student_authentication")
    fun studentAuthentication(
        @Body authenticationRequest: AuthenticationRequest
    ): Call<ApiResponse<AuthenticationResponse>>

    // Lấy thông tin chi tiết sinh viên
    @GET("students/student_detail")
    fun getStudentDetail(
        @Header("Authorization") authorizationHeader: String
    ): Call<ApiResponse<StudentResponse>>

    // Cập nhật thông tin sinh viên
    @PUT("students/update_student_detail")
    fun updateStudentDetail(
        @Header("Authorization") authorizationHeader: String,
        @Body request: UpdateStudentRequest
    ): Call<ApiResponse<StudentResponse>>

    // Cập nhật mật khẩu
    @PATCH("students/update_password")
    fun updatePassword(
        @Header("Authorization") authorizationHeader: String,
        @Body request: UpdatePasswordRequest
    ): Call<ApiResponse<Boolean>>
}