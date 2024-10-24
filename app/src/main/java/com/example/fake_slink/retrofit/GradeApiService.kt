package com.example.fake_slink.retrofit

import com.example.fake_slink.api.GradeService
import com.example.fake_slink.api.StudentService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GradeApiService {
    private const val BASE_URL = "https://fake-slink.onrender.com/api/v1/"

    val gradeService: GradeService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GradeService::class.java)
    }
}