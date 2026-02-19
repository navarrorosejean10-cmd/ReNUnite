package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ArchiveActivity : AppCompatActivity() {

    private lateinit var cardArchived1: View
    private lateinit var cardArchived2: View
    private lateinit var cardArchived3: View
    private lateinit var etSearchArchive: EditText
    private lateinit var ivClearSearchArchive: ImageView
    private lateinit var autoCompleteFilter: AutoCompleteTextView
    
    private var currentCategory = "All Categories"
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        cardArchived1 = findViewById(R.id.cardArchived1)
        cardArchived2 = findViewById(R.id.cardArchived2)
        cardArchived3 = findViewById(R.id.cardArchived3)
        etSearchArchive = findViewById(R.id.etSearchArchive)
        ivClearSearchArchive = findViewById(R.id.ivClearSearchArchive)
        autoCompleteFilter = findViewById(R.id.autoCompleteFilter)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupFilterDropdown()
        setupSearch()
        setupItemClicks()
    }

    private fun setupFilterDropdown() {
        val categories = arrayOf("All Categories", "IDs", "Gadgets", "Personal Items", "Bags", "Others")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categories)
        autoCompleteFilter.setAdapter(adapter)

        autoCompleteFilter.setOnItemClickListener { _, _, position, _ ->
            currentCategory = categories[position]
            applyFilters()
        }
    }

    private fun setupSearch() {
        etSearchArchive.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString().lowercase()
                ivClearSearchArchive.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        ivClearSearchArchive.setOnClickListener {
            etSearchArchive.text.clear()
        }
    }

    private fun applyFilters() {
        // Item 1: Student ID - Maria Santos (Category: IDs)
        val name1 = "student id maria santos"
        val desc1 = "found and claimed success"
        val matchesSearch1 = name1.contains(searchQuery) || desc1.contains(searchQuery)
        val matchesCategory1 = currentCategory == "All Categories" || currentCategory == "IDs"
        cardArchived1.visibility = if (matchesSearch1 && matchesCategory1) View.VISIBLE else View.GONE

        // Item 2: Blue Backpack (Category: Bags)
        val name2 = "blue backpack"
        val desc2 = "matched and returned to"
        val matchesSearch2 = name2.contains(searchQuery) || desc2.contains(searchQuery)
        val matchesCategory2 = currentCategory == "All Categories" || currentCategory == "Bags"
        cardArchived2.visibility = if (matchesSearch2 && matchesCategory2) View.VISIBLE else View.GONE

        // Item 3: Red Water Bottle (Category: Personal Items)
        val name3 = "red water bottle"
        val desc3 = "left at discipline office"
        val matchesSearch3 = name3.contains(searchQuery) || desc3.contains(searchQuery)
        val matchesCategory3 = currentCategory == "All Categories" || currentCategory == "Personal Items"
        cardArchived3.visibility = if (matchesSearch3 && matchesCategory3) View.VISIBLE else View.GONE
    }

    private fun setupItemClicks() {
        cardArchived1.setOnClickListener {
            openDetails(
                "Student ID - Maria Santos",
                "NU Dasmariñas Student ID found at the library and successfully returned to its owner Maria Santos.",
                "Library, 2nd Floor",
                "2026-01-15",
                R.drawable.img_blue_id
            )
        }

        cardArchived2.setOnClickListener {
            openDetails(
                "Blue Backpack",
                "Blue JanSport backpack matched via Smart Match and returned to the rightful owner.",
                "Canteen Area",
                "2026-01-10",
                R.drawable.img_backpack
            )
        }

        cardArchived3.setOnClickListener {
            openDetails(
                "Red Water Bottle",
                "Insulated red water bottle left unclaimed at the discipline office after 30 days.",
                "Discipline Office",
                "2025-12-20",
                R.drawable.img_red_bottle
            )
        }
    }

    private fun openDetails(name: String, desc: String, loc: String, date: String, image: Int) {
        val intent = Intent(this, MyReportDetailsActivity::class.java)
        intent.putExtra("ITEM_NAME", name)
        intent.putExtra("ITEM_DESCRIPTION", desc)
        intent.putExtra("ITEM_LOCATION", loc)
        intent.putExtra("ITEM_DATE", date)
        intent.putExtra("ITEM_STATUS", "Claimed") // All archived items show as Claimed in details
        intent.putExtra("ITEM_IMAGE", image)
        intent.putExtra("IS_LOST", false) // Archive items don't show the "Cancel Report" button
        startActivity(intent)
    }
}
