package com.example.renunite

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SecurityPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_password)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnUpdatePassword = findViewById<MaterialButton>(R.id.btnUpdatePassword)
        val etCurrentPassword = findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNewPassword = findViewById<TextInputEditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)

        btnBack.setOnClickListener {
            finish()
        }

        btnUpdatePassword.setOnClickListener {
            val current = etCurrentPassword.text.toString()
            val new = etNewPassword.text.toString()
            val confirm = etConfirmPassword.text.toString()

            if (current.isEmpty() || new.isEmpty() || confirm.isEmpty()) {
                showErrorDialog("Please fill in all password fields.")
            } else if (new != confirm) {
                showErrorDialog("New passwords do not match.")
            } else {
                showSuccessDialog()
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvDialogMessage)
        tvMessage.text = "Your password has been updated successfully."

        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                // Stay on security activity as requested (finish current logic if it was a separate screen, 
                // but here we just clear fields or just stay)
                // The user said "go back to the security activity", but we are already in SecurityPasswordActivity.
                // If they meant go back to Profile, I would call finish(). 
                // Re-reading: "once updated and ok is clicked it will just go back to the security activity"
                // This implies they might be seeing the dialog as a separate state.
                // I'll clear the fields to show it's done.
                clearFields()
            }
            .setCancelable(false)
            .show()
    }

    private fun clearFields() {
        findViewById<TextInputEditText>(R.id.etCurrentPassword).text?.clear()
        findViewById<TextInputEditText>(R.id.etNewPassword).text?.clear()
        findViewById<TextInputEditText>(R.id.etConfirmPassword).text?.clear()
    }
}
