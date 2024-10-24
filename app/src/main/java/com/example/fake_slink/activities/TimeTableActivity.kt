package com.example.fake_slink.activities

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fake_slink.R
import com.example.fake_slink.model.singleton.TimeTables

class TimeTableActivity : AppCompatActivity() {

    private lateinit var dropdown_menu_tuan_hoc: AutoCompleteTextView;
    private lateinit var tkb: ListView;
    private lateinit var loading: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_time_table)

        dropdown_menu_tuan_hoc = findViewById(R.id.dropdown_menu_tuan_hoc)
        tkb = findViewById(R.id.tkb)
        loading = findViewById(R.id.loading)

        onLoadTimeTable()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun onLoadTimeTable() {
        if(TimeTables.getTimeTables() ==  null) {
            loading.visibility = View.VISIBLE
        }
    }
}