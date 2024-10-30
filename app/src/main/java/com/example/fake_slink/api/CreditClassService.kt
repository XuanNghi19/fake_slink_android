package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.CreditClassResponse
import com.example.fake_slink.model.response.StudentResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface CreditClassService {
    // Lay tat ca lop hoc va nam hoc cua student
    @GET("classSubject/credit_class/{student_id_num}")
    suspend fun getCreditClass(
        @Header("Authorization") authorizationHeader: String,
        @Path("student_id_num") studentIdNum: String?
    ): ApiResponse<CreditClassResponse>

    @GET("classSubject/get_students_in_credit_class/{classSubjectID}")
    suspend fun getStudentInCreditClass(
        @Header("Authorization") authorizationHeader: String,
        @Path("classSubjectID") classSubjectID: Int
    ): ApiResponse<List<StudentResponse>>
}