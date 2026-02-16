package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SmartMatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_match)

        val flowType = intent.getStringExtra("FLOW_TYPE")

        findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
            finish()
        }

        val cardMatch1 = findViewById<CardView>(R.id.cardMatch1)
        cardMatch1.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            intent.putExtra("FLOW_TYPE", flowType)
            startActivity(intent)
        }

        val cardMatch2 = findViewById<CardView>(R.id.cardMatch2)
        cardMatch2.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            intent.putExtra("FLOW_TYPE", flowType)
            startActivity(intent)
        }

        val cardMatch3 = findViewById<CardView>(R.id.cardMatch3)
        cardMatch3.setOnClickListener {
            val intent = Intent(this, ItemDetailsActivity::class.java)
            intent.putExtra("FLOW_TYPE", flowType)
            startActivity(intent)
        }
    }
}
