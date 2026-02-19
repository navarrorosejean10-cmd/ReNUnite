package com.example.renunite

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
        
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val llEmailContainer = findViewById<LinearLayout>(R.id.llEmailContainer)
        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val llPasswordContainer = findViewById<LinearLayout>(R.id.llPasswordContainer)
        val tvPasswordError = findViewById<TextView>(R.id.tvPasswordError)
        
        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePassword)

        var isPasswordVisible = false

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_visibility)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isEmpty()) {
                    llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                    tvEmailError.visibility = View.GONE
                    return
                }

                if (input.endsWith("@students.nu-dasma.edu.ph")) {
                    llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_success)
                    tvEmailError.visibility = View.GONE
                } else {
                    llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvEmailError.text = "Please use your official NU student email."
                    tvEmailError.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    tvPasswordError.visibility = View.GONE
                    llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            var isValid = true

            if (email.isEmpty()) {
                tvEmailError.text = "Please enter email or username"
                tvEmailError.visibility = View.VISIBLE
                llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                isValid = false
            } else if (!email.endsWith("@students.nu-dasma.edu.ph")) {
                tvEmailError.text = "Please use your official NU student email."
                tvEmailError.visibility = View.VISIBLE
                llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                isValid = false
            }

            if (password.isEmpty()) {
                tvPasswordError.visibility = View.VISIBLE
                llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                isValid = false
            }

            if (isValid) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        btnMicrosoft.setOnClickListener {
            showMicrosoftConnectDialog()
        }
    }

    private fun showMicrosoftConnectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_microsoft_connect)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val btnContinueMicrosoft = dialog.findViewById<MaterialButton>(R.id.btnContinueMicrosoft)

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        btnContinueMicrosoft.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun showForgotPasswordDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val btnSendReset = dialog.findViewById<MaterialButton>(R.id.btnSendReset)
        val etEmailReset = dialog.findViewById<EditText>(R.id.etEmailReset)
        val llEmailResetContainer = dialog.findViewById<LinearLayout>(R.id.llEmailResetContainer)
        val tvEmailResetError = dialog.findViewById<TextView>(R.id.tvEmailResetError)

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        etEmailReset.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isEmpty()) {
                    llEmailResetContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                    tvEmailResetError.visibility = View.GONE
                    return
                }

                if (input.endsWith("@students.nu-dasma.edu.ph")) {
                    llEmailResetContainer.setBackgroundResource(R.drawable.input_field_bg_success)
                    tvEmailResetError.visibility = View.GONE
                } else {
                    llEmailResetContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvEmailResetError.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSendReset.setOnClickListener {
            val email = etEmailReset.text.toString().trim()
            if (email.endsWith("@students.nu-dasma.edu.ph")) {
                dialog.dismiss()
                showEmailSentSuccessDialog()
            } else {
                llEmailResetContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                tvEmailResetError.visibility = View.VISIBLE
            }
        }

        dialog.show()
    }

    private fun showEmailSentSuccessDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_email_sent_success)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDone = dialog.findViewById<MaterialButton>(R.id.btnDone)

        btnDone.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
