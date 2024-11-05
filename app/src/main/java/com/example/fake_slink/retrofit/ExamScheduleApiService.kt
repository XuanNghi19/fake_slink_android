package com.example.fake_slink.retrofit

import com.example.fake_slink.api.CreditClassService
import com.example.fake_slink.api.ExamScheduleService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExamScheduleApiService {
    private const val BASE_URL = "https://fake-slink.onrender.com/api/v1/"

    val examScheduleService: ExamScheduleService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ExamScheduleService::class.java)
    }
}