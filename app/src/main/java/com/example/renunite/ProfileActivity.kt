package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val etStudentId = findViewById<EditText>(R.id.etStudentId)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnEditPhoto = findViewById<CardView>(R.id.btnEditPhoto)
        val btnSecurityPassword = findViewById<LinearLayout>(R.id.btnSecurityPassword)
        val btnAppSettings = findViewById<LinearLayout>(R.id.btnAppSettings)

        // Disable editing for Student ID and Email
        etStudentId.isEnabled = false
        etEmail.isEnabled = false

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnEditPhoto.setOnClickListener {
            showChangePhotoDialog()
        }

        btnSecurityPassword.setOnClickListener {
            val intent = Intent(this, SecurityPasswordActivity::class.java)
            startActivity(intent)
        }

        btnAppSettings.setOnClickListener {
            // Debug Toast to confirm click is registered
            Toast.makeText(this, "Opening App Settings...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AppSettingsActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.navHome).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        findViewById<View>(R.id.navNotifications).setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.navMessages).setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showChangePhotoDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Remove Photo")
        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setTitle("Change Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Camera coming soon", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Gallery coming soon", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
