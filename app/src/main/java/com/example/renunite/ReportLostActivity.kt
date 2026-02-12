package com.example.renunite

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ReportLostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_lost)

        findViewById<Button>(R.id.btnBackHeader).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnCloseApp).setOnClickListener {
            finishAffinity()
        }
    }
}