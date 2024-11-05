package com.example.fake_slink.activities

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.helpers.AppHelper
import com.example.fake_slink.model.request.CreateReviewFormRequest
import com.example.fake_slink.model.response.ClassSubjectResponse
import com.example.fake_slink.model.response.GradeResponse
import com.example.fake_slink.model.singleton.GradeAppealsList
import com.example.fake_slink.model.singleton.GradeDetail
import com.example.fake_slink.model.singleton.LearningOutcomes
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.retrofit.GradeApiService
import com.example.fake_slink.retrofit.ReviewFormApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class ReviewFormActivity : AppCompatActivity() {
    private val TAG = "REVIEW_FORM"
    private lateinit var gradeAppealsList: List<GradeResponse>
    private lateinit var fileUri: Uri
    private lateinit var extension: String

    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    private lateinit var dropdown_hoc_phan: AutoCompleteTextView
    private lateinit var back: CardView
    private lateinit var name: TextView
    private lateinit var ma_sv: TextView
    private lateinit var message: EditText
    private lateinit var upload: CardView
    private lateinit var upload_file: CardView
    private lateinit var upload_pdf_file: CardView
    private lateinit var loading: ProgressBar
    private lateinit var layout_hoc_phan: TextInputLayout
    private lateinit var send: MaterialButton
    private lateinit var name_file: TextView
    private lateinit var name_file_pdf: TextView
    private lateinit var scroll: ScrollView
    private lateinit var coating: ImageView
    private lateinit var loading_send: ProgressBar

    private lateinit var classSubjectResponse: ClassSubjectResponse

    @RequiresApi(Build.VERSION_CODES.O)
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                handleFileSelection(it)
            } ?: Toast.makeText(
                this@ReviewFormActivity,
                "Không có file nào được chọn!",
                Toast.LENGTH_SHORT
            )
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_review_form)

        dropdown_hoc_phan = findViewById(R.id.dropdown_hoc_phan)
        back = findViewById(R.id.back)
        name = findViewById(R.id.name)
        ma_sv = findViewById(R.id.ma_sv)
        message = findViewById(R.id.message)
        upload = findViewById(R.id.upload)
        upload_file = findViewById(R.id.upload_file)
        upload_pdf_file = findViewById(R.id.upload_pdf_file)
        loading = findViewById(R.id.loading)
        layout_hoc_phan = findViewById(R.id.layout_hoc_phan)
        send = findViewById(R.id.send)
        name_file = findViewById(R.id.name_file)
        name_file_pdf = findViewById(R.id.name_file_pdf)
        scroll = findViewById(R.id.scroll)
        coating = findViewById(R.id.coating)
        loading_send = findViewById(R.id.loading_send)

        back.setOnClickListener {
            back()
        }

        upload.setOnClickListener {
            selectFile()
        }

        upload_file.setOnClickListener {
            selectFile()
        }

        upload_pdf_file.setOnClickListener {
            selectFile()
        }

        send.setOnClickListener {
            send()
        }
        loadConfig()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadConfig() {
        val fromGradeDetail = intent.getBooleanExtra("fromGradeDetail", false)

        if (fromGradeDetail) {
            GradeDetail.get()?.let {
                classSubjectResponse = it.classSubjectResponse
                dropdown_hoc_phan.setText(classSubjectResponse.subjectResponse.subjectName, false)
                dropdown_hoc_phan.isFocusable = false
                dropdown_hoc_phan.isEnabled = false
            }
        } else {
            loading.visibility = View.VISIBLE
            layout_hoc_phan.visibility = View.GONE
            loadData()
        }
    }

    private fun loadData() {
        if (GradeAppealsList.get() == null) {
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
                                GradeApiService.gradeService.getGradeAppealsList(
                                    authorizationStr,
                                    idNum
                                )
                            if (apiResponse.code == 200) {
                                Log.d(TAG, apiResponse.result.toString())
                                GradeAppealsList.login(apiResponse.result)

                                withContext(Dispatchers.Main) {
                                    setData()
                                }

                            }  else {
                                Log.e(TAG, "Có lỗi xảy ra: ${apiResponse.code}")
                                runOnUiThread {
                                    Toast.makeText(
                                        this@ReviewFormActivity,
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
                                this@ReviewFormActivity,
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
        Student.getStudent()?.let {
            name.text = it.name
            ma_sv.text = it.idNum
        }

        gradeAppealsList = GradeAppealsList.get() ?: emptyList()
        var currentSubjectIdx = 0

        val subjectNames =
            listOf("Học phần") + gradeAppealsList.map { it.classSubjectResponse.subjectResponse.subjectName }
        runOnUiThread {
            dropdown_hoc_phan.setAdapter(
                ArrayAdapter(this@ReviewFormActivity, R.layout.list_item_week, subjectNames)
            )
        }

        dropdown_hoc_phan.setOnItemClickListener { parent, view, position, id ->
            currentSubjectIdx = position
            classSubjectResponse = gradeAppealsList[position].classSubjectResponse
        }

        dropdown_hoc_phan.setOnClickListener {
            dropdown_hoc_phan.post {
                dropdown_hoc_phan.showDropDown()

                // cuon den vi tri currentSemesterIdx
                val adapterDropdownMenuSemester = dropdown_hoc_phan.adapter as ArrayAdapter<*>
                val viewGroup = dropdown_hoc_phan.parent as ViewGroup
                adapterDropdownMenuSemester.getView(currentSubjectIdx, null, viewGroup).let {
                    dropdown_hoc_phan.listSelection = currentSubjectIdx
                }
            }
        }

        loading.visibility = View.GONE
        layout_hoc_phan.visibility = View.VISIBLE
    }

    private fun send() {
        scroll.isEnabled = false
        coating.visibility = View.VISIBLE
        loading_send.visibility = View.VISIBLE
        val cloudPath =
            "files/" + Student.getStudent()?.idNum + "_" + AppHelper.generateUniqueStringUsingUUID() + ".$extension"
        val fileReference = storageReference.child(
            cloudPath
        )
        Log.d(TAG, cloudPath)
        fileReference.putFile(fileUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    Log.d(TAG, "Upload file success: $uri")

                    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                    val token = sharedPreferences.getString("token", null)
                    val authorizationStr = "Bearer $token"
                    val studentIdNum = Student.getStudent()?.idNum

                    val createReviewFormRequest = CreateReviewFormRequest(
                        studentIdNum!!,
                        classSubjectResponse.classSubjectID,
                        message.text.toString(),
                        uri.toString()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val apiResponse =
                            ReviewFormApiService.reviewFormService.createReviewForm(
                                authorizationStr,
                                createReviewFormRequest
                            )

                        if (apiResponse.result) {
                            Log.d(TAG, "Tạo đơn phúc khảo thành công!")
                            runOnUiThread {
                                Toast.makeText(
                                    this@ReviewFormActivity,
                                    "Tạo đơn phúc khảo thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            finish()
                        } else {
                            scroll.isEnabled = true
                            coating.visibility = View.GONE
                            loading_send.visibility = View.GONE
                            Log.e(TAG, "Tạo đơn phúc khảo thất bại!")
                            runOnUiThread {
                                Toast.makeText(
                                    this@ReviewFormActivity,
                                    "Tạo đơn phúc khảo thất bại!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Tạo đơn phúc khảo thất bại, lỗi: ${e.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@ReviewFormActivity,
                        "Tạo đơn phúc khảo thất bại, lỗi: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectFile() {
        // define MIME type for pdfs, image and docs
        val mimeType = arrayOf(
            "application/pdf",
            "image/*",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )

        filePickerLauncher.launch(mimeType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleFileSelection(uri: Uri) {
        extension = AppHelper.getFileExtension(this@ReviewFormActivity, uri) ?: ""
        val fileName: String = AppHelper.getFileName(this@ReviewFormActivity, uri) ?: ""
        if (extension != "") {
            upload.visibility = View.GONE
            if (extension == "pdf") {
                upload_pdf_file.visibility = View.VISIBLE
                upload_file.visibility = View.GONE
                name_file_pdf.text = fileName
                name_file_pdf.ellipsize = TextUtils.TruncateAt.END
            } else {
                upload_file.visibility = View.VISIBLE
                upload_pdf_file.visibility = View.GONE
                name_file.text = fileName
                name_file.ellipsize = TextUtils.TruncateAt.END
            }
        }

        fileUri = uri
    }

    private fun back() {
        finish()
    }
}