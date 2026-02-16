package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val btnMicrosoft = findViewById<Button>(R.id.btnMicrosoft)

        btnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCreateAccount.setOnClickListener {
            // Assuming CreateAccountActivity exists based on previous code
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            // Handle forgot password
        }

        btnMicrosoft.setOnClickListener {
            // Handle Microsoft login
        }
    }
}