package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class ItemConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_confirmation)

        val itemName = intent.getStringExtra("ITEM_NAME") ?: "Item"
        val itemDesc = intent.getStringExtra("ITEM_DESC") ?: ""
        val itemLoc = intent.getStringExtra("ITEM_LOC") ?: ""
        val itemImage = intent.getIntExtra("ITEM_IMAGE", R.drawable.img_backpack)

        findViewById<TextView>(R.id.tvConfirmedItemName).text = "$itemName Found!"
        findViewById<TextView>(R.id.tvConfirmedItemDesc).text = itemDesc
        findViewById<TextView>(R.id.tvConfirmedItemLoc).text = itemLoc
        findViewById<ImageView>(R.id.ivConfirmedItemImage).setImageResource(itemImage)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        findViewById<AppCompatButton>(R.id.btnBackToHome).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        findViewById<AppCompatButton>(R.id.btnViewReports).setOnClickListener {
            val intent = Intent(this, MyReportsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
