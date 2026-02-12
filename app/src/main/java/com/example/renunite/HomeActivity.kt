package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnReportLost = findViewById<View>(R.id.btnReportLost)
        btnReportLost.setOnClickListener {
            val intent = Intent(this, ReportLostActivity::class.java)
            startActivity(intent)
        }

        val btnReportFound = findViewById<View>(R.id.btnReportFound)
        btnReportFound.setOnClickListener {
            val intent = Intent(this, ReportFoundActivity::class.java)
            startActivity(intent)
        }
    }
}