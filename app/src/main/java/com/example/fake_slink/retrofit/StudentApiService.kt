package com.example.fake_slink.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fake_slink.api.StudentService
import com.example.fake_slink.model.OffsetDateTimeDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
object StudentApiService {
    private const val BASE_URL = "https://fake-slink.onrender.com/api/v1/"

    val studentService: StudentService by lazy {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeDeserializer()) // Đăng ký kiểu OffsetDateTime
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))  // Sử dụng Gson đã cấu hình
            .build()

        retrofit.create(StudentService::class.java)
    }
}