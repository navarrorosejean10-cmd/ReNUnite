package com.example.renunite

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val btnBackHeader = findViewById<Button>(R.id.btnBackHeader)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBackHeader.setOnClickListener {
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}