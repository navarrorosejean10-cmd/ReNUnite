package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<AppCompatButton>(R.id.btnLogin)
        val btnCreateAccount = findViewById<MaterialButton>(R.id.btnCreateAccount)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val btnMicrosoft = findViewById<MaterialButton>(R.id.btnMicrosoft)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePassword)

        var isPasswordVisible = false

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // Show Password
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Hide Password
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_visibility)
            }
            // Move cursor to the end of the text
            etPassword.setSelection(etPassword.text.length)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnMicrosoft.setOnClickListener {
            // Handle Microsoft login
        }
    }
}