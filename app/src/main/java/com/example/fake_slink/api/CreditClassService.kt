package com.example.fake_slink.api

import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.CreditClassResponse
import com.example.fake_slink.model.response.StudentResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface CreditClassService {
    // Lay tat ca lop hoc va nam hoc cua student
    @GET("/credit_class/{student_id_num}")
    suspend fun getCreditClass(
        @Path("student_id_num") studentIdNum: String?
    ): ApiResponse<CreditClassResponse>

    @GET("/get_students_in_credit_class/{classSubjectID}")
    suspend fun getCreditClass(
        @Path("classSubjectID") classSubjectID: Int
    ): ApiResponse<List<StudentResponse>>
}