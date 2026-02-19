package com.example.renunite

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var etCurrentPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var llCurrentPasswordContainer: LinearLayout
    private lateinit var llNewPasswordContainer: LinearLayout
    private lateinit var llConfirmPasswordContainer: LinearLayout
    private lateinit var llRequirementsContainer: LinearLayout
    
    private lateinit var tvRequirementLength: TextView
    private lateinit var tvRequirementAlphabet: TextView
    private lateinit var tvRequirementNumber: TextView
    private lateinit var tvRequirementSpecial: TextView
    private lateinit var tvRequirementMatch: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_settings)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnUpdatePassword = findViewById<MaterialButton>(R.id.btnUpdatePassword)
        
        etCurrentPassword = findViewById(R.id.etCurrentPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        
        llCurrentPasswordContainer = findViewById(R.id.llCurrentPasswordContainer)
        llNewPasswordContainer = findViewById(R.id.llNewPasswordContainer)
        llConfirmPasswordContainer = findViewById(R.id.llConfirmPasswordContainer)
        llRequirementsContainer = findViewById(R.id.llRequirementsContainer)
        
        tvRequirementLength = findViewById(R.id.tvRequirementLength)
        tvRequirementAlphabet = findViewById(R.id.tvRequirementAlphabet)
        tvRequirementNumber = findViewById(R.id.tvRequirementNumber)
        tvRequirementSpecial = findViewById(R.id.tvRequirementSpecial)
        tvRequirementMatch = findViewById(R.id.tvRequirementMatch)

        val ivCurrentPasswordVisibility = findViewById<ImageView>(R.id.ivCurrentPasswordVisibility)
        val ivNewPasswordVisibility = findViewById<ImageView>(R.id.ivNewPasswordVisibility)
        val ivConfirmPasswordVisibility = findViewById<ImageView>(R.id.ivConfirmPasswordVisibility)

        val switch2FA = findViewById<SwitchMaterial>(R.id.switch2FA)
        val switchBiometric = findViewById<SwitchMaterial>(R.id.switchBiometric)

        btnBack.setOnClickListener {
            finish()
        }

        // Visibility Toggles
        setupVisibilityToggle(etCurrentPassword, ivCurrentPasswordVisibility)
        setupVisibilityToggle(etNewPassword, ivNewPasswordVisibility)
        setupVisibilityToggle(etConfirmPassword, ivConfirmPasswordVisibility)

        etCurrentPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    llCurrentPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Real-time password validation (New Password)
        etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                
                if (password.isEmpty()) {
                    llRequirementsContainer.visibility = View.GONE
                    llNewPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                    return
                }
                
                llRequirementsContainer.visibility = View.VISIBLE
                
                val isLengthValid = password.length >= 8
                updateRequirementUI(tvRequirementLength, isLengthValid, "8 characters minimum")
                
                val isAlphabetValid = password.any { it.isLetter() }
                updateRequirementUI(tvRequirementAlphabet, isAlphabetValid, "One alphabet letter")
                
                val isNumberValid = password.any { it.isDigit() }
                updateRequirementUI(tvRequirementNumber, isNumberValid, "One number")
                
                val isSpecialValid = password.any { !it.isLetterOrDigit() }
                updateRequirementUI(tvRequirementSpecial, isSpecialValid, "One special character")
                
                val isAllValid = isLengthValid && isAlphabetValid && isNumberValid && isSpecialValid
                if (isAllValid) {
                    llNewPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_success)
                } else {
                    llNewPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                }
                
                if (etConfirmPassword.text.isNotEmpty()) {
                    validatePasswordMatch(password, etConfirmPassword.text.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword = s.toString()
                tvRequirementMatch.visibility = if (confirmPassword.isEmpty()) View.GONE else View.VISIBLE
                validatePasswordMatch(etNewPassword.text.toString(), confirmPassword)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnUpdatePassword.setOnClickListener {
            val current = etCurrentPassword.text.toString()
            val new = etNewPassword.text.toString()
            val confirm = etConfirmPassword.text.toString()

            var isValid = true

            if (current.isEmpty()) {
                llCurrentPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                isValid = false
            }

            if (new.isEmpty()) {
                llNewPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                llRequirementsContainer.visibility = View.VISIBLE
                isValid = false
            }

            if (confirm.isEmpty()) {
                llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                tvRequirementMatch.visibility = View.VISIBLE
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            val isLengthValid = new.length >= 8
            val isAlphabetValid = new.any { it.isLetter() }
            val isNumberValid = new.any { it.isDigit() }
            val isSpecialValid = new.any { !it.isLetterOrDigit() }
            
            if (!isLengthValid || !isAlphabetValid || !isNumberValid || !isSpecialValid || new != confirm) {
                return@setOnClickListener
            }

            showCustomDialog("Success", "Your password has been updated successfully.", R.drawable.ic_check_circle) {
                clearFields()
            }
        }

        // Security Switches Popups
        switch2FA.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showCustomDialog("2FA Activated", "Two-Factor Authentication has been enabled for your account.", R.drawable.ic_lock)
            } else {
                showCustomDialog("2FA Deactivated", "Two-Factor Authentication has been disabled.", R.drawable.ic_lock)
            }
        }

        switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showCustomDialog("Biometric Enabled", "Biometric login has been activated successfully.", R.drawable.ic_person)
            } else {
                showCustomDialog("Biometric Disabled", "Biometric login has been deactivated.", R.drawable.ic_person)
            }
        }
    }

    private fun setupVisibilityToggle(editText: EditText, imageView: ImageView) {
        var isVisible = false
        imageView.setOnClickListener {
            isVisible = !isVisible
            if (isVisible) {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageView.setImageResource(R.drawable.ic_visibility_off)
            } else {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imageView.setImageResource(R.drawable.ic_visibility)
            }
            editText.setSelection(editText.text.length)
        }
    }

    private fun updateRequirementUI(textView: TextView, isValid: Boolean, label: String) {
        if (isValid) {
            textView.text = "✓ $label"
            textView.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            textView.text = "✕ $label"
            textView.setTextColor(Color.parseColor("#FF5252"))
        }
    }

    private fun validatePasswordMatch(password: String, confirm: String) {
        if (confirm.isEmpty()) {
            llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
            tvRequirementMatch.visibility = View.GONE
            return
        }
        
        tvRequirementMatch.visibility = View.VISIBLE
        if (password == confirm) {
            tvRequirementMatch.text = "✓ Passwords Match"
            tvRequirementMatch.setTextColor(Color.parseColor("#4CAF50"))
            llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_success)
        } else {
            tvRequirementMatch.text = "✕ Passwords Match"
            tvRequirementMatch.setTextColor(Color.parseColor("#FF5252"))
            llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
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
        btnNegative.visibility = View.GONE

        btnPositive.setOnClickListener {
            dialog.dismiss()
            onPositiveClick?.invoke()
        }

        dialog.show()
    }

    private fun clearFields() {
        etCurrentPassword.text.clear()
        etNewPassword.text.clear()
        etConfirmPassword.text.clear()
        llRequirementsContainer.visibility = View.GONE
        tvRequirementMatch.visibility = View.GONE
        llCurrentPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
        llNewPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
        llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
    }
}
