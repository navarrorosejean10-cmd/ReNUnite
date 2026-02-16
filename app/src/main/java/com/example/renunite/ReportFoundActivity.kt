package com.example.renunite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Filter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class ReportFoundActivity : AppCompatActivity() {

    private val allKeywords = listOf(
        "Blue", "Wallet", "Leather", "Black", "iPhone", "Keys", "Backpack", "Watch", "ID Card", "Samsung",
        "Charger", "Glasses", "Notebook", "Pen", "Calculator", "Water Bottle", "Umbrella", "Jacket", "Cap",
        "Shoes", "Earphones", "Laptop", "Tablet", "Flash Drive", "Documents", "Files", "Money", "Cash",
        "Card", "ATM Card", "Coins", "Bag", "Lunch Box", "Towel", "Perfume", "Comb", "Mirror", "Makeup",
        "Jewelry", "Ring", "Earrings", "Necklace", "Bracelet", "Silver", "Gold", "Pink", "Red", "Green",
        "Yellow", "Orange", "Purple", "White", "Grey", "Brown", "Navy", "Sticker", "Logo", "Case", "Cover",
        "Screen Protector", "AirPods", "Headphones", "Powerbank", "Passport", "License", "Permit", "Vape",
        "Lighter", "Keypad", "Android", "iOS", "Apple", "Huawei", "Oppo", "Vivo", "Xiaomi", "Realme"
    )
    private val addedKeywords = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_found)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCancel = findViewById<TextView>(R.id.btnCancel)
        val btnSubmit = findViewById<MaterialButton>(R.id.btnSubmit)
        val cardAddPhoto = findViewById<CardView>(R.id.cardAddPhoto)
        val menu = findViewById<TextInputLayout>(R.id.menu)
        val autoCompleteCategory = findViewById<AutoCompleteTextView>(R.id.autoCompleteCategory)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etKeywords = findViewById<EditText>(R.id.etKeywords)
        val cgKeywords = findViewById<ChipGroup>(R.id.cgKeywords)
        val llChipContainer = findViewById<LinearLayout>(R.id.llChipContainer)
        val tvSuggestedLabel = findViewById<TextView>(R.id.tvSuggestedLabel)

        btnBack.setOnClickListener { finish() }
        btnCancel.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            validateAndSubmit(autoCompleteCategory, etLocation, etDescription)
        }

        cardAddPhoto.setOnClickListener {
            showPhotoOptionsDialog()
        }

        // Setup Category Dropdown
        val categories = arrayOf("IDs", "Gadgets", "Personal Items", "Others")
        val adapter = object : ArrayAdapter<String>(this, R.layout.dropdown_item, categories) {
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val results = FilterResults()
                        results.values = categories
                        results.count = categories.size
                        return results
                    }
                    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                        notifyDataSetChanged()
                    }
                }
            }
        }
        autoCompleteCategory.setAdapter(adapter)

        autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            val icons = intArrayOf(
                R.drawable.ic_id_card,
                R.drawable.ic_search,
                R.drawable.ic_backpack,
                R.drawable.ic_school
            )
            menu.setStartIconDrawable(icons[position])

            if (selectedCategory == "Others") {
                autoCompleteCategory.inputType = InputType.TYPE_CLASS_TEXT
                autoCompleteCategory.setText("")
                autoCompleteCategory.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(autoCompleteCategory, InputMethodManager.SHOW_IMPLICIT)
            } else {
                autoCompleteCategory.inputType = InputType.TYPE_NULL
                autoCompleteCategory.setText(selectedCategory, false)
            }
        }

        autoCompleteCategory.setOnClickListener {
            autoCompleteCategory.showDropDown()
        }

        // Keyword tagging logic
        etKeywords.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: ""
                if (text.endsWith(",")) {
                    val keyword = text.removeSuffix(",").trim()
                    if (keyword.isNotEmpty()) {
                        addKeywordChip(keyword, cgKeywords, etKeywords)
                    }
                } else {
                    val filtered = allKeywords.filter { 
                        it.contains(text.trim(), ignoreCase = true) && !addedKeywords.contains(it.lowercase()) 
                    }.take(6)
                    updateSuggestions(filtered, etKeywords, cgKeywords, llChipContainer, tvSuggestedLabel)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        etKeywords.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val keyword = etKeywords.text.toString().trim()
                if (keyword.isNotEmpty()) {
                    addKeywordChip(keyword, cgKeywords, etKeywords)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun validateAndSubmit(category: AutoCompleteTextView, location: EditText, description: EditText) {
        val categoryText = category.text.toString().trim()
        val locationText = location.text.toString().trim()
        val descriptionText = description.text.toString().trim()
        
        when {
            categoryText.isEmpty() -> showValidationDialog("Item Category")
            locationText.isEmpty() -> showValidationDialog("Location Found")
            descriptionText.isEmpty() -> showValidationDialog("Description")
            addedKeywords.isEmpty() -> showValidationDialog("Keywords")
            else -> {
                val intent = Intent(this, SmartMatchActivity::class.java)
                intent.putExtra("FLOW_TYPE", "FOUND")
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showValidationDialog(missingField: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Incomplete Report")
            .setMessage("Please provide the $missingField before proceeding.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun addKeywordChip(keyword: String, chipGroup: ChipGroup, editText: EditText) {
        val keywordLower = keyword.lowercase()
        if (addedKeywords.contains(keywordLower)) {
            editText.setText("")
            return
        }

        val chipView = LayoutInflater.from(this).inflate(R.layout.item_keyword_chip, chipGroup, false) as LinearLayout
        val tvName = chipView.findViewById<TextView>(R.id.tvKeywordName)
        val btnRemove = chipView.findViewById<ImageView>(R.id.btnRemoveKeyword)

        tvName.text = keyword
        addedKeywords.add(keywordLower)

        btnRemove.setOnClickListener {
            chipGroup.removeView(chipView)
            addedKeywords.remove(keywordLower)
        }

        chipGroup.addView(chipView)
        editText.setText("")
    }

    private fun updateSuggestions(keywords: List<String>, etKeywords: EditText, chipGroup: ChipGroup, container: LinearLayout, label: TextView) {
        container.removeAllViews()
        
        if (keywords.isEmpty()) {
            label.visibility = View.GONE
            return
        }

        label.visibility = View.VISIBLE
        keywords.forEach { keyword ->
            val chip = layoutInflater.inflate(R.layout.chip_item, container, false) as TextView
            chip.text = keyword
            chip.setOnClickListener {
                addKeywordChip(keyword, chipGroup, etKeywords)
                container.removeAllViews()
                label.visibility = View.GONE
            }
            container.addView(chip)
            
            val params = chip.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 16, 0)
            chip.layoutParams = params
        }
    }

    private fun showPhotoOptionsDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        MaterialAlertDialogBuilder(this)
            .setTitle("Add Photo")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> { /* Handle Take Photo */ }
                    1 -> { /* Handle Gallery */ }
                    2 -> dialog.dismiss()
                }
            }
            .show()
    }
}
