package com.example.fake_slink.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.singleton.GradeDetail

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
        GradeDetail.get()?.let {
            diem_cc.text = it.diemCC.toString()
            diem_bt.text = it.diemBT.toString()
            diem_th.text = it.diemTH.toString()
            diem_kt.text = it.diemKT.toString()
            diem_ck.text = it.diemCK.toString()
            if(it.status.equals("Đã hoàn thành môn học")) {
                diem_tkhs4.text = AppHelper.convertScoreToGradeNum(it.diemTK)
                diem_tkhs10.text = it.diemTK.toString()
                diem_tkhc.text = AppHelper.convertScoreToGrade(it.diemTK)
            }
        }
    }

    private fun back() {
        finish()
    }
}