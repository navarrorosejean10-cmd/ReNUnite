package com.example.renunite

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ClaimRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_request)

        val etProof = findViewById<EditText>(R.id.etProof)
        val llProofContainer = findViewById<LinearLayout>(R.id.llProofContainer)
        val tvProofError = findViewById<TextView>(R.id.tvProofError)
        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue)
        val btnCancel = findViewById<MaterialButton>(R.id.btnCancel)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        etProof.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    tvProofError.visibility = View.GONE
                    llProofContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnContinue.setOnClickListener {
            val proofText = etProof.text.toString().trim()
            if (proofText.isEmpty()) {
                tvProofError.visibility = View.VISIBLE
                llProofContainer.setBackgroundResource(R.drawable.input_field_bg_error)
            } else {
                // Directly proceed to ScheduleAppointmentActivity
                val intent = Intent(this, ScheduleAppointmentActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
