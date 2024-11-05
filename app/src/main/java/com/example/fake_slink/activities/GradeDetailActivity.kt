package com.example.fake_slink.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.singleton.GradeDetail
import com.google.android.material.button.MaterialButton
import java.util.Date

class GradeDetailActivity : AppCompatActivity() {
    private val TAG = "GradeDetail"
    private lateinit var back: CardView
    private lateinit var diem_cc: TextView
    private lateinit var diem_bt: TextView
    private lateinit var diem_th: TextView
    private lateinit var diem_kt: TextView
    private lateinit var diem_ck: TextView
    private lateinit var diem_tkhs4: TextView
    private lateinit var diem_tkhs10: TextView
    private lateinit var diem_tkhc: TextView
    private lateinit var add_review: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grade_detail)

        back = findViewById(R.id.back)
        diem_cc = findViewById(R.id.diem_cc)
        diem_bt = findViewById(R.id.diem_bt)
        diem_th = findViewById(R.id.diem_th)
        diem_kt = findViewById(R.id.diem_kt)
        diem_ck = findViewById(R.id.diem_ck)
        diem_tkhs4 = findViewById(R.id.diem_tkhs4)
        diem_tkhs10 = findViewById(R.id.diem_tkhs10)
        diem_tkhc = findViewById(R.id.diem_tkhc)
        add_review = findViewById(R.id.add_review)

        back.setOnClickListener {
            back()
        }
        loadData()
        add_review.setOnClickListener {
            add_review()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData() {
        GradeDetail.get()?.let {
            diem_cc.text = it.diemCC.toString()
            diem_bt.text = it.diemBT.toString()
            diem_th.text = it.diemTH.toString()
            diem_kt.text = it.diemKT.toString()
            diem_ck.text = it.diemCK.toString()
            if (it.status == "Đã hoàn thành môn học") {
                diem_tkhs4.text = AppHelper.convertScoreToGradeNum(it.diemTK)
                diem_tkhs10.text = it.diemTK.toString()
                diem_tkhc.text = AppHelper.convertScoreToGrade(it.diemTK)
            }
        }
    }

    private fun back() {
        finish()
    }

    private fun add_review() {
        val appealsDateline = GradeDetail.get()?.appealsDateline
        if(appealsDateline != null) {
            if(appealsDateline.after(Date()) || appealsDateline == Date()) {
                val reviewFormIntent = Intent(this@GradeDetailActivity, ReviewFormActivity::class.java)
                reviewFormIntent.putExtra("fromGradeDetail", true)
                startActivity(reviewFormIntent)
            } else {
                Toast.makeText(
                    this@GradeDetailActivity,
                    "Hết thời hạn tạo phiếu phúc khảo cho học phần này!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this@GradeDetailActivity,
                "Học phần này không trong thời gian phúc khảo!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}