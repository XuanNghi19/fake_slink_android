package com.example.fake_slink.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemStudentsInClassAdapter
import com.example.fake_slink.adapter.ItemTkbMiniAdapter
import com.example.fake_slink.model.singleton.ClassSubjectDetails
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.model.singleton.TimeTables
import com.example.fake_slink.retrofit.CreditClassApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class StudentsInClassActivity : AppCompatActivity() {
    private val TAG = "StudentsInClass"
    private lateinit var back: CardView
    private lateinit var student_num: TextView
    private lateinit var list_students: ListView
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_in_class)

        back = findViewById(R.id.back)
        student_num = findViewById(R.id.student_num)
        list_students = findViewById(R.id.list_students)
        loading = findViewById(R.id.loading)
        back.setOnClickListener {
            back()
        }
        loadData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        val student = Student.getStudent()
        val idNum = student?.idNum
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val authorizationStr = "Bearer $token"
        if (idNum != null) {
            CoroutineScope(Dispatchers.IO).launch {
                withTimeout(TimeUnit.SECONDS.toMillis(10)) {
                    ClassSubjectDetails.get()?.let {
                        val apiResponse =
                            CreditClassApiService.creditClassService.getStudentInCreditClass(
                                authorizationStr,
                                it.classSubjectID
                            )
                        if (apiResponse.code == 200) {
                            runOnUiThread {
                                apiResponse.result?.let { studentList ->
                                    list_students.adapter = ItemStudentsInClassAdapter(
                                        this@StudentsInClassActivity,
                                        studentList
                                    )
                                    student_num.text = "${studentList.size.toString()} sinh viên"
                                    loading.visibility = View.GONE
                                }
                            } ?: run {
                                val errorMessage = "No response body"
                                Log.e(TAG, errorMessage)
                                Toast.makeText(
                                    this@StudentsInClassActivity,
                                    "Có lỗi xảy ra: $errorMessage !",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Log.e(TAG, apiResponse.code.toString())
                            Toast.makeText(
                                this@StudentsInClassActivity,
                                "Có lỗi xảy ra, ma loi: ${apiResponse.code}!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            Log.e(TAG, "idNum is null!")
        }
    }

    private fun back() {
        finish()
    }
}