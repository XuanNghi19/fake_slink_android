package com.example.fake_slink.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.singleton.ClassSubjectDetails
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date

class ClassSubjectDetailActivity : AppCompatActivity() {

    private lateinit var text_name_subject: TextView
    private lateinit var subject_idNum: TextView
    private lateinit var teacher_name: TextView
    private lateinit var progress_week: TextView
    private lateinit var progress_week_bar: ProgressBar
    private lateinit var card_info_students: CardView
    private lateinit var card_ket_qua_hoc_tap: CardView
    private lateinit var back: CardView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_class_subject_detail)

        text_name_subject = findViewById(R.id.text_name_subject)
        subject_idNum = findViewById(R.id.subject_idNum)
        teacher_name = findViewById(R.id.teacher_name)
        progress_week = findViewById(R.id.progress_week)
        progress_week_bar = findViewById(R.id.progress_week_bar)
        card_info_students = findViewById(R.id.card_info_students)
        card_ket_qua_hoc_tap = findViewById(R.id.card_ket_qua_hoc_tap)
        back = findViewById(R.id.back)

        back.setOnClickListener {
            onBackClick()
        }
        card_ket_qua_hoc_tap.setOnClickListener {
            startActivity(Intent(this@ClassSubjectDetailActivity, LearningOutcomesActivity::class.java))
        }
        card_info_students.setOnClickListener {
            startActivity(Intent(this@ClassSubjectDetailActivity, StudentsInClassActivity::class.java))
        }
        setInfo()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setInfo() {
        ClassSubjectDetails.get()?.let {
            text_name_subject.text = it.subjectResponse.subjectName
            subject_idNum.text = it.subjectResponse.subjectName
            teacher_name.text = it.teacherResponse.name
            val startDate = it.semesterResponse.startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            val endDate = it.semesterResponse.endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            val currentDate = LocalDate.now()

            val totalWeek = ChronoUnit.WEEKS.between(startDate, endDate) + 1
            val thisWeek = ChronoUnit.WEEKS.between(startDate, currentDate) + 1

            progress_week.text = StringBuffer("$thisWeek/$totalWeek")
            progress_week_bar.max = totalWeek.toInt()
            progress_week_bar.setProgress(thisWeek.toInt(), true)
        }
    }

    private fun onBackClick() {
        finish()
    }
}