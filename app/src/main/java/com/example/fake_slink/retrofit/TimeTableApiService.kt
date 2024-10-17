package com.example.fake_slink.retrofit

import com.example.fake_slink.api.TimeTableService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TimeTableApiService {
    private const val BASE_URL = "https://fake-slink.onrender.com/api/v1/"

    val timeTableService: TimeTableService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(TimeTableService::class.java)
    }
}