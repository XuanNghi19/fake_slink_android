package com.example.fake_slink.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.request.AuthenticationRequest
import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.AuthenticationResponse
import com.example.fake_slink.retrofit.StudentApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_login = findViewById<MaterialButton>(R.id.login);
        btn_login.setOnClickListener{
            val student_id = findViewById<TextInputEditText>(R.id.student_id).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password).text.toString()

            val authenticationRequest = AuthenticationRequest(
                idNum = student_id,
                password = password
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
}