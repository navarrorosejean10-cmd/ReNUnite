package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SmartMatchActivity : AppCompatActivity() {

    data class DummyItem(
        val name: String,
        val subtitle: String,
        val date: String,
        val location: String,
        val category: String,
        val keywords: List<String>,
        val imageRes: Int
    )

    private val dummyDatabase = listOf(
        DummyItem("Blue Student ID", "NU Dasmariñas student ID", "Feb 11, 2025", "Library, 2nd", "IDs", listOf("blue", "id", "card", "student"), R.drawable.img_blue_id),
        DummyItem("iPhone 13 Pro", "Black with cracked screen", "Feb 10, 2025", "Canteen", "Gadgets", listOf("iphone", "apple", "black", "phone"), R.drawable.img_iphone),
        DummyItem("Red Water Bottle", "Insulated with NU sticker", "Feb 09, 2025", "Gym", "Others", listOf("red", "bottle", "water", "nu"), R.drawable.img_red_bottle),
        DummyItem("Black Backpack", "JanSport with laptop pocket", "Feb 08, 2025", "Room 402", "Bags", listOf("black", "backpack", "bag", "jansport"), R.drawable.img_backpack)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_match)

        val reportedCategory = intent.getStringExtra("CATEGORY") ?: ""
        val reportedKeywords = intent.getStringArrayListExtra("KEYWORDS") ?: arrayListOf()
        val flowType = intent.getStringExtra("FLOW_TYPE") ?: "LOST"

        // Handle close button
        findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
            finish()
        }

        // Find the summary image view
        val ivSummaryIcon = findViewById<ImageView>(R.id.ivSummaryIcon)

        // Logic for category icons
        val categoryIcon = when (reportedCategory.lowercase()) {
            "gadgets" -> R.drawable.img_yellow
            "ids" -> R.drawable.img_red
            "personal items" -> R.drawable.img_blue
            else -> R.drawable.img_green
        }
        
        ivSummaryIcon?.setImageResource(categoryIcon)

        val llMatchContainer = findViewById<LinearLayout>(R.id.llMatchContainer)
        val tvMatchCount = findViewById<TextView>(R.id.tvMatchCount)
        val tvMatchStatus = findViewById<TextView>(R.id.tvMatchStatus)
        val tvMatchDescription = findViewById<TextView>(R.id.tvMatchDescription)

        // Calculate matches
        val matches = dummyDatabase.map { item ->
            var score = 0
            if (item.category.equals(reportedCategory, ignoreCase = true)) score += 40
            
            val keywordMatch = item.keywords.count { keyword ->
                reportedKeywords.any { it.equals(keyword, ignoreCase = true) }
            }
            score += (keywordMatch * 15)
            
            score = score.coerceAtMost(98) // Cap at 98% for dummy logic
            Pair(item, score)
        }.filter { it.second > 20 } // Only show matches above 20%
         .sortedByDescending { it.second }

        tvMatchCount.text = "${matches.size} Potential"
        
        if (matches.isEmpty()) {
            tvMatchStatus.text = "No Matches Found"
            tvMatchDescription.text = "Based on your report, there are no items that might match what you lost"
        } else {
            tvMatchStatus.text = "Matches Found!"
            tvMatchDescription.text = "Based on your report, here are items\nthat might match what you found:"
            
            matches.forEach { (item, score) ->
                addMatchCard(llMatchContainer, item, score, flowType)
            }
        }
    }

    private fun addMatchCard(container: LinearLayout, item: DummyItem, score: Int, flowType: String) {
        val card = LayoutInflater.from(this).inflate(R.layout.item_smart_match, container, false)
        
        card.findViewById<TextView>(R.id.tvMatchName).text = item.name
        card.findViewById<TextView>(R.id.tvMatchSubtitle).text = item.subtitle
        card.findViewById<TextView>(R.id.tvMatchDate).text = "Found ${item.date}"
        card.findViewById<TextView>(R.id.tvMatchLocation).text = item.location
        card.findViewById<ImageView>(R.id.ivMatchImage).setImageResource(item.imageRes)
        
        val tvPercent = card.findViewById<TextView>(R.id.tvMatchPercentage)
        tvPercent.text = "${score}%"
        
        val vStatus = card.findViewById<View>(R.id.vMatchStatusCircle)
        val color = when {
            score >= 90 -> "#FFD700" // Gold
            score >= 70 -> "#F9B33C" // Orange
            else -> "#FF8C00" // Dark Orange
        }
        vStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(color))

        card.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_NAME", item.name)
            intent.putExtra("FLOW_TYPE", flowType)
            intent.putExtra("MATCH_SCORE", score)
            startActivity(intent)
        }

        container.addView(card)
    }
}
