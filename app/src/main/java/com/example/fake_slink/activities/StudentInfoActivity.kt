package com.example.fake_slink.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.request.UpdatePasswordRequest
import com.example.fake_slink.model.request.UploadAvatarRequest
import com.example.fake_slink.model.response.ApiResponse
import com.example.fake_slink.model.singleton.Student
import com.example.fake_slink.retrofit.StudentApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class StudentInfoActivity : AppCompatActivity() {
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.reference

    private lateinit var id_num: TextView
    private lateinit var name: TextView
    private lateinit var avatar_image: ImageView
    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_info)

        id_num = findViewById(R.id.id_num)
        name = findViewById(R.id.name)
        avatar_image = findViewById(R.id.avatar_image)
        setInfo()

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                avatar_image.setImageURI(it)
                if (it != null) {
                    Student.getStudent()?.avatarUrl = it.toString()
                    val contentResolve = applicationContext.contentResolver
                    val mimeType = contentResolver.getType(it)
                    val extension = when (mimeType) {
                        "image/jpeg" -> ".jpg"
                        "image/png" -> ".png"
                        else -> ""
                    }

                    val fileReference = storageReference.child(
                        "avatars/" + Student.getStudent()?.idNum + extension
                    )
                    fileReference.putFile(it!!)
                        .addOnSuccessListener { taskSnapshot ->
                            fileReference.downloadUrl.addOnSuccessListener { uri ->
                                Student.getStudent()?.avatarUrl = it.toString()
                                Log.d("STUDENT_INFO", "Upload success, url image: $uri")
                                Student.getStudent()?.avatarUrl = uri.toString()
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val sharedPreferences =
                                            getSharedPreferences("MySharedPref", MODE_PRIVATE)
                                        val token = sharedPreferences.getString("token", null)
                                        val authorizationStr = "Bearer $token"
                                        val uploadAvatarRequest =
                                            UploadAvatarRequest(uri.toString())
                                        val apiResponse =
                                            StudentApiService.studentService.uploadAvatarUrl(
                                                authorizationStr,
                                                uploadAvatarRequest
                                            )

                                        if (apiResponse.code == 200) {
                                            if (apiResponse.result == true) {
                                                runOnUiThread {
                                                    Toast.makeText(
                                                        this@StudentInfoActivity,
                                                        "Thay đổi ảnh thành công!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            } else {
                                                runOnUiThread {
                                                    Toast.makeText(
                                                        this@StudentInfoActivity,
                                                        "Upload failed!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            }
                                        } else {
                                            runOnUiThread {
                                                Toast.makeText(
                                                    this@StudentInfoActivity,
                                                    "Upload failed, apiResponse.code: ${apiResponse.code} !",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }
                                    } catch (e: Exception) {
                                        val errorMessage = e.message
                                        Log.e("STUDENT_INFO", errorMessage.toString())
                                        runOnUiThread {
                                            Toast.makeText(
                                                this@StudentInfoActivity,
                                                "Có lỗi xảy ra: $errorMessage !",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@StudentInfoActivity,
                                    "Upload failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@StudentInfoActivity,
                            "Chưa chọn ảnh!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun setInfo() {
        val student = Student.getStudent()
        id_num.text = "MSV: ${student?.idNum}"
        name.text = student?.name
        Picasso.get()
            .load(student?.avatarUrl)
            .into(avatar_image)
    }

    fun onHomeClick(view: View) {
        val homeIntent = Intent(this@StudentInfoActivity, HomeActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(homeIntent)
        finish()
    }

    fun onInfoClick(view: View) {
        val infoIntent = Intent(this@StudentInfoActivity, InforActivity::class.java)
        startActivity(infoIntent)
    }

    fun onLogoutClick(view: View) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        // delete token
        edit.remove("token")

        //delete student data
        Student.logoutStudent()

        // chuyen den man hinh Login
        val loginIntent = Intent(this@StudentInfoActivity, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    fun onChangePassword(view: View) {
        val TAGD = "change_password_dialog"
        val dialog = Dialog(this@StudentInfoActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.change_password_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window = dialog.window
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        val old_pass = dialog.findViewById<TextInputEditText>(R.id.old_pass)
        val new_pass = dialog.findViewById<TextInputEditText>(R.id.new_pass)
        val load = dialog.findViewById<ProgressBar>(R.id.load)
        val btn_change_pass = dialog.findViewById<MaterialButton>(R.id.btn_change_pass)

        btn_change_pass.setOnClickListener {
            val old_pass_text = old_pass.text
            val new_pass_text = new_pass.text

            if (old_pass_text != null && new_pass_text != null) {
                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val token = sharedPreferences.getString("token", null)

                if (token != null) {
                    val authorizationStr = "Bearer $token"

                    val updatePasswordRequest = UpdatePasswordRequest(
                        old_pass_text.toString(),
                        new_pass_text.toString()
                    )

                    load.visibility = View.VISIBLE

                    StudentApiService.studentService.updatePassword(
                        authorizationStr,
                        updatePasswordRequest
                    ).enqueue(object : retrofit2.Callback<ApiResponse<Boolean>> {
                        override fun onResponse(
                            call: Call<ApiResponse<Boolean>>,
                            response: Response<ApiResponse<Boolean>>
                        ) {
                            if (response.isSuccessful) {
                                val apiResponse = response.body()
                                if (apiResponse?.result == true) {
                                    dialog.hide()
                                    Log.e(TAGD, "Lưu mật khẩu mới thành công!")
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@StudentInfoActivity,
                                            "Lưu mật khẩu mới thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Log.e(TAGD, "Sai mật khẩu!")
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@StudentInfoActivity,
                                            "Sai mật khẩu!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Log.e(TAGD, response.message())
                                runOnUiThread {
                                    Toast.makeText(
                                        this@StudentInfoActivity,
                                        response.message(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                            Log.e(TAGD, "Send api request failed!")
                            runOnUiThread {
                                Toast.makeText(
                                    this@StudentInfoActivity,
                                    "Send api request failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                } else {
                    Log.e(TAGD, "Token không được lưu!")
                    runOnUiThread {
                        Toast.makeText(
                            this@StudentInfoActivity,
                            "Token không được lưu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                runOnUiThread {
                    Toast.makeText(
                        this@StudentInfoActivity,
                        "Vui lòng điền đủ thông tin!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        dialog.show()
    }

    fun onChangeImageClick(view: View) {
        getContent.launch("image/*")
    }
}