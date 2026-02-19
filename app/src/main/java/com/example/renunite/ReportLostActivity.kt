package com.example.renunite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class ReportLostActivity : AppCompatActivity() {
    
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
    private var selectedImageUri: Uri? = null
    private lateinit var ivSelectedPhoto: ImageView
    private lateinit var llAddPhotoPlaceholder: LinearLayout
    private lateinit var btnRemovePhotoAttached: ImageButton
    
    private lateinit var cvKeywords: MaterialCardView
    private lateinit var tvKeywordsError: TextView

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as? android.graphics.Bitmap
            if (imageBitmap != null) {
                ivSelectedPhoto.setImageBitmap(imageBitmap)
                updatePhotoVisibility(true)
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            ivSelectedPhoto.setImageURI(uri)
            updatePhotoVisibility(true)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_lost)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCancel = findViewById<TextView>(R.id.btnCancel)
        val btnSubmit = findViewById<AppCompatButton>(R.id.btnSubmit)
        val cardAddPhoto = findViewById<MaterialCardView>(R.id.cardAddPhoto)
        ivSelectedPhoto = findViewById(R.id.ivSelectedPhoto)
        llAddPhotoPlaceholder = findViewById(R.id.llAddPhotoPlaceholder)
        btnRemovePhotoAttached = findViewById(R.id.btnRemovePhotoAttached)
        
        val cvCategory = findViewById<MaterialCardView>(R.id.cvCategory)
        val menu = findViewById<TextInputLayout>(R.id.menu)
        val autoCompleteCategory = findViewById<AutoCompleteTextView>(R.id.autoCompleteCategory)
        val tvCategoryError = findViewById<TextView>(R.id.tvCategoryError)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val cvDescription = findViewById<MaterialCardView>(R.id.cvDescription)
        val tvDescriptionError = findViewById<TextView>(R.id.tvDescriptionError)
        val etKeywords = findViewById<EditText>(R.id.etKeywords)
        cvKeywords = findViewById(R.id.cvKeywords)
        tvKeywordsError = findViewById(R.id.tvKeywordsError)
        val cgKeywords = findViewById<ChipGroup>(R.id.cgKeywords)
        val llChipContainer = findViewById<LinearLayout>(R.id.llChipContainer)
        val tvSuggestedLabel = findViewById<TextView>(R.id.tvSuggestedLabel)

        btnBack.setOnClickListener { finish() }
        btnCancel.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            validateAndSubmit(autoCompleteCategory, tvCategoryError, cvCategory, etDescription, tvDescriptionError, cvDescription)
        }

        cardAddPhoto.setOnClickListener {
            if (ivSelectedPhoto.visibility == View.GONE) {
                showPhotoOptionsDialog()
            }
        }

        btnRemovePhotoAttached.setOnClickListener {
            clearPhoto()
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
            
            // Clear error
            tvCategoryError.visibility = View.GONE
            cvCategory.strokeWidth = 0

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

        etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    tvDescriptionError.visibility = View.GONE
                    cvDescription.strokeColor = Color.parseColor("#DDE5F5")
                    cvDescription.strokeWidth = 0
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

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

    private fun updatePhotoVisibility(isPhotoAttached: Boolean) {
        if (isPhotoAttached) {
            ivSelectedPhoto.visibility = View.VISIBLE
            btnRemovePhotoAttached.visibility = View.VISIBLE
            llAddPhotoPlaceholder.visibility = View.GONE
        } else {
            ivSelectedPhoto.visibility = View.GONE
            btnRemovePhotoAttached.visibility = View.GONE
            llAddPhotoPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun clearPhoto() {
        selectedImageUri = null
        ivSelectedPhoto.setImageDrawable(null)
        updatePhotoVisibility(false)
    }

    private fun validateAndSubmit(
        category: AutoCompleteTextView, 
        catError: TextView, 
        catCard: MaterialCardView,
        description: EditText, 
        descError: TextView,
        descCard: MaterialCardView
    ) {
        val categoryText = category.text.toString().trim()
        val descriptionText = description.text.toString().trim()
        
        var isValid = true

        if (categoryText.isEmpty()) {
            catError.visibility = View.VISIBLE
            catCard.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics).toInt()
            catCard.strokeColor = Color.parseColor("#FF5252")
            isValid = false
        } else {
            catError.visibility = View.GONE
        }

        if (descriptionText.isEmpty()) {
            descError.visibility = View.VISIBLE
            descCard.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics).toInt()
            descCard.strokeColor = Color.parseColor("#FF5252")
            isValid = false
        } else {
            descError.visibility = View.GONE
        }

        if (addedKeywords.isEmpty()) {
            tvKeywordsError.visibility = View.VISIBLE
            cvKeywords.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, resources.displayMetrics).toInt()
            cvKeywords.strokeColor = Color.parseColor("#FF5252")
            isValid = false
        } else {
            tvKeywordsError.visibility = View.GONE
        }

        if (isValid) {
            val intent = Intent(this, SmartMatchActivity::class.java)
            intent.putExtra("FLOW_TYPE", "LOST")
            intent.putExtra("CATEGORY", categoryText)
            intent.putStringArrayListExtra("KEYWORDS", ArrayList(addedKeywords.toList()))
            startActivity(intent)
            finish()
        }
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
        
        // Hide error when a keyword is added
        tvKeywordsError.visibility = View.GONE
        cvKeywords.strokeColor = Color.parseColor("#DDE5F5")
        cvKeywords.strokeWidth = 0

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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_photo_options_branded, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnTakePhoto = dialogView.findViewById<MaterialButton>(R.id.btnTakePhoto)
        val btnChooseGallery = dialogView.findViewById<MaterialButton>(R.id.btnChooseGallery)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelPhoto)

        btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(intent)
            dialog.dismiss()
        }

        btnChooseGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
