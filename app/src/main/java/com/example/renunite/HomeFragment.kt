package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView
    private lateinit var item1: CardView
    private lateinit var item2: CardView
    private lateinit var item3: CardView
    private lateinit var item4: CardView
    private lateinit var filters: List<TextView>
    
    private var currentFilter = "All"
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        etSearch = view.findViewById(R.id.etSearch)
        ivClearSearch = view.findViewById(R.id.ivClearSearch)
        item1 = view.findViewById(R.id.item1)
        item2 = view.findViewById(R.id.item2)
        item3 = view.findViewById(R.id.item3)
        item4 = view.findViewById(R.id.item4)

        val filterAll = view.findViewById<TextView>(R.id.filterAll)
        val filterIDs = view.findViewById<TextView>(R.id.filterIDs)
        val filterGadgets = view.findViewById<TextView>(R.id.filterGadgets)
        val filterPersonal = view.findViewById<TextView>(R.id.filterPersonal)
        val filterBags = view.findViewById<TextView>(R.id.filterBags)
        val filterOthers = view.findViewById<TextView>(R.id.filterOthers)

        filters = listOf(filterAll, filterIDs, filterGadgets, filterPersonal, filterBags, filterOthers)

        setupFilters()
        setupSearch()

        // Set up click listeners for quick actions using ImageView
        view.findViewById<ImageView>(R.id.btnReportLost).setOnClickListener {
            startActivity(Intent(requireContext(), ReportLostActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.btnReportFound).setOnClickListener {
            startActivity(Intent(requireContext(), ReportFoundActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.btnMyReports).setOnClickListener {
            startActivity(Intent(requireContext(), MyReportsActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.btnArchive).setOnClickListener {
            startActivity(Intent(requireContext(), ArchiveActivity::class.java))
        }

        // Set up click listeners for items
        item1.setOnClickListener {
            val intent = Intent(requireContext(), ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_NAME", "Blue Student ID")
            startActivity(intent)
        }

        item2.setOnClickListener {
            val intent = Intent(requireContext(), ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_NAME", "iPhone 13 Pro")
            startActivity(intent)
        }

        item3.setOnClickListener {
            val intent = Intent(requireContext(), ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_NAME", "Red Water Bottle")
            startActivity(intent)
        }

        item4.setOnClickListener {
            val intent = Intent(requireContext(), ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_NAME", "Black Backpack")
            startActivity(intent)
        }

        ivClearSearch.setOnClickListener {
            etSearch.text.clear()
        }

        return view
    }

    private fun setupFilters() {
        filters.forEach { textView ->
            textView.setOnClickListener {
                currentFilter = textView.text.toString()
                updateFilterUI()
                applyFilters()
            }
        }
    }

    private fun updateFilterUI() {
        filters.forEach { textView ->
            if (textView.text.toString() == currentFilter) {
                textView.setBackgroundResource(R.drawable.bg_category_selected)
                textView.backgroundTintList = null
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                textView.setBackgroundResource(R.drawable.bg_category_unselected)
                textView.backgroundTintList = null
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.brand_blue_accent))
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString().lowercase()
                ivClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun applyFilters() {
        // Item 1: Blue Student ID, Category: IDs
        val matchesSearch1 = "blue student id".contains(searchQuery) || "nu dasmariñas student id with blue lanyard. name starts with m.".contains(searchQuery)
        val matchesFilter1 = currentFilter == "All" || currentFilter == "IDs"
        item1.visibility = if (matchesSearch1 && matchesFilter1) View.VISIBLE else View.GONE

        // Item 2: iPhone 13 Pro, Category: Gadgets
        val matchesSearch2 = "iphone 13 pro".contains(searchQuery) || "black iphone 13 pro with cracked screen protector".contains(searchQuery)
        val matchesFilter2 = currentFilter == "All" || currentFilter == "Gadgets"
        item2.visibility = if (matchesSearch2 && matchesFilter2) View.VISIBLE else View.GONE

        // Item 3: Red Water Bottle, Category: School AND Personal Items
        val matchesSearch3 = "red water bottle".contains(searchQuery) || "insulated red water bottle with nu sticker".contains(searchQuery)
        val matchesFilter3 = currentFilter == "All" || currentFilter == "School" || currentFilter == "Personal Items"
        item3.visibility = if (matchesSearch3 && matchesFilter3) View.VISIBLE else View.GONE

        // Item 4: Black Backpack, Category: Bags
        val matchesSearch4 = "black backpack".contains(searchQuery) || "black jansport backpack with laptop compartment".contains(searchQuery)
        val matchesFilter4 = currentFilter == "All" || currentFilter == "Bags"
        item4.visibility = if (matchesSearch4 && matchesFilter4) View.VISIBLE else View.GONE
    }
}
