package com.example.renunite

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class ScheduleAppointmentActivity : AppCompatActivity() {

    private var selectedDateChip: TextView? = null
    private var selectedTimeChip: TextView? = null
    private lateinit var tvDateError: TextView
    private lateinit var tvTimeError: TextView
    private lateinit var llDateContainer: LinearLayout
    private lateinit var llTimeContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_appointment)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCancel = findViewById<MaterialButton>(R.id.btnCancel)
        val btnConfirm = findViewById<MaterialButton>(R.id.btnConfirm)
        val glDates = findViewById<GridLayout>(R.id.glDates)
        val glTimes = findViewById<GridLayout>(R.id.glTimes)
        tvDateError = findViewById(R.id.tvDateError)
        tvTimeError = findViewById(R.id.tvTimeError)
        llDateContainer = findViewById(R.id.llDateContainer)
        llTimeContainer = findViewById(R.id.llTimeContainer)

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
        var isValid = true
        
        if (selectedDateChip == null) {
            tvDateError.visibility = View.VISIBLE
            llDateContainer.setBackgroundResource(R.drawable.bg_smart_item_error)
            isValid = false
        } else {
            tvDateError.visibility = View.GONE
            llDateContainer.setBackgroundResource(R.drawable.bg_smart_item)
        }
        
        if (selectedTimeChip == null) {
            tvTimeError.visibility = View.VISIBLE
            llTimeContainer.setBackgroundResource(R.drawable.bg_smart_item_error)
            isValid = false
        } else {
            tvTimeError.visibility = View.GONE
            llTimeContainer.setBackgroundResource(R.drawable.bg_smart_item)
        }

        if (isValid) {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
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

        ivIcon.setImageResource(R.drawable.ic_calendar)
        tvTitle.text = "Confirm Appointment"
        tvMessage.text = "Are you sure you want to schedule your appointment for ${selectedDateChip?.text} at ${selectedTimeChip?.text}?"
        btnPositive.text = "Confirm"
        btnNegative.text = "Cancel"
        
        btnNegative.visibility = View.VISIBLE

        btnPositive.setOnClickListener {
            dialog.dismiss()
            showSuccessDialog()
        }
        
        btnNegative.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSuccessDialog() {
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

        ivIcon.setImageResource(R.drawable.ic_check_circle)
        tvTitle.text = "Appointment Saved!"
        tvMessage.text = "Your appointment has been successfully scheduled. Please arrive on time with your valid ID."
        btnPositive.text = "View Details"
        
        btnNegative.visibility = View.GONE

        btnPositive.setOnClickListener {
            dialog.dismiss()
            navigateToConfirmation()
        }

        dialog.show()
    }

    private fun navigateToConfirmation() {
        val intent = Intent(this, AppointmentConfirmedActivity::class.java)
        intent.putExtra("SELECTED_DATE", selectedDateChip?.text.toString())
        intent.putExtra("SELECTED_TIME", selectedTimeChip?.text.toString())
        startActivity(intent)
        finish()
    }

    private fun handleDateSelection(chip: TextView) {
        selectedDateChip?.let {
            it.setBackgroundResource(R.drawable.input_field_bg)
            it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_light)
            it.setTextColor(ContextCompat.getColor(this, R.color.brand_primary))
        }
        
        selectedDateChip = chip
        chip.setBackgroundResource(R.drawable.bg_chip_selected)
        chip.backgroundTintList = null 
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        
        tvDateError.visibility = View.GONE
        llDateContainer.setBackgroundResource(R.drawable.bg_smart_item)
    }

    private fun handleTimeSelection(chip: TextView) {
        selectedTimeChip?.let {
            it.setBackgroundResource(R.drawable.input_field_bg)
            it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_light)
            it.setTextColor(ContextCompat.getColor(this, R.color.brand_primary))
        }
        
        selectedTimeChip = chip
        chip.setBackgroundResource(R.drawable.bg_chip_selected)
        chip.backgroundTintList = null 
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        
        tvTimeError.visibility = View.GONE
        llTimeContainer.setBackgroundResource(R.drawable.bg_smart_item)
    }
}
