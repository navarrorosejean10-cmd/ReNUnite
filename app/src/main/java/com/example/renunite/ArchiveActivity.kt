package com.example.renunite

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ArchiveActivity : AppCompatActivity() {

    private var cardArchived1: View? = null
    private var cardArchived2: View? = null
    private var cardArchived3: View? = null
    private lateinit var autoCompleteFilter: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        cardArchived1 = findViewById(R.id.cardArchived1)
        cardArchived2 = findViewById(R.id.cardArchived2)
        cardArchived3 = findViewById(R.id.cardArchived3)
        autoCompleteFilter = findViewById(R.id.autoCompleteFilter)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupFilterDropdown()
    }

    private fun setupFilterDropdown() {
        val options = arrayOf("All Items", "Claimed", "Unclaimed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        autoCompleteFilter.setAdapter(adapter)

        autoCompleteFilter.setOnItemClickListener { _, _, position, _ ->
            val selected = options[position]
            applyFilter(selected)
        }
    }

    private fun applyFilter(status: String) {
        when (status) {
            "All Items" -> {
                cardArchived1?.visibility = View.VISIBLE
                cardArchived2?.visibility = View.VISIBLE
                cardArchived3?.visibility = View.VISIBLE
            }
            "Claimed" -> {
                cardArchived1?.visibility = View.VISIBLE
                cardArchived2?.visibility = View.VISIBLE
                cardArchived3?.visibility = View.GONE
            }
            "Unclaimed" -> {
                cardArchived1?.visibility = View.GONE
                cardArchived2?.visibility = View.GONE
                cardArchived3?.visibility = View.VISIBLE
            }
        }
    }
}
