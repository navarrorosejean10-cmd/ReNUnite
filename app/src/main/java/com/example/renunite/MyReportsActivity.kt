package com.example.renunite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MyReportsActivity : AppCompatActivity() {

    private lateinit var etSearchReports: EditText
    private lateinit var ivClearSearchReports: ImageView
    private lateinit var tvLostItemsTab: TextView
    private lateinit var tvFoundItemsTab: TextView
    private lateinit var llLostItemsList: LinearLayout
    private lateinit var llFoundItemsList: LinearLayout
    
    private lateinit var cardLostItem2: View
    private lateinit var cardLostItem3: View
    private lateinit var cardFoundItem1: View
    private lateinit var cardFoundItem2: View
    
    private lateinit var statusFilters: List<TextView>
    
    private var currentTabIsLost = true
    private var currentStatusFilter = "All"
    private var searchQuery = ""

    private val cancelledItems = mutableSetOf<String>()

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

        val fromConfirmation = intent.getBooleanExtra("FROM_CONFIRMATION", false)

        // Initialize Views
        etSearchReports = findViewById(R.id.etSearchReports)
        ivClearSearchReports = findViewById(R.id.ivClearSearchReports)
        tvLostItemsTab = findViewById(R.id.tvLostItemsTab)
        tvFoundItemsTab = findViewById(R.id.tvFoundItemsTab)
        llLostItemsList = findViewById(R.id.llLostItemsList)
        llFoundItemsList = findViewById(R.id.llFoundItemsList)
        
        cardLostItem2 = findViewById(R.id.cardLostItem2)
        cardLostItem3 = findViewById(R.id.cardLostItem3)
        cardFoundItem1 = findViewById(R.id.cardFoundItem1)
        cardFoundItem2 = findViewById(R.id.cardFoundItem2)

        val filterAll = findViewById<TextView>(R.id.filterAll)
        val filterPending = findViewById<TextView>(R.id.filterPending)
        val filterMatching = findViewById<TextView>(R.id.filterMatching)
        val filterVerified = findViewById<TextView>(R.id.filterVerified)
        val filterClaimed = findViewById<TextView>(R.id.filterClaimed)
        
        statusFilters = listOf(filterAll, filterPending, filterMatching, filterVerified, filterClaimed)

        val backAction = {
            if (fromConfirmation) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            backAction()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backAction()
            }
        })

        setupTabs()
        setupSearch()
        setupFilters()
        setupItemClicks()
    }

    private fun setupTabs() {
        tvLostItemsTab.setOnClickListener {
            currentTabIsLost = true
            updateTabUI()
            applyAllFilters()
        }

        tvFoundItemsTab.setOnClickListener {
            currentTabIsLost = false
            updateTabUI()
            applyAllFilters()
        }
    }

    private fun updateTabUI() {
        if (currentTabIsLost) {
            tvLostItemsTab.setBackgroundResource(R.drawable.bg_btn_primary)
            tvLostItemsTab.backgroundTintList = null
            tvLostItemsTab.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvLostItemsTab.setTypeface(null, android.graphics.Typeface.BOLD)
            
            tvFoundItemsTab.background = null
            tvFoundItemsTab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))
            tvFoundItemsTab.setTypeface(null, android.graphics.Typeface.NORMAL)

            llLostItemsList.visibility = View.VISIBLE
            llFoundItemsList.visibility = View.GONE
        } else {
            tvFoundItemsTab.setBackgroundResource(R.drawable.bg_btn_primary)
            tvFoundItemsTab.backgroundTintList = null
            tvFoundItemsTab.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFoundItemsTab.setTypeface(null, android.graphics.Typeface.BOLD)
            
            tvLostItemsTab.background = null
            tvLostItemsTab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))
            tvLostItemsTab.setTypeface(null, android.graphics.Typeface.NORMAL)

            llLostItemsList.visibility = View.GONE
            llFoundItemsList.visibility = View.VISIBLE
        }
    }

    private fun setupSearch() {
        etSearchReports.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString().lowercase()
                ivClearSearchReports.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                applyAllFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        ivClearSearchReports.setOnClickListener {
            etSearchReports.text.clear()
        }
    }

    private fun setupFilters() {
        statusFilters.forEach { textView ->
            textView.setOnClickListener {
                currentStatusFilter = textView.text.toString()
                updateFilterUI()
                applyAllFilters()
            }
        }
    }

    private fun updateFilterUI() {
        statusFilters.forEach { textView ->
            if (textView.text.toString() == currentStatusFilter) {
                textView.setBackgroundResource(R.drawable.bg_category_selected)
                textView.setTextColor(ContextCompat.getColor(this, R.color.white))
                textView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                textView.setBackgroundResource(R.drawable.bg_category_unselected)
                textView.setTextColor(ContextCompat.getColor(this, R.color.brand_blue_accent))
                textView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    private fun applyAllFilters() {
        // Lost Item 2: iPhone 13, Status: Matching
        val matchesSearch2 = "iphone 13".contains(searchQuery) || "blue iphone 13 with clear case".contains(searchQuery)
        val matchesStatus2 = currentStatusFilter == "All" || currentStatusFilter == "Matching"
        val isNotCancelled2 = !cancelledItems.contains("iPhone 13")
        cardLostItem2.visibility = if (currentTabIsLost && matchesSearch2 && matchesStatus2 && isNotCancelled2) View.VISIBLE else View.GONE

        // Lost Item 3: Blue Student ID, Status: Pending
        val matchesSearch3 = "blue student id".contains(searchQuery) || "nu dasmariñas student id with blue lanyard".contains(searchQuery)
        val matchesStatus3 = currentStatusFilter == "All" || currentStatusFilter == "Pending"
        val isNotCancelled3 = !cancelledItems.contains("Blue Student ID")
        cardLostItem3.visibility = if (currentTabIsLost && matchesSearch3 && matchesStatus3 && isNotCancelled3) View.VISIBLE else View.GONE

        // Found Item 1: Blue Backpack, Status: Verified
        val matchesSearch4 = "blue backpack".contains(searchQuery) || "navy blue jansport backpack".contains(searchQuery)
        val matchesStatus4 = currentStatusFilter == "All" || currentStatusFilter == "Verified"
        val isNotCancelled4 = !cancelledItems.contains("Blue Backpack")
        cardFoundItem1.visibility = if (!currentTabIsLost && matchesSearch4 && matchesStatus4 && isNotCancelled4) View.VISIBLE else View.GONE

        // Found Item 2: Red Water Bottle, Status: Claimed
        val matchesSearch5 = "red water bottle".contains(searchQuery) || "insulated red water bottle with nu sticker".contains(searchQuery)
        val matchesStatus5 = currentStatusFilter == "All" || currentStatusFilter == "Claimed"
        val isNotCancelled5 = !cancelledItems.contains("Red Water Bottle")
        cardFoundItem2.visibility = if (!currentTabIsLost && matchesSearch5 && matchesStatus5 && isNotCancelled5) View.VISIBLE else View.GONE
    }

    private fun setupItemClicks() {
        cardLostItem2.setOnClickListener {
            openDetails("iPhone 13", "Blue iPhone 13 with clear case", "Unknown Location", "2024-04-12", "Matching", R.drawable.img_iphone, true)
        }

        cardLostItem3.setOnClickListener {
            openDetails("Blue Student ID", "NU Dasmariñas student ID with blue lanyard.", "Unknown Location", "2024-04-14", "Pending", R.drawable.img_blue_id, true)
        }

        cardFoundItem1.setOnClickListener {
            openDetails("Blue Backpack", "Navy blue Jansport backpack", "University Library", "2024-04-15", "Verified", R.drawable.img_backpack, false)
        }

        cardFoundItem2.setOnClickListener {
            openDetails("Red Water Bottle", "Insulated red water bottle with NU sticker.", "Canteen", "2024-04-16", "Claimed", R.drawable.img_red_bottle, false)
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
        cancelledItems.add(itemName)
        applyAllFilters()
        Toast.makeText(this, "$itemName report cancelled", Toast.LENGTH_SHORT).show()
    }
}
