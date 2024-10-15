package com.example.fake_slink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.fake_slink.model.request.AuthenticationRequest
import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.AuthenticationResponse
import com.example.fake_slink.retrofit.StudentApiService
import retrofit2.Call
import retrofit2.Response
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val authenticationRequest = AuthenticationRequest(
            idNum = "D21CNDPT001",
            password = "123456"
        )
        val TAG = "MainActivity"
        StudentApiService.studentService.studentAuthentication(
            authenticationRequest
        ).enqueue(object: retrofit2.Callback<ApiResponse<AuthenticationResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<AuthenticationResponse>>,
                response: Response<ApiResponse<AuthenticationResponse>>
            ) {
                if(response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        val authenticationResponse = it.result
                        val token = authenticationResponse.token
                        Log.d(TAG,token)
                    }
                } else {
                    val errorMessage= response.message()
                    Log.e(TAG, errorMessage)
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<AuthenticationResponse>>,
                t: Throwable
            ) {
                val errorMessage = t.message
                Log.e(TAG, errorMessage.toString())
            }
        })

    }
}
