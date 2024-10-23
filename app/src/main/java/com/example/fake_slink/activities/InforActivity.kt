package com.example.fake_slink.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.singleton.Student
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date

class InforActivity : AppCompatActivity() {

    private lateinit var tai_khoan: TextView
    private lateinit var input_name: TextInputEditText
    private lateinit var input_sex: TextInputEditText
    private lateinit var input_phone_num: TextInputEditText
    private lateinit var input_phone_num_2: TextInputEditText
    private lateinit var input_email: TextInputEditText
    private lateinit var input_cccd: TextInputEditText
    private lateinit var input_dob: TextInputEditText
    private lateinit var input_address: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_infor)

        tai_khoan = findViewById(R.id.tai_khoan)
        input_name = findViewById(R.id.input_name)
        input_sex = findViewById(R.id.input_sex)
        input_phone_num = findViewById(R.id.input_phone_num)
        input_phone_num_2 = findViewById(R.id.input_phone_num_2)
        input_email = findViewById(R.id.input_email)
        input_cccd = findViewById(R.id.input_cccd)
        input_dob = findViewById(R.id.input_dob)
        input_address = findViewById(R.id.input_address)

        setInfo()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setInfo() {
        val student = Student.getStudent()
        input_name.setText(student?.name)
        input_sex.setText(student?.sex)
        input_phone_num.setText(student?.phone1)
        input_phone_num_2.setText(student?.phone2)
        input_email.setText(student?.email)
        input_cccd.setText(student?.cccd)

        val dob = student?.dob
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        input_dob.setText(dateFormat.format(dob))
        input_address.setText(student?.address)
    }

    fun onBackClick(view: View){
        finish()
    }
}