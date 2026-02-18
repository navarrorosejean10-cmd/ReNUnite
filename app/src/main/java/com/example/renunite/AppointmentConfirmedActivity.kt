package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class AppointmentConfirmedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_confirmed)

        // Retrieve data from Intent
        val selectedDate = intent.getStringExtra("SELECTED_DATE") ?: "February 12, 2026"
        val selectedTime = intent.getStringExtra("SELECTED_TIME") ?: "2:00 PM"

        // Update UI with confirmed details
        findViewById<TextView>(R.id.tvConfirmedDate).text = selectedDate
        findViewById<TextView>(R.id.tvConfirmedTime).text = selectedTime

        findViewById<AppCompatButton>(R.id.btnBackToHome).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        findViewById<AppCompatButton>(R.id.btnViewReports).setOnClickListener {
            val intent = Intent(this, MyReportsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
