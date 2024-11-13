package com.example.fake_slink.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.request.AuthenticationWithMobilePhoneRequest
import com.example.fake_slink.model.request.UpdateStudentDeviceRequest
import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.response.AuthenticationResponse
import com.example.fake_slink.model.response.StudentResponse
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.retrofit.StudentApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private val TAG = "LOGIN"
    private lateinit var loadding: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
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
        loadding = findViewById<ProgressBar>(R.id.loading)
        btn_login.setOnClickListener {
            val student_id = findViewById<TextInputEditText>(R.id.student_id).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password).text.toString()

            if (student_id.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Vui lòng nhập đầy đủ thông tin!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            loadding.visibility = View.VISIBLE

            // getToken
            authentication(student_id, password)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun authentication(student_id: String, password: String) {

        var fcmToken: String = "";
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
                val updateStudentDeviceRequest = UpdateStudentDeviceRequest(
                    fcmToken,
                    OffsetDateTime.now(ZoneOffset.UTC).toString()
                )

                val authenticationRequest = AuthenticationWithMobilePhoneRequest(
                    student_id,
                    password,
                    updateStudentDeviceRequest
                )

                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                StudentApiService.studentService.studentAuthenticationWithMobilePhone(
                    authenticationRequest
                ).enqueue(object : retrofit2.Callback<ApiResponse<AuthenticationResponse>> {
                    override fun onResponse(
                        call: Call<ApiResponse<AuthenticationResponse>>,
                        response: Response<ApiResponse<AuthenticationResponse>>
                    ) {

                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            apiResponse?.let {
                                val authenticationResponse = it.result
                                val token = authenticationResponse.token
                                Log.d(TAG, token)
                                val editor = sharedPreferences.edit()
                                editor.putString("token", token)
                                editor.apply()

                                // getStudent detail
                                getStudentDetail()
                            }
                        } else {
                            loadding.visibility = View.GONE
                            val errorMessage = response.message()
                            Log.e(TAG, "onResponse: ${errorMessage.toString()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<AuthenticationResponse>>,
                        t: Throwable
                    ) {
                        loadding.visibility = View.GONE
                        val errorMessage = t.message
                        Log.e(TAG, "onFailure: ${errorMessage.toString()}")
                        Toast.makeText(
                            this@LoginActivity,
                            "Có lỗi xảy ra: $errorMessage !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStudentDetail() {
        val TAG = "LOGIN"
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            val authorizationHeader = "Bearer " + token
            try {
                StudentApiService.studentService.getStudentDetail(
                    authorizationHeader
                ).enqueue(object : retrofit2.Callback<ApiResponse<StudentResponse>> {
                    override fun onResponse(
                        call: Call<ApiResponse<StudentResponse>>,
                        response: Response<ApiResponse<StudentResponse>>
                    ) {
                        loadding.visibility = View.GONE
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            apiResponse?.let {
                                val studentResponse = it.result
                                Log.d(TAG, "studentResponse: $studentResponse")

                                Student.loginStudent(studentResponse)

                                val homeIntent =
                                    Intent(this@LoginActivity, HomeActivity::class.java)
                                startActivity(homeIntent)
                                finish()
                            }
                        } else {
                            loadding.visibility = View.GONE
                            val errorMessage = response.message()
                            Log.e(TAG, "getStudentDetail_else: $errorMessage")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<StudentResponse>>, t: Throwable) {
                        loadding.visibility = View.GONE
                        val errorMessage = t.message
                        Log.e(TAG, "getStudentDetail_onFailure: ${errorMessage.toString()}")
                        Toast.makeText(
                            this@LoginActivity,
                            "Có lỗi xảy ra: $errorMessage !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } catch (ex: Exception) {
                Log.e(TAG, "getStudentDetail_catch: $ex")
            }
        }
    }
}