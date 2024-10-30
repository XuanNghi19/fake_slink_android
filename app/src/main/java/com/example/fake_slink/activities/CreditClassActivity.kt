package com.example.fake_slink.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
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
import com.example.fake_slink.adapter.ItemTimeTableAdapter
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.singleton.CreditClass
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.model.singleton.TimeTables
import com.example.fake_slink.retrofit.CreditClassApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.Date
import java.util.concurrent.TimeUnit

class CreditClassActivity : AppCompatActivity() {
    private lateinit var back: CardView
    private lateinit var dropdown_ky_hoc: AutoCompleteTextView
    private lateinit var recycler_subject: RecyclerView
    private lateinit var subject_loading: ProgressBar
    private val TAG = "CREDIT_CLASS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_credit_class)

        back = findViewById(R.id.back)
        dropdown_ky_hoc = findViewById(R.id.dropdown_ky_hoc)
        recycler_subject = findViewById(R.id.recycler_subject)
        recycler_subject.layoutManager = LinearLayoutManager(this)
        subject_loading = findViewById(R.id.subject_loading)

        recycler_subject.visibility = View.GONE
        subject_loading.visibility = View.VISIBLE



        back.setOnClickListener {
            onBackClick()
        }
        loadData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData() {
        if (CreditClass.getCreditClassResponse() == null) {
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
                                CreditClassApiService.creditClassService.getCreditClass(
                                    authorizationStr,
                                    idNum
                                )
                            if (apiResponse.code == 200) {
                                Log.d(TAG, apiResponse.result.toString())
                                runOnUiThread {
                                    CreditClass.loginCreditClassResponse(apiResponse.result)
                                    setData()
                                } ?: run {
                                    val errorMessage = "No response body"
                                    Log.e(TAG, errorMessage)
                                    Toast.makeText(
                                        this@CreditClassActivity,
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
                                this@CreditClassActivity,
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
        val creditClass = CreditClass.getCreditClassResponse()
        val now = Date()
        var currentSemesterIdx = creditClass?.semesterResponseList?.indexOfFirst { semester ->
            now == semester.startDate || now == semester.endDate ||
                    (now.after(semester.startDate) && now.before(semester.endDate))
        } ?: 0
        val semesterNames =
            creditClass?.semesterResponseList?.map { it.semesterName } ?: emptyList()
        runOnUiThread {
            dropdown_ky_hoc.setAdapter(
                ArrayAdapter(this@CreditClassActivity, R.layout.list_item_week, semesterNames)
            )
        }
        val classSubjectResponseList = creditClass?.classSubjectResponseList ?: emptyList()
        val semesterResponseList = creditClass?.semesterResponseList ?: emptyList()
        dropdown_ky_hoc.setOnItemClickListener { parent, view, position, id ->
            currentSemesterIdx = position
            val classSubjectList = mutableListOf<ClassSubjectResponse>()
            for (it in classSubjectResponseList) {
                if (it.semesterResponse == semesterResponseList[position]) {
                    classSubjectList.add(it)
                }
            }
            runOnUiThread {
                recycler_subject.adapter = ItemCreditClassAdapter(classSubjectList)
            }
        }

        dropdown_ky_hoc.setOnClickListener {
            dropdown_ky_hoc.post {
                dropdown_ky_hoc.showDropDown()

                // cuon den vi tri currentSemesterIdx
                val adapterDropdownMenuSemester = dropdown_ky_hoc.adapter as ArrayAdapter<*>
                val viewGroup = dropdown_ky_hoc.parent as ViewGroup
                adapterDropdownMenuSemester.getView(currentSemesterIdx, null, viewGroup)?.let {
                    dropdown_ky_hoc.listSelection = currentSemesterIdx
                }
            }
        }

        if (currentSemesterIdx != -1) {
            dropdown_ky_hoc.setText(semesterNames[currentSemesterIdx], false)
            CreditClass.getCreditClassResponse()?.let {
                val classSubjectList = mutableListOf<ClassSubjectResponse>()
                for (it in classSubjectResponseList) {
                    if (it.semesterResponse == semesterResponseList[currentSemesterIdx]) {
                        classSubjectList.add(it)
                    }
                }
                runOnUiThread {
                    recycler_subject.adapter = ItemCreditClassAdapter(classSubjectList)
                }
            }
        } else {
            Log.d(TAG, "Khong co ky hoc nao cho ngay hom nay")
        }

        subject_loading.visibility = View.GONE
        recycler_subject.visibility = View.VISIBLE
    }

    fun onBackClick() {
        finish()
    }
}