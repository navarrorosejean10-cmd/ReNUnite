package com.example.renunite

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MyReportDetailsActivity : AppCompatActivity() {
    private var itemName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_report_details)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvItemName = findViewById<TextView>(R.id.tvItemName)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvLocation = findViewById<TextView>(R.id.tvLocation)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvStatusBadge = findViewById<TextView>(R.id.tvStatusBadge)
        val ivItemImage = findViewById<ImageView>(R.id.ivItemImage)
        val tvCurrentStatusLabel = findViewById<TextView>(R.id.tvCurrentStatusLabel)
        val btnCancelReport = findViewById<MaterialButton>(R.id.btnCancelReport)
        val vStatusIndicator = findViewById<View>(R.id.vStatusIndicator)

        btnBack.setOnClickListener {
            finish()
        }

        // Get data from intent
        itemName = intent.getStringExtra("ITEM_NAME") ?: "Item Name"
        val description = intent.getStringExtra("ITEM_DESCRIPTION") ?: "Description"
        val location = intent.getStringExtra("ITEM_LOCATION") ?: "Unknown Location"
        val date = intent.getStringExtra("ITEM_DATE") ?: "Unknown Date"
        val status = intent.getStringExtra("ITEM_STATUS") ?: "Pending"
        val imageRes = intent.getIntExtra("ITEM_IMAGE", R.drawable.ic_search)
        val isLost = intent.getBooleanExtra("IS_LOST", true)

        // Set data
        tvItemName.text = itemName
        tvDescription.text = description
        tvLocation.text = location
        tvDate.text = date
        tvStatusBadge.text = status
        ivItemImage.setImageResource(imageRes)
        
        // Setup status-specific UI
        when (status.lowercase()) {
            "pending" -> {
                tvStatusBadge.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_pending_bg))
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_pending_text))
                tvCurrentStatusLabel.text = "Pending Verification"
                vStatusIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_pending_text))
            }
            "matching" -> {
                tvStatusBadge.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_matching_bg))
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_matching_text))
                tvCurrentStatusLabel.text = "Potential Match Found"
                vStatusIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_matching_text))
            }
            "verified" -> {
                tvStatusBadge.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_verified_bg))
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_verified_text))
                tvCurrentStatusLabel.text = "Item Verified"
                vStatusIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_verified_text))
            }
            "claimed" -> {
                // Style for archived/claimed items
                tvStatusBadge.text = "Claimed"
                tvStatusBadge.setBackgroundResource(R.drawable.bg_verified_badge)
                tvStatusBadge.backgroundTintList = null // Use the gradient from drawable
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.white))
                // Removed the circled check icon as requested
                tvStatusBadge.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                
                tvCurrentStatusLabel.text = "Item Claimed & Archived"
                // Set status timeline circle color to green as requested
                vStatusIndicator.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_verified_text))
            }
        }

        // Hide cancel button for found items or archived items
        btnCancelReport.visibility = if (isLost && status.lowercase() != "claimed") View.VISIBLE else View.GONE

        btnCancelReport.setOnClickListener {
            showCancelConfirmationDialog()
        }
    }

    private fun showCancelConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_cancel, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConfirmCancel = dialogView.findViewById<MaterialButton>(R.id.btnConfirmCancel)
        val btnKeepReport = dialogView.findViewById<MaterialButton>(R.id.btnKeepReport)

        btnConfirmCancel.setOnClickListener {
            // Return result to MyReportsActivity to remove the item
            val resultIntent = intent
            resultIntent.putExtra("CANCELLED_ITEM_NAME", itemName)
            setResult(Activity.RESULT_OK, resultIntent)
            dialog.dismiss()
            finish()
        }

        btnKeepReport.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
