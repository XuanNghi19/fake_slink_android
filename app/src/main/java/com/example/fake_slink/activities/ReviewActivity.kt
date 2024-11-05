package com.example.fake_slink.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
import com.example.fake_slink.adapter.ItemReviewFormAdapter
import com.example.fake_slink.model.singleton.GradeAppealsList
import com.example.fake_slink.model.singleton.ReviewFormList
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.retrofit.GradeApiService
import com.example.fake_slink.retrofit.ReviewFormApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.sql.Time
import java.util.concurrent.TimeUnit

class ReviewActivity : AppCompatActivity() {

    private val TAG = "REVIEW"
    private lateinit var back: CardView
    private lateinit var review_form_recycler: RecyclerView
    private lateinit var add_review: ImageButton
    private lateinit var space_side: ImageView
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_review)

        back = findViewById(R.id.back)
        review_form_recycler = findViewById(R.id.review_form_recycler)
        add_review = findViewById(R.id.add_review)
        space_side = findViewById(R.id.space_side)
        loading = findViewById(R.id.loading)

        review_form_recycler.layoutManager = LinearLayoutManager(this@ReviewActivity)
        review_form_recycler.visibility = View.INVISIBLE
        space_side.visibility = View.INVISIBLE
        loading.visibility = View.VISIBLE

        back.setOnClickListener {
            back()
        }
        add_review.setOnClickListener {
            addReview()
        }
        loadData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addReview() {
        startActivity(Intent(this@ReviewActivity, ReviewFormActivity::class.java))
    }

    private fun loadData() {
        val student = Student.getStudent()
        val idNum = student?.idNum
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val authorizationStr = "Bearer $token"

        if (GradeAppealsList.get() == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withTimeout(TimeUnit.SECONDS.toMillis(20)) {
                        val apiResponse =
                            GradeApiService.gradeService.getGradeAppealsList(
                                authorizationStr,
                                idNum!!
                            )
                        if (apiResponse.code == 200) {
                            Log.d(TAG, apiResponse.result.toString())
                            GradeAppealsList.login(apiResponse.result)
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: ${apiResponse.code}")
                        }
                    }
                } catch (e: Exception) {
                    val errorMessage = e.message
                    Log.e(TAG, errorMessage.toString())
                    runOnUiThread {
                        Toast.makeText(
                            this@ReviewActivity,
                            "Có lỗi xảy ra: $errorMessage !",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }

        if (ReviewFormList.get() == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withTimeout(TimeUnit.SECONDS.toMillis(20)) {
                        val apiResponse =
                            ReviewFormApiService.reviewFormService.getAllReviewFormByStudent(
                                authorizationStr,
                                idNum!!
                            )

                        if (apiResponse.code == 200) {
                            Log.d(TAG, apiResponse.result.toString())
                            ReviewFormList.login(apiResponse.result)

                            withContext(Dispatchers.Main) {
                                setData()
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: ${apiResponse.code}")
                        }
                    }
                } catch (e: Exception) {
                    val errorMessage = e.message
                    Log.e(TAG, errorMessage.toString())
                    runOnUiThread {
                        Toast.makeText(
                            this@ReviewActivity,
                            "Có lỗi xảy ra: $errorMessage !",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        } else {
            setData()
        }
    }

    private fun setData() {
        review_form_recycler.adapter = ItemReviewFormAdapter(ReviewFormList.get()!!)
        review_form_recycler.visibility = View.VISIBLE
        space_side.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    private fun back() {
        finish()
    }
}