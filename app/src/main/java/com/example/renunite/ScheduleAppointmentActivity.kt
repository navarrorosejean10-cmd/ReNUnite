package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScheduleAppointmentActivity : AppCompatActivity() {

    private var selectedDateChip: TextView? = null
    private var selectedTimeChip: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_appointment)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCancel = findViewById<AppCompatButton>(R.id.btnCancel)
        val btnConfirm = findViewById<AppCompatButton>(R.id.btnConfirm)
        val glDates = findViewById<GridLayout>(R.id.glDates)
        val glTimes = findViewById<GridLayout>(R.id.glTimes)

        btnBack.setOnClickListener { finish() }

        btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnConfirm.setOnClickListener {
            validateAndShowConfirmation()
        }

        // Setup Date Selection
        for (i in 0 until glDates.childCount) {
            val view = glDates.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    handleDateSelection(view)
                }
            }
        }

        // Setup Time Selection
        for (i in 0 until glTimes.childCount) {
            val view = glTimes.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    handleTimeSelection(view)
                }
            }
        }
    }

    private fun validateAndShowConfirmation() {
        when {
            selectedDateChip == null -> showValidationDialog("Date")
            selectedTimeChip == null -> showValidationDialog("Time")
            else -> {
                // Show Confirmation Popup
                MaterialAlertDialogBuilder(this)
                    .setTitle("Confirm Appointment")
                    .setMessage("Are you sure you want to schedule your appointment for ${selectedDateChip?.text} at ${selectedTimeChip?.text}?")
                    .setPositiveButton("Confirm") { _, _ ->
                        navigateToConfirmation()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    private fun navigateToConfirmation() {
        val intent = Intent(this, AppointmentConfirmedActivity::class.java)
        // Pass data to confirmation screen
        intent.putExtra("SELECTED_DATE", selectedDateChip?.text.toString())
        intent.putExtra("SELECTED_TIME", selectedTimeChip?.text.toString())
        startActivity(intent)
        finish()
    }

    private fun showValidationDialog(missingField: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Incomplete Selection")
            .setMessage("Please select a $missingField before proceeding.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun handleDateSelection(chip: TextView) {
        // Reset previous selection
        selectedDateChip?.let {
            it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_blue_50)
            it.setTextColor(ContextCompat.getColor(this, R.color.brand_blue_dark))
        }
        
        // Update new selection
        selectedDateChip = chip
        chip.backgroundTintList = ContextCompat.getColorStateList(this, R.color.brand_blue_dark)
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun handleTimeSelection(chip: TextView) {
        // Reset previous selection
        selectedTimeChip?.let {
            it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_blue_50)
            it.setTextColor(ContextCompat.getColor(this, R.color.brand_blue_dark))
        }
        
        // Update new selection
        selectedTimeChip = chip
        chip.backgroundTintList = ContextCompat.getColorStateList(this, R.color.brand_blue_dark)
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}
