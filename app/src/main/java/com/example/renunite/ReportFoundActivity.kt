package com.example.renunite

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ReportFoundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_found)

        findViewById<Button>(R.id.btnBackHeader).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnCloseApp).setOnClickListener {
            finishAffinity()
        }
    }
}