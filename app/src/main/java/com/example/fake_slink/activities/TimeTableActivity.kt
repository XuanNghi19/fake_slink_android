package com.example.fake_slink.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.adapter.ItemTimeTableAdapter
import com.example.fake_slink.databinding.ActivityTimeTableBinding
import com.example.fake_slink.model.Week
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.model.singleton.TimeTables
import com.example.fake_slink.retrofit.TimeTableApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class TimeTableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimeTableBinding
    private val TAG = "TimeTable"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        onLoadTimeTable()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onLoadTimeTable() {
        if (TimeTables.getTimeTables() == null) {
            binding.loading.visibility = View.VISIBLE
            val student = Student.getStudent()
            val idNum = student?.idNum
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val authorizationStr = "Bearer $token"
            if (idNum != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        withTimeout(TimeUnit.SECONDS.toMillis(10)) {
                            val apiResponse = TimeTableApiService.timeTableService.getTimeTable(
                                authorizationStr,
                                idNum
                            )
                            if (apiResponse.code == 200) {
                                TimeTables.loginTimeTables(apiResponse.result)
                            }
                        }
                    } catch (e: Exception) {
                        val errorMessage = e.message
                        Log.e(TAG, errorMessage.toString())
                        runOnUiThread {
                            Toast.makeText(
                                this@TimeTableActivity,
                                "Có lỗi xảy ra: $errorMessage !",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        }

        TimeTables.getTimeTables()?.get(0)?.classSubjectResponse?.let {
            val weeks: List<Week> = Week.createWeeks(it.semesterResponse)
            val today = LocalDate.now()
            var currentWeekIdx = weeks.indexOfFirst { week ->
                today.equals(week.startDate) || today.equals(week.endDate) ||
                        (today.isAfter(week.startDate) && (today.isBefore(week.endDate)))
            }
            val weekNames = weeks.map { it.weekName }
            val adapter = ArrayAdapter(this@TimeTableActivity, R.layout.list_item_week, weekNames)
            binding.dropdownMenuTuanHoc.setAdapter(adapter)
            binding.dropdownMenuTuanHoc.setOnItemClickListener { parent, view, position, id ->
                currentWeekIdx = position

                runOnUiThread {
                    TimeTables.getTimeTables()?.let {
                        binding.tkb.adapter =
                            ItemTimeTableAdapter(this@TimeTableActivity, it, weeks[position])
                    }
                }
            }

            binding.dropdownMenuTuanHoc.setOnClickListener{
                binding.dropdownMenuTuanHoc.post{
                    binding.dropdownMenuTuanHoc.showDropDown()

                    // cuon den vi tri currentWeekIdx
                    val adapterDropdownMenuTuanHoc = binding.dropdownMenuTuanHoc.adapter as ArrayAdapter<*>
                    val viewGroup = binding.dropdownMenuTuanHoc.parent as ViewGroup
                    adapterDropdownMenuTuanHoc.getView(currentWeekIdx, null, viewGroup)?.let {
                        binding.dropdownMenuTuanHoc.listSelection = currentWeekIdx
                    }
                }
            }

            if (currentWeekIdx != -1) {
                binding.dropdownMenuTuanHoc.setText(weekNames[currentWeekIdx], false)
                TimeTables.getTimeTables()?.let {
                    runOnUiThread {
                        binding.tkb.adapter =
                            ItemTimeTableAdapter(this@TimeTableActivity, it, weeks[currentWeekIdx])
                    }
                }
            } else {
                Log.d(TAG, "Khong co tuan nao cho ngay hom nay")
            }

        }
        binding.loading.visibility = View.GONE
    }
}