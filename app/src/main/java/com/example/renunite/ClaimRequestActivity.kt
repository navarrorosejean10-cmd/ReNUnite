package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClaimRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_request)

        val etProof = findViewById<EditText>(R.id.etProof)

        // Updated to ImageButton to match the new layout
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btnCancel).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<MaterialButton>(R.id.btnContinue).setOnClickListener {
            val proofText = etProof.text.toString().trim()
            if (proofText.isEmpty()) {
                showValidationDialog()
            } else {
                val intent = Intent(this, ScheduleAppointmentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showValidationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Incomplete Request")
            .setMessage("Please provide a description of proof before proceeding.")
            .setPositiveButton("OK", null)
            .show()
    }
}
