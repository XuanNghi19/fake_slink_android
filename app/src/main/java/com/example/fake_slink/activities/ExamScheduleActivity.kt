package com.example.fake_slink.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemExamScheduleAdapter
import com.example.fake_slink.adapter.ItemGradeAdapter
import com.example.fake_slink.model.response.ExamScheduleResponse
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.singleton.LearningOutcomes
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.model.singleton.ViewExamSchedule
import com.example.fake_slink.retrofit.ExamScheduleApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.Date
import java.util.concurrent.TimeUnit

class ExamScheduleActivity : AppCompatActivity() {

    private val TAG = "EXAM_SCHEDULE"
    private lateinit var back: CardView
    private lateinit var dropdownKyHoc: AutoCompleteTextView
    private lateinit var examScheduleRecycler: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_schedule)

        back = findViewById(R.id.back)
        dropdownKyHoc = findViewById(R.id.dropdown_ky_hoc)
        examScheduleRecycler = findViewById(R.id.exam_schedule_recycler)
        loading = findViewById(R.id.loading)
        tableLayout = findViewById(R.id.table_layout)

        examScheduleRecycler.layoutManager = LinearLayoutManager(this)
        loading.visibility = View.VISIBLE
        examScheduleRecycler.visibility = View.GONE

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

    private fun loadData() {
        if (ViewExamSchedule.get() == null) {
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
                                ExamScheduleApiService.examScheduleService.getAllExamScheduleByStudent(
                                    authorizationStr,
                                    idNum
                                )
                            if (apiResponse.code == 200) {
                                Log.d(TAG, apiResponse.result.toString())
                                ViewExamSchedule.login(apiResponse.result)

                                withContext(Dispatchers.Main) {
                                    setData()
                                }

                            } else {
                                Log.e(TAG, "Có lỗi xảy ra: ${apiResponse.code}")
                                runOnUiThread {
                                    Toast.makeText(
                                        this@ExamScheduleActivity,
                                        "Có lỗi xảy ra: ${apiResponse.code}!",
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
                                this@ExamScheduleActivity,
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
        val viewExamSchedule = ViewExamSchedule.get()
        val semesterResponseList = viewExamSchedule?.semesterResponseList ?: emptyList()
        val examScheduleResponseList = viewExamSchedule?.examScheduleResponseList ?: emptyList()

        // select item semester with present time
        val now = Date()
        var currentSemesterIdx = semesterResponseList?.indexOfFirst { semester ->
            now == semester.startDate || now == semester.endDate ||
                    (now.after(semester.startDate) && now.before(semester.endDate))
        } ?: 0
        val semesterNames =
            semesterResponseList?.map { it.semesterName } ?: emptyList()
        runOnUiThread {
            dropdownKyHoc.setAdapter(
                ArrayAdapter(this@ExamScheduleActivity, R.layout.list_item_week, semesterNames)
            )
        }

        dropdownKyHoc.setOnItemClickListener { parent, view, position, id ->
            currentSemesterIdx = position
            val examScheduleList = mutableListOf<ExamScheduleResponse>()
            for (it in examScheduleResponseList) {
                if (it.classSubjectResponse?.semesterResponse == semesterResponseList[position]) {
                    examScheduleList.add(it)
                }
            }
            runOnUiThread {
                examScheduleRecycler.adapter = ItemExamScheduleAdapter(examScheduleList)
            }
        }

        dropdownKyHoc.setOnClickListener {
            dropdownKyHoc.post {
                dropdownKyHoc.showDropDown()

                // cuon den vi tri currentSemesterIdx
                val adapterDropdownMenuSemester = dropdownKyHoc.adapter as ArrayAdapter<*>
                val viewGroup = dropdownKyHoc.parent as ViewGroup
                adapterDropdownMenuSemester.getView(currentSemesterIdx, null, viewGroup)?.let {
                    dropdownKyHoc.listSelection = currentSemesterIdx
                }
            }
        }

        if (currentSemesterIdx != -1) {
            dropdownKyHoc.setText(semesterNames[currentSemesterIdx], false)
            val examScheduleList = mutableListOf<ExamScheduleResponse>()
            for (it in examScheduleResponseList) {
                if (it.classSubjectResponse?.semesterResponse == semesterResponseList[currentSemesterIdx]) {
                    examScheduleList.add(it)
                }
            }
            runOnUiThread {
                examScheduleRecycler.adapter = ItemExamScheduleAdapter(examScheduleList)
            }
        } else {
            Log.d(TAG, "Khong co ky hoc nao cho ngay hom nay")
        }

        loading.visibility = View.GONE
        examScheduleRecycler.visibility = View.VISIBLE
    }

    private fun back() {
        finish()
    }
}