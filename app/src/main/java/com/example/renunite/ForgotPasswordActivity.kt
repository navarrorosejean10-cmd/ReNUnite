package com.example.renunite

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnResetPassword = findViewById<MaterialButton>(R.id.btnResetPassword)
        val etEmail = findViewById<EditText>(R.id.etEmail)

        btnBack.setOnClickListener {
            finish()
        }

        btnResetPassword.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                showCustomDialog("Missing Details", "Please provide your email address.", R.drawable.ic_email)
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showCustomDialog("Invalid Email", "Please enter a valid email address.", R.drawable.ic_email)
                return@setOnClickListener
            }

            // Simulate sending reset link
            showCustomDialog("Success", "A reset link has been sent to $email", R.drawable.ic_check_circle) {
                finish()
            }
        }
    }

    private fun showCustomDialog(title: String, message: String, iconRes: Int, onPositiveClick: (() -> Unit)? = null) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btnPositive)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btnNegative)

        ivIcon.setImageResource(iconRes)
        tvTitle.text = title
        tvMessage.text = message
        btnPositive.text = "OK"
        
        btnNegative.visibility = android.view.View.GONE

        btnPositive.setOnClickListener {
            dialog.dismiss()
            onPositiveClick?.invoke()
        }

        dialog.show()
    }
}