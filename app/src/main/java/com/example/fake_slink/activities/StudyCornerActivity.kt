package com.example.fake_slink.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R

class StudyCornerActivity : AppCompatActivity() {

    private lateinit var back: CardView
    private lateinit var creditsClass: CardView
    private lateinit var appeal: CardView
    private lateinit var examSchedule: CardView
    private lateinit var timeTable: CardView
    private lateinit var kqht: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_corner)

        back = findViewById(R.id.back)
        creditsClass = findViewById(R.id.creditsClass)
        appeal = findViewById(R.id.appeal)
        examSchedule = findViewById(R.id.examSchedule)
        timeTable = findViewById(R.id.timeTable)
        kqht = findViewById(R.id.kqht)

        back.setOnClickListener {
            back()
        }
        creditsClass.setOnClickListener {
            creditsClass()
        }
        appeal.setOnClickListener {
            appeal()
        }
        examSchedule.setOnClickListener {
            examSchedule()
        }
        timeTable.setOnClickListener {
            timeTable()
        }
        kqht.setOnClickListener {
            kqht()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun back() {
        finish()
    }

    private fun creditsClass() {
        startActivity(Intent(this@StudyCornerActivity, CreditClassActivity::class.java))
    }

    private fun appeal() {
        startActivity(Intent(this@StudyCornerActivity, ReviewActivity::class.java))
    }

    private fun examSchedule() {
        startActivity(Intent(this@StudyCornerActivity, ExamScheduleActivity::class.java))
    }

    private fun timeTable() {
        startActivity(Intent(this@StudyCornerActivity, TimeTableActivity::class.java))
    }

    private fun kqht() {
        startActivity(Intent(this@StudyCornerActivity, LearningOutcomesActivity::class.java))
    }
}