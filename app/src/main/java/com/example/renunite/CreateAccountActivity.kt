package com.example.renunite

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
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
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivPasswordVisibility = findViewById<ImageView>(R.id.ivPasswordVisibility)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val ivConfirmPasswordVisibility = findViewById<ImageView>(R.id.ivConfirmPasswordVisibility)
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

            if (firstName.isEmpty()) {
                showCustomDialog("Missing Details", "Please provide your first name.", R.drawable.ic_person)
                return@setOnClickListener
            }
            if (lastName.isEmpty()) {
                showCustomDialog("Missing Details", "Please provide your last name.", R.drawable.ic_person)
                return@setOnClickListener
            }
            if (studentId.isEmpty()) {
                showCustomDialog("Missing Details", "Please provide your NU student ID.", R.drawable.ic_id_card)
                return@setOnClickListener
            }
            if (department.isEmpty()) {
                showCustomDialog("Missing Details", "Please select your department.", R.drawable.ic_department)
                return@setOnClickListener
            }
            if (program.isEmpty()) {
                showCustomDialog("Missing Details", "Please select your program.", R.drawable.ic_program)
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                showCustomDialog("Missing Details", "Please provide your NU email.", R.drawable.ic_email)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                showCustomDialog("Insecure Password", "Please provide a password.", R.drawable.ic_lock)
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                showCustomDialog("Passwords Don't Match", "Please make sure your passwords match.", R.drawable.ic_lock)
                return@setOnClickListener
            }
            if (!cbTerms.isChecked) {
                showCustomDialog("Terms & Conditions", "Please accept the Terms & Conditions and Privacy Policy to proceed.", R.drawable.ic_check_circle)
                return@setOnClickListener
            }

            // Handle account creation logic
            showCustomDialog("Success", "Account created successfully", R.drawable.ic_check_circle) {
                finish()
            }
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
        showCustomDialog(
            "Terms & Conditions",
            "By using ReNUnite, you agree to: \n\n1. Use the app for legitimate lost and found reporting.\n2. Not post false or misleading information.\n3. Respect other users' privacy.\n4. Abide by NU Dasmariñas student code of conduct.",
            R.drawable.ic_program
        )
    }

    private fun showPrivacyDialog() {
        showCustomDialog(
            "Privacy Policy",
            "ReNUnite collects your NU student ID, email, and name to facilitate item recovery. We do not share your data with third parties. Your data is stored securely and used solely for campus-related item matching.",
            R.drawable.ic_email
        )
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
        val programs = if (selectedDepartment != null) {
            programsMap[selectedDepartment] ?: arrayOf()
        } else {
            arrayOf()
        }

        val programAdapter = ArrayAdapter(this, R.layout.dropdown_item, programs)
        actProgram.setAdapter(programAdapter)
        
        actProgram.setOnItemClickListener { parent, _, position, _ ->
            selectedProgram = parent.getItemAtPosition(position).toString()
        }
    }
}