package com.example.fake_slink.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemCreditClassAdapter
import com.example.fake_slink.adapter.ItemGradeAdapter
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.singleton.CreditClass
import com.example.fake_slink.model.singleton.LearningOutcomes
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.retrofit.CreditClassApiService
import com.example.fake_slink.retrofit.GradeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.Date
import java.util.concurrent.TimeUnit

class LearningOutcomesActivity : AppCompatActivity() {
    private val TAG = "LearningOutcomes"
    private lateinit var back: CardView
    private lateinit var gpa: TextView
    private lateinit var dropdown_ky_hoc: AutoCompleteTextView
    private lateinit var recycler_kqht: RecyclerView
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_learning_outcomes)

        back = findViewById(R.id.back)
        gpa = findViewById(R.id.gpa)
        loading = findViewById(R.id.loading)
        dropdown_ky_hoc = findViewById(R.id.dropdown_ky_hoc)
        recycler_kqht = findViewById(R.id.recycler_kqht)
        recycler_kqht.layoutManager = LinearLayoutManager(this)
        back.setOnClickListener {
            back()
        }
        loading.visibility = View.VISIBLE
        recycler_kqht.visibility = View.GONE
        loadData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData() {
        if (LearningOutcomes.get() == null) {
            val student = Student.getStudent()
            val idNum = student?.idNum
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val authorizationStr = "Bearer $token"
            if (idNum != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        withTimeout(TimeUnit.SECONDS.toMillis(20)) {
                            val apiResponse =
                                GradeApiService.gradeService.getLearningOutcomes(
                                    authorizationStr,
                                    idNum
                                )
                            if (apiResponse.code == 200) {
                                Log.d(TAG, apiResponse.result.toString())
                                runOnUiThread {
                                    LearningOutcomes.login(apiResponse.result)
                                    setData()
                                } ?: run {
                                    val errorMessage = "No response body"
                                    Log.e(TAG, errorMessage)
                                    Toast.makeText(
                                        this@LearningOutcomesActivity,
                                        "Có lỗi xảy ra: $errorMessage !",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }
                        }
                    } catch (e: Exception) {
                        val errorMessage = e.message
                        Log.e(TAG, errorMessage.toString())
                        runOnUiThread {
                            Toast.makeText(
                                this@LearningOutcomesActivity,
                                "Có lỗi xảy ra: $errorMessage !",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        } else {
            setData()
        }
    }

    private fun setData() {
        gpa.text = Student.getStudent()?.gpa.toString()
        val learningOutcomes = LearningOutcomes.get()

        // select item semester with present time
        val now = Date()
        var currentSemesterIdx = learningOutcomes?.semesterResponseList?.indexOfFirst { semester ->
            now == semester.startDate || now == semester.endDate ||
                    (now.after(semester.startDate) && now.before(semester.endDate))
        } ?: 0
        val semesterNames =
            learningOutcomes?.semesterResponseList?.map { it.semesterName } ?: emptyList()
        runOnUiThread {
            dropdown_ky_hoc.setAdapter(
                ArrayAdapter(this@LearningOutcomesActivity, R.layout.list_item_week, semesterNames)
            )
        }
        val gradeResponseList = learningOutcomes?.gradeResponseList ?: emptyList()
        val semesterResponseList = learningOutcomes?.semesterResponseList ?: emptyList()
        dropdown_ky_hoc.setOnItemClickListener { parent, view, position, id ->
            currentSemesterIdx = position
            val gradeList = mutableListOf<GradeResponse>()
            for (it in gradeResponseList) {
                if (it.classSubjectResponse?.semesterResponse == semesterResponseList[position]) {
                    gradeList.add(it)
                }
            }
            runOnUiThread {
                recycler_kqht.adapter = ItemGradeAdapter(gradeList)
            }
        }

        dropdown_ky_hoc.setOnClickListener {
            dropdown_ky_hoc.post {
                dropdown_ky_hoc.showDropDown()

                // cuon den vi tri currentSemesterIdx
                val adapterDropdownMenuSemester = dropdown_ky_hoc.adapter as ArrayAdapter<*>
                val viewGroup = dropdown_ky_hoc.parent as ViewGroup
                adapterDropdownMenuSemester.getView(currentSemesterIdx, null, viewGroup).let {
                    dropdown_ky_hoc.listSelection = currentSemesterIdx
                }
            }
        }

        if (currentSemesterIdx != -1) {
            dropdown_ky_hoc.setText(semesterNames[currentSemesterIdx], false)
            val gradeList = mutableListOf<GradeResponse>()
            for (it in gradeResponseList) {
                if (it.classSubjectResponse?.semesterResponse == semesterResponseList[currentSemesterIdx]) {
                    gradeList.add(it)
                }
            }
            runOnUiThread {
                recycler_kqht.adapter = ItemGradeAdapter(gradeList)
            }
        } else {
            Log.d(TAG, "Khong co ky hoc nao cho ngay hom nay")
        }

        loading.visibility = View.GONE
        recycler_kqht.visibility = View.VISIBLE
    }

    private fun back() {
        finish()
    }
}