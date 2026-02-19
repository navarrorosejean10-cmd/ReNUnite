package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.chip.ChipGroup

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val itemName = intent.getStringExtra("ITEM_NAME") ?: "Unknown Item"
        val flowType = intent.getStringExtra("FLOW_TYPE")
        val matchScore = intent.getIntExtra("MATCH_SCORE", -1)

        val ivItemImage = findViewById<ImageView>(R.id.ivItemImage)
        val tvItemName = findViewById<TextView>(R.id.tvItemName)
        val tvItemDescription = findViewById<TextView>(R.id.tvItemDescription)
        val tvItemLocation = findViewById<TextView>(R.id.tvItemLocation)
        val tvItemDate = findViewById<TextView>(R.id.tvItemDate)
        val tvMatchBadge = findViewById<TextView>(R.id.tvMatchBadge)
        val cgKeywords = findViewById<ChipGroup>(R.id.cgKeywords)
        val btnAction = findViewById<AppCompatButton>(R.id.btnRequestClaim)

        // Set name and basic info
        tvItemName.text = itemName

        // Show match badge if score is provided
        if (matchScore != -1) {
            tvMatchBadge.visibility = View.VISIBLE
            tvMatchBadge.text = "$matchScore% Match"
        } else {
            tvMatchBadge.visibility = View.GONE
        }

        // Adjust button based on flow
        if ("FOUND".equals(flowType, ignoreCase = true)) {
            btnAction.text = "Confirm Similar Item"
        } else {
            btnAction.text = "Request Claim"
        }

        var itemImageResId = R.drawable.img_backpack // Default
        // Dummy data mapping
        when (itemName) {
            "Blue Student ID" -> {
                itemImageResId = R.drawable.img_blue_id
                ivItemImage.setImageResource(itemImageResId)
                tvItemDescription.text = "NU Dasmarinas student ID with blue lanyard. Name starts with M."
                tvItemLocation.text = "Library 2nd Floor"
                tvItemDate.text = "2025-02-11"
                setKeywords(cgKeywords, listOf("student id", "blue", "lanyard", "id card"))
            }
            "iPhone 13 Pro" -> {
                itemImageResId = R.drawable.img_iphone
                ivItemImage.setImageResource(itemImageResId)
                tvItemDescription.text = "Black iPhone 13 Pro with cracked screen protector."
                tvItemLocation.text = "Canteen"
                tvItemDate.text = "2025-02-10"
                setKeywords(cgKeywords, listOf("iphone", "gadget", "black", "phone"))
            }
            "Red Water Bottle" -> {
                itemImageResId = R.drawable.img_red_bottle
                ivItemImage.setImageResource(itemImageResId)
                tvItemDescription.text = "Insulated red water bottle with NU sticker."
                tvItemLocation.text = "Gym"
                tvItemDate.text = "2025-02-09"
                setKeywords(cgKeywords, listOf("bottle", "red", "water", "nu"))
            }
            "Black Backpack" -> {
                itemImageResId = R.drawable.img_backpack
                ivItemImage.setImageResource(itemImageResId)
                tvItemDescription.text = "Black JanSport backpack with laptop compartment."
                tvItemLocation.text = "Room 402"
                tvItemDate.text = "2025-02-08"
                setKeywords(cgKeywords, listOf("backpack", "black", "bag", "jansport"))
            }
        }

        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnAction.setOnClickListener {
            if ("FOUND".equals(flowType, ignoreCase = true)) {
                val nextIntent = Intent(this, ItemConfirmationActivity::class.java).apply {
                    putExtra("ITEM_NAME", itemName)
                    putExtra("ITEM_DESC", tvItemDescription.text.toString())
                    putExtra("ITEM_LOC", tvItemLocation.text.toString())
                    putExtra("ITEM_IMAGE", itemImageResId)
                }
                startActivity(nextIntent)
                finish() // Closes current activity so back goes to the list or home
            } else {
                val nextIntent = Intent(this, ClaimRequestActivity::class.java).apply {
                    putExtra("ITEM_NAME", itemName)
                }
                startActivity(nextIntent)
            }
        }
    }

    private fun setKeywords(chipGroup: ChipGroup, keywords: List<String>) {
        chipGroup.removeAllViews()
        val inflater = LayoutInflater.from(this)
        keywords.forEach { keyword ->
            val chip = inflater.inflate(R.layout.item_keyword_tag, chipGroup, false) as TextView
            chip.text = keyword
            chipGroup.addView(chip)
        }
    }
}
