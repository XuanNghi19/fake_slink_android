package com.example.fake_slink.activities

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemTkbMiniAdapter
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.model.singleton.TimeTables
import com.example.fake_slink.model.singleton.ViewExamSchedule
import com.example.fake_slink.notification.NotificationUtils
import com.example.fake_slink.retrofit.ExamScheduleApiService
import com.example.fake_slink.retrofit.TimeTableApiService
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private val student = Student.getStudent()
    private val idNum = student?.idNum
    private val TAG = "HOME"

    private lateinit var gpa_text_bar: TextView
    private lateinit var avatar_image: CircleImageView
    private lateinit var name_text_bar: TextView
    private lateinit var id_text_bar: TextView
    private lateinit var tkb: ListView
    private lateinit var mo_rong: TextView
    private lateinit var load_tkb_mini: ProgressBar
    private lateinit var chuc_nang: TextView
    private lateinit var main_constraint: ConstraintLayout
    private lateinit var main_scroll_view: ScrollView
    private lateinit var thoi_khoa_bieu: ImageView
    private lateinit var tin_tuc: ImageView
    private lateinit var ket_qua_hoc_tap: ImageView
    private lateinit var lop_tin_chi: ImageView
    private lateinit var xem_lich_thi: ImageView
    private lateinit var tien_ich: ImageView
    private lateinit var hoc_tap: ImageView
    private lateinit var notification: ImageView
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        gpa_text_bar = findViewById(R.id.gpa_text_bar)
        avatar_image = findViewById(R.id.avatar_image)
        name_text_bar = findViewById(R.id.name_text_bar)
        id_text_bar = findViewById(R.id.id_text_bar)
        tkb = findViewById(R.id.tkb)
        mo_rong = findViewById(R.id.mo_rong)
        load_tkb_mini = findViewById(R.id.load_tkb_mini)
        chuc_nang = findViewById(R.id.chuc_nang)
        main_constraint = findViewById(R.id.main_contraint)
        main_scroll_view = findViewById(R.id.main_scroll_view)
        thoi_khoa_bieu = findViewById(R.id.thoi_khoa_bieu)
        tin_tuc = findViewById(R.id.tin_tuc)
        ket_qua_hoc_tap = findViewById(R.id.ket_qua_hoc_tap)
        lop_tin_chi = findViewById(R.id.lop_tin_chi)
        xem_lich_thi = findViewById(R.id.xem_lich_thi)
        tien_ich = findViewById(R.id.tien_ich)
        hoc_tap = findViewById(R.id.hoc_tap)
        notification = findViewById(R.id.notification)
        notification.setOnClickListener {
            notification()
        }
        tien_ich.setOnClickListener {
            tienIch()
        }
        hoc_tap.setOnClickListener {
            hocTap()
        }
        thoi_khoa_bieu.setOnClickListener {
            thoiKhoaBieu()
        }
        lop_tin_chi.setOnClickListener {
            lop_tin_chi()
        }
        xem_lich_thi.setOnClickListener {
            xemLichThi()
        }
        ket_qua_hoc_tap.setOnClickListener {
            ket_qua_hoc_tap()
        }
        tin_tuc.setOnClickListener {
            tin_tuc()
        }
        tkb.visibility = View.GONE
        mo_rong.visibility = View.GONE

        val constraintSet = ConstraintSet()
        constraintSet.clone(main_constraint)

        constraintSet.connect(
            chuc_nang.id,
            ConstraintSet.TOP,
            load_tkb_mini.id,
            ConstraintSet.BOTTOM,
            25.dpToPx(this)
        )
        constraintSet.applyTo(main_constraint)

        setHeaderInformation()
        getMiniTkb()
        loadExamSchedule()
        setHomeOnClick()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun tienIch() {
        startActivity(Intent(this@HomeActivity, UtilitiesActivity::class.java))
    }

    private fun hocTap() {
        startActivity(Intent(this@HomeActivity, StudyCornerActivity::class.java))
    }

    private fun xemLichThi() {
        startActivity(Intent(this@HomeActivity, ExamScheduleActivity::class.java))
    }

    private fun notification() {
        startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        Picasso.get()
            .load(Student.getStudent()?.avatarUrl)
            .into(avatar_image)
    }

    private fun setHeaderInformation() {
        val student = Student.getStudent()
        Log.d("HOME", student?.avatarUrl.toString())
        Picasso.get()
            .load(student?.avatarUrl)
            .into(avatar_image)
        gpa_text_bar.text = student?.gpa.toString() + "/4"
        name_text_bar.text = student?.name
        id_text_bar.text = "MSV: ${student?.idNum}";
    }

    private fun getMiniTkb() {
        if (idNum != null) {
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val authorizationStr = "Bearer $token"
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withTimeout(TimeUnit.SECONDS.toMillis(10)) {
                        val apiResponse = TimeTableApiService.timeTableService.getTimeTable(
                            authorizationStr,
                            idNum
                        )
                        if (apiResponse.code == 200) {
                            runOnUiThread {
                                apiResponse.result?.let { listTimeTableResponse ->
                                    val adapter = ItemTkbMiniAdapter(
                                        this@HomeActivity,
                                        listTimeTableResponse
                                    )
                                    load_tkb_mini.visibility = View.GONE
                                    tkb.visibility = View.VISIBLE
                                    mo_rong.visibility = View.VISIBLE

                                    val constraintSet = ConstraintSet()
                                    constraintSet.clone(main_constraint)

                                    constraintSet.connect(
                                        chuc_nang.id,
                                        ConstraintSet.TOP,
                                        mo_rong.id,
                                        ConstraintSet.BOTTOM,
                                        15.dpToPx(this@HomeActivity)
                                    )
                                    constraintSet.applyTo(main_constraint)
                                    tkb.adapter = adapter

                                    TimeTables.loginTimeTables(listTimeTableResponse)
                                }
                            } ?: run {
                                val errorMessage = "No response body"
                                Log.e(TAG, errorMessage)
                                Toast.makeText(
                                    this@HomeActivity,
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
                            this@HomeActivity,
                            "Có lỗi xảy ra: $errorMessage !",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }

    private fun setHomeOnClick() {
        mo_rong.setOnClickListener {
            if (isOpen) {
                val layoutPrams = tkb.layoutParams
                val heightInDp = 248
                val scale = resources.displayMetrics.density
                val targetHeightInPx = (heightInDp * scale + 0.5f).toInt()
                val startHeight = layoutPrams.height

                // animation
                val animator = ValueAnimator.ofInt(startHeight, targetHeightInPx)
                animator.addUpdateListener { valueAnimator ->
                    val animatedValue = valueAnimator.animatedValue as Int

                    layoutPrams.height = animatedValue
                    tkb.layoutParams = layoutPrams
                }
                animator.duration = 500
                animator.start()
                val spannableString = SpannableString(getString(R.string.mo_rong))
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                mo_rong.text = spannableString

                main_scroll_view.post {
                    main_scroll_view.smoothScrollTo(0, 0)
                }

                isOpen = false
            } else {
                val layoutPrams = tkb.layoutParams
                val heightInDp = 868
                val scale = resources.displayMetrics.density
                val targetHeightInPx = (heightInDp * scale + 0.5f).toInt()
                val startHeight = layoutPrams.height

                // animation
                val animator = ValueAnimator.ofInt(startHeight, targetHeightInPx)
                animator.addUpdateListener { valueAnimator ->
                    val animatedValue = valueAnimator.animatedValue as Int

                    layoutPrams.height = animatedValue
                    tkb.layoutParams = layoutPrams
                }
                animator.duration = 500
                animator.start()
                val spannableString = SpannableString(getString(R.string.thu_gon))
                spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
                mo_rong.text = spannableString

                isOpen = true
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun onCaNhanClick(view: View) {
        val studentInfoIntent = Intent(this@HomeActivity, StudentInfoActivity::class.java)
        startActivity(studentInfoIntent)
    }

    private fun thoiKhoaBieu() {
        val timeTableIntent = Intent(this@HomeActivity, TimeTableActivity::class.java)
        startActivity(timeTableIntent)
    }

    private fun lop_tin_chi() {
        startActivity(Intent(this@HomeActivity, CreditClassActivity::class.java))
    }

    private fun ket_qua_hoc_tap() {
        startActivity(Intent(this@HomeActivity, LearningOutcomesActivity::class.java))
    }

    private fun tin_tuc() {
        val uri = Uri.parse("https://ptit.edu.vn/giao-vu")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun loadExamSchedule() {
        if(idNum != null) {
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val authorizationStr = "Bearer $token"
            CoroutineScope(Dispatchers.IO).launch {
                withTimeout(TimeUnit.SECONDS.toMillis(10)) {
                    val apiResponse =
                        ExamScheduleApiService.examScheduleService.getAllExamScheduleByStudent(
                            authorizationStr,
                            idNum
                        )
                    if (apiResponse.code == 200) {
                        Log.d(TAG, apiResponse.result.toString())
                        ViewExamSchedule.login(apiResponse.result)

                        ViewExamSchedule.get()?.examScheduleResponseList?.let {
                            for(x in it) {
                                NotificationUtils.createExamScheduleNotification(this@HomeActivity, x)
                            }
                        }

                    } else {
                        Log.e(TAG, "Có lỗi xảy ra: ${apiResponse.code}")
                        runOnUiThread {
                            Toast.makeText(
                                this@HomeActivity,
                                "Có lỗi xảy ra: ${apiResponse.code}!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        }
    }
}

























