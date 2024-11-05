package com.example.fake_slink.retrofit

import com.example.fake_slink.api.ReviewFormService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReviewFormApiService {
    private const val BASE_URL = "https://fake-slink.onrender.com/api/v1/"

    val reviewFormService: ReviewFormService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ReviewFormService::class.java)
    }
}