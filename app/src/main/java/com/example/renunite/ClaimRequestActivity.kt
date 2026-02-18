package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClaimRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_request)

        val etProof = findViewById<EditText>(R.id.etProof)
        val btnContinue = findViewById<AppCompatButton>(R.id.btnContinue)
        val btnCancel = findViewById<AppCompatButton>(R.id.btnCancel)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val proofText = etProof.text.toString().trim()
            if (proofText.isEmpty()) {
                showIncompleteDialog()
            } else {
                // Directly proceed to ScheduleAppointmentActivity
                val intent = Intent(this, ScheduleAppointmentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showIncompleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Incomplete Request")
            .setMessage("Please provide a description of proof before proceeding.")
            .setPositiveButton("OK", null)
            .show()
    }
}
