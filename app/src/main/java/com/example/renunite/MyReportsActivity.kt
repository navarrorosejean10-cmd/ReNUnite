package com.example.renunite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class MyReportsActivity : AppCompatActivity() {

    private val detailsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val cancelledItemName = result.data?.getStringExtra("CANCELLED_ITEM_NAME")
            if (cancelledItemName != null) {
                removeItemFromList(cancelledItemName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reports)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvLostItemsTab = findViewById<TextView>(R.id.tvLostItemsTab)
        val tvFoundItemsTab = findViewById<TextView>(R.id.tvFoundItemsTab)
        val llLostItemsList = findViewById<LinearLayout>(R.id.llLostItemsList)
        val llFoundItemsList = findViewById<LinearLayout>(R.id.llFoundItemsList)

        btnBack.setOnClickListener {
            finish()
        }

        tvLostItemsTab.setOnClickListener {
            updateTabs(true, tvLostItemsTab, tvFoundItemsTab, llLostItemsList, llFoundItemsList)
        }

        tvFoundItemsTab.setOnClickListener {
            updateTabs(false, tvLostItemsTab, tvFoundItemsTab, llLostItemsList, llFoundItemsList)
        }

        // Lost Item Click Listeners
        findViewById<CardView>(R.id.cardLostItem1).setOnClickListener {
            openDetails("Black Wallet", "Leather wallet with credit cards", "Unknown Location", "2024-04-10", "Pending", R.drawable.ic_search, true)
        }

        findViewById<CardView>(R.id.cardLostItem2).setOnClickListener {
            openDetails("iPhone 13", "Blue iPhone 13 with clear case", "Unknown Location", "2024-04-12", "Matching", R.drawable.ic_search, true)
        }

        // Found Item Click Listeners
        findViewById<CardView>(R.id.cardFoundItem1).setOnClickListener {
            openDetails("Blue Backpack", "Navy blue Jansport backpack", "University Library", "2024-04-15", "Verified", R.drawable.ic_backpack, false)
        }
    }

    private fun openDetails(name: String, desc: String, loc: String, date: String, status: String, image: Int, isLost: Boolean) {
        val intent = Intent(this, MyReportDetailsActivity::class.java)
        intent.putExtra("ITEM_NAME", name)
        intent.putExtra("ITEM_DESCRIPTION", desc)
        intent.putExtra("ITEM_LOCATION", loc)
        intent.putExtra("ITEM_DATE", date)
        intent.putExtra("ITEM_STATUS", status)
        intent.putExtra("ITEM_IMAGE", image)
        intent.putExtra("IS_LOST", isLost)
        detailsLauncher.launch(intent)
    }

    private fun removeItemFromList(itemName: String) {
        when (itemName) {
            "Black Wallet" -> findViewById<CardView>(R.id.cardLostItem1).visibility = View.GONE
            "iPhone 13" -> findViewById<CardView>(R.id.cardLostItem2).visibility = View.GONE
            "Blue Backpack" -> findViewById<CardView>(R.id.cardFoundItem1).visibility = View.GONE
        }
        Toast.makeText(this, "$itemName report cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun updateTabs(
        isLost: Boolean,
        lostTab: TextView,
        foundTab: TextView,
        lostList: LinearLayout,
        foundList: LinearLayout
    ) {
        if (isLost) {
            lostTab.setBackgroundResource(R.drawable.input_field_bg)
            lostTab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.brand_blue)
            lostTab.setTextColor(ContextCompat.getColor(this, R.color.white))
            
            foundTab.background = null
            foundTab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))

            lostList.visibility = View.VISIBLE
            foundList.visibility = View.GONE
        } else {
            foundTab.setBackgroundResource(R.drawable.input_field_bg)
            foundTab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.brand_blue)
            foundTab.setTextColor(ContextCompat.getColor(this, R.color.white))
            
            lostTab.background = null
            lostTab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))

            lostList.visibility = View.GONE
            foundList.visibility = View.VISIBLE
        }
    }
}
