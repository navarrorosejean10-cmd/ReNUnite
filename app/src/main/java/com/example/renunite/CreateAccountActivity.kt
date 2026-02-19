package com.example.renunite

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class CreateAccountActivity : AppCompatActivity() {

    private var selectedDepartment: String? = null
    private var selectedProgram: String? = null

    private val departments = arrayOf(
        "SECA (School of Engineering, Computing, and Architecture)",
        "SBMA (School of Business, Management, and Accounting)",
        "SASE (School of Arts, Sciences, and Education)",
        "SHS (Senior High School)"
    )

    private val programsMap = mapOf(
        "SECA (School of Engineering, Computing, and Architecture)" to arrayOf(
            "BS Architecture",
            "BS Civil Engineering",
            "BS Computer Engineering",
            "BS Computer Science",
            "BS Information Technology"
        ),
        "SBMA (School of Business, Management, and Accounting)" to arrayOf(
            "BS Hospitality Management",
            "BS Tourism Management",
            "BSBA Marketing Management",
            "BSBA Financial Management",
            "BS Managment Accounting",
            "BS Accountancy"
        ),
        "SASE (School of Arts, Sciences, and Education)" to arrayOf(
            "AB Communication",
            "BS Pyschology",
            "Bachelor of Physical Education"
        ),
        "SHS (Senior High School)" to arrayOf(
            "Humanities and Social Sciences (HUMMS)",
            "Science, Technology, Engineering and Mathematics (STEM)",
            "Accountancy, Business, and Managament (ABM)"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val llBack = findViewById<LinearLayout>(R.id.llBack)
        val btnCreateAccount = findViewById<MaterialButton>(R.id.btnCreateAccount)
        val actDepartment = findViewById<AutoCompleteTextView>(R.id.actDepartment)
        val actProgram = findViewById<AutoCompleteTextView>(R.id.actProgram)
        
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        
        val etStudentId = findViewById<EditText>(R.id.etStudentId)
        val llStudentIdContainer = findViewById<LinearLayout>(R.id.llStudentIdContainer)
        val tvStudentIdError = findViewById<TextView>(R.id.tvStudentIdError)
        
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val llEmailContainer = findViewById<LinearLayout>(R.id.llEmailContainer)
        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val llPasswordContainer = findViewById<LinearLayout>(R.id.llPasswordContainer)
        val ivPasswordVisibility = findViewById<ImageView>(R.id.ivPasswordVisibility)
        val llRequirementsContainer = findViewById<LinearLayout>(R.id.llRequirementsContainer)
        
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val llConfirmPasswordContainer = findViewById<LinearLayout>(R.id.llConfirmPasswordContainer)
        val ivConfirmPasswordVisibility = findViewById<ImageView>(R.id.ivConfirmPasswordVisibility)
        
        val tvRequirementLength = findViewById<TextView>(R.id.tvRequirementLength)
        val tvRequirementAlphabet = findViewById<TextView>(R.id.tvRequirementAlphabet)
        val tvRequirementNumber = findViewById<TextView>(R.id.tvRequirementNumber)
        val tvRequirementSpecial = findViewById<TextView>(R.id.tvRequirementSpecial)
        val tvRequirementMatch = findViewById<TextView>(R.id.tvRequirementMatch)
        
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val tvTerms = findViewById<TextView>(R.id.tvTerms)

        llBack.setOnClickListener {
            finish()
        }

        // Set up Department Dropdown
        val departmentAdapter = ArrayAdapter(this, R.layout.dropdown_item, departments)
        actDepartment.setAdapter(departmentAdapter)

        actDepartment.setOnItemClickListener { parent, _, position, _ ->
            selectedDepartment = parent.getItemAtPosition(position).toString()
            findViewById<LinearLayout>(R.id.llDepartmentContainer).setBackgroundResource(R.drawable.input_field_bg_rounded)
            // Clear and update program dropdown
            actProgram.setText("")
            selectedProgram = null
            setupProgramDropdown(actProgram)
        }

        // Initial setup for Program Dropdown
        setupProgramDropdown(actProgram)

        // Password visibility toggle
        var isPasswordVisible = false
        ivPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        var isConfirmPasswordVisible = false
        ivConfirmPasswordVisibility.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivConfirmPasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivConfirmPasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }

        etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    findViewById<LinearLayout>(R.id.llFirstNameContainer).setBackgroundResource(R.drawable.input_field_bg_rounded)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    findViewById<LinearLayout>(R.id.llLastNameContainer).setBackgroundResource(R.drawable.input_field_bg_rounded)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Real-time Student ID validation
        etStudentId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                if (input.isEmpty()) {
                    llStudentIdContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                    tvStudentIdError.visibility = View.GONE
                    return
                }
                
                val regex = "^\\d{4}-\\d{6}\$".toRegex()
                if (input.matches(regex)) {
                    llStudentIdContainer.setBackgroundResource(R.drawable.input_field_bg_success)
                    tvStudentIdError.visibility = View.GONE
                } else {
                    llStudentIdContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvStudentIdError.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Real-time Email validation
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
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
                    tvEmailError.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Real-time password validation
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                
                if (password.isEmpty()) {
                    llRequirementsContainer.visibility = View.GONE
                    llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_rounded)
                    return
                }
                
                llRequirementsContainer.visibility = View.VISIBLE
                
                // Length check (8 characters minimum)
                val isLengthValid = password.length >= 8
                updateRequirementUI(tvRequirementLength, isLengthValid, "8 characters minimum")
                
                // Alphabet check
                val isAlphabetValid = password.any { it.isLetter() }
                updateRequirementUI(tvRequirementAlphabet, isAlphabetValid, "One alphabet letter")
                
                // Number check
                val isNumberValid = password.any { it.isDigit() }
                updateRequirementUI(tvRequirementNumber, isNumberValid, "One number")
                
                // Special character check
                val isSpecialValid = password.any { !it.isLetterOrDigit() }
                updateRequirementUI(tvRequirementSpecial, isSpecialValid, "One special character")
                
                // Update container border
                val isAllValid = isLengthValid && isAlphabetValid && isNumberValid && isSpecialValid
                if (isAllValid) {
                    llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_success)
                } else {
                    llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                }
                
                // Re-validate match if confirm password is not empty
                if (etConfirmPassword.text.isNotEmpty()) {
                    validatePasswordMatch(password, etConfirmPassword.text.toString(), tvRequirementMatch, llConfirmPasswordContainer)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword = s.toString()
                tvRequirementMatch.visibility = if (confirmPassword.isEmpty()) View.GONE else View.VISIBLE
                validatePasswordMatch(etPassword.text.toString(), confirmPassword, tvRequirementMatch, llConfirmPasswordContainer)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        setupTermsAndConditions(tvTerms)

        btnCreateAccount.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val studentId = etStudentId.text.toString().trim()
            val department = actDepartment.text.toString().trim()
            val program = actProgram.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // 1. Check for empty fields first and show a general dialog
            if (firstName.isEmpty() || lastName.isEmpty() || studentId.isEmpty() ||
                department.isEmpty() || program.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {

                // Show general error dialog
                showCustomDialog("Incomplete Details", "Please fill all required fields to proceed.", R.drawable.ic_close)

                // Highlight empty fields with red borders, but keep detailed error messages hidden if they were empty
                if (firstName.isEmpty()) findViewById<LinearLayout>(R.id.llFirstNameContainer).setBackgroundResource(R.drawable.input_field_bg_error)
                if (lastName.isEmpty()) findViewById<LinearLayout>(R.id.llLastNameContainer).setBackgroundResource(R.drawable.input_field_bg_error)
                if (studentId.isEmpty()) {
                    llStudentIdContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvStudentIdError.visibility = View.GONE // Don't show "Invalid format" yet
                }
                if (department.isEmpty()) findViewById<LinearLayout>(R.id.llDepartmentContainer).setBackgroundResource(R.drawable.input_field_bg_error)
                if (program.isEmpty()) findViewById<LinearLayout>(R.id.llProgramContainer).setBackgroundResource(R.drawable.input_field_bg_error)
                if (email.isEmpty()) {
                    llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvEmailError.visibility = View.GONE // Don't show "Please use official email" yet
                }
                if (password.isEmpty()) {
                    llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    llRequirementsContainer.visibility = View.GONE // Don't show requirements yet
                }
                if (confirmPassword.isEmpty()) {
                    llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                    tvRequirementMatch.visibility = View.GONE // Don't show match status yet
                }

                return@setOnClickListener
            }

            // 2. If all fields are filled, perform detailed validation
            var isAllValid = true

            val studentIdRegex = "^\\d{4}-\\d{6}\$".toRegex()
            if (!studentId.matches(studentIdRegex)) {
                llStudentIdContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                tvStudentIdError.visibility = View.VISIBLE
                isAllValid = false
            }

            if (!email.endsWith("@students.nu-dasma.edu.ph")) {
                llEmailContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                tvEmailError.visibility = View.VISIBLE
                isAllValid = false
            }

            // Password complexity check
            val isLengthValid = password.length >= 8
            val isAlphabetValid = password.any { it.isLetter() }
            val isNumberValid = password.any { it.isDigit() }
            val isSpecialValid = password.any { !it.isLetterOrDigit() }
            
            if (!isLengthValid || !isAlphabetValid || !isNumberValid || !isSpecialValid) {
                llPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                llRequirementsContainer.visibility = View.VISIBLE
                isAllValid = false
            }

            if (password != confirmPassword) {
                llConfirmPasswordContainer.setBackgroundResource(R.drawable.input_field_bg_error)
                tvRequirementMatch.visibility = View.VISIBLE
                isAllValid = false
            }

            if (!isAllValid) return@setOnClickListener

            if (!cbTerms.isChecked) {
                showCustomDialog("Terms & Conditions", "Please accept the Terms & Conditions and Privacy Policy to proceed.", R.drawable.ic_close)
                return@setOnClickListener
            }

            // Handle account creation logic
            showCustomDialog("Success", "Account created successfully", R.drawable.ic_check_circle) {
                finish()
            }
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

    private fun validatePasswordMatch(password: String, confirm: String, textView: TextView, container: LinearLayout) {
        if (confirm.isEmpty()) {
            container.setBackgroundResource(R.drawable.input_field_bg_rounded)
            textView.visibility = View.GONE
            return
        }
        
        textView.visibility = View.VISIBLE
        if (password == confirm) {
            textView.text = "✓ Passwords Match"
            textView.setTextColor(Color.parseColor("#4CAF50"))
            container.setBackgroundResource(R.drawable.input_field_bg_success)
        } else {
            textView.text = "✕ Passwords Match"
            textView.setTextColor(Color.parseColor("#FF5252"))
            container.setBackgroundResource(R.drawable.input_field_bg_error)
        }
    }

    private fun setupTermsAndConditions(tvTerms: TextView) {
        val fullText = "I accept Terms & Conditions and Privacy Policy"
        val spannableString = SpannableString(fullText)

        val termsClickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTermsDialog()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val privacyClickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyDialog()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val termsStart = fullText.indexOf("Terms & Conditions")
        val termsEnd = termsStart + "Terms & Conditions".length
        val privacyStart = fullText.indexOf("Privacy Policy")
        val privacyEnd = privacyStart + "Privacy Policy".length

        spannableString.setSpan(termsClickable, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.brand_primary)), termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        
        spannableString.setSpan(privacyClickable, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.brand_primary)), privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvTerms.text = spannableString
        tvTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showTermsDialog() {
        val builder = SpannableStringBuilder()
        
        appendBold(builder, "1. Acceptance of Terms\n")
        builder.append("By accessing or using ReNUnite, you acknowledge that you have read, understood, and agreed to comply with these Terms and Conditions and this User Agreement.\n\n")
        
        appendBold(builder, "2. Purpose of the Platform\n")
        builder.append("ReNUnite is a lost and found platform developed for students of National University Dasmariñas. It provides a secure, efficient, and confidential digital space for students to:\n" +
            "• Report lost items\n" +
            "• Report found items\n" +
            "• Facilitate item recovery\n\n")
        
        appendBold(builder, "3. User Eligibility\n")
        builder.append("ReNUnite is accessible exclusively to currently enrolled NU Dasmariñas students. All users must log in using their official NU email address and Student ID.\n\n")
        
        appendBold(builder, "4. Proper Use of the Platform\n")
        builder.append("Users agree to provide accurate, truthful, and verifiable information during registration and submission. Users must not:\n" +
            "• Submit false or misleading reports\n" +
            "• Use the platform for personal disputes\n" +
            "• Attempt to exploit the system")

        showFormalDialog("Terms and Conditions and User Agreement", builder)
    }

    private fun showPrivacyDialog() {
        val builder = SpannableStringBuilder()
        
        appendBold(builder, "1. Data Collection\n")
        builder.append("ReNUnite strictly adheres to the Data Privacy Act of 2012 (R.A. 10173). We collect only necessary and relevant information for item recovery, including:\n" +
            "• Basic user details (Name, NU Email, Student ID)\n" +
            "• Details of reported lost/found items\n\n")
        
        appendBold(builder, "2. Purpose of Collection\n")
        builder.append("Data is collected to:\n" +
            "• Verify user identity\n" +
            "• Facilitate efficient item matching\n" +
            "• Maintain accountability in item handling\n\n")
        
        appendBold(builder, "3. Data Protection\n")
        builder.append("Your data is stored securely and used solely for campus-related item matching. We do not share your personal information with third parties without your consent.")

        showFormalDialog("Privacy Policy", builder)
    }
    
    private fun appendBold(builder: SpannableStringBuilder, text: String) {
        val start = builder.length
        builder.append(text)
        builder.setSpan(StyleSpan(Typeface.BOLD), start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun showFormalDialog(title: String, message: CharSequence) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_terms_privacy)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btnPositive)

        tvTitle.text = title
        tvMessage.text = message

        ivClose.setOnClickListener { dialog.dismiss() }
        btnPositive.setOnClickListener { dialog.dismiss() }

        dialog.show()
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

    private fun setupProgramDropdown(actProgram: AutoCompleteTextView) {
        val currentDepartment = selectedDepartment
        val programs = if (currentDepartment != null) {
            programsMap[currentDepartment] ?: emptyArray()
        } else {
            emptyArray()
        }

        val programAdapter = ArrayAdapter(this, R.layout.dropdown_item, programs)
        actProgram.setAdapter(programAdapter)

        actProgram.setOnItemClickListener { parent, _, position, _ ->
            selectedProgram = parent.getItemAtPosition(position).toString()
            findViewById<LinearLayout>(R.id.llProgramContainer).setBackgroundResource(R.drawable.input_field_bg_rounded)
        }
    }
}
