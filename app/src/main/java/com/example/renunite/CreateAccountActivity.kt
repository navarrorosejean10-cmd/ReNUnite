package com.example.renunite

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val llBack = findViewById<LinearLayout>(R.id.llBack)
        val btnCreateAccount = findViewById<MaterialButton>(R.id.btnCreateAccount)
        val llDepartment = findViewById<LinearLayout>(R.id.llDepartment)
        val llProgram = findViewById<LinearLayout>(R.id.llProgram)

        llBack.setOnClickListener {
            finish()
        }

        btnCreateAccount.setOnClickListener {
            // Handle account creation logic
            finish()
        }

        llDepartment.setOnClickListener {
            // Show department selection
        }

        llProgram.setOnClickListener {
            // Show program selection
        }
    }
}