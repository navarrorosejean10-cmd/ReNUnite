package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ItemDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val flowType = intent.getStringExtra("FLOW_TYPE")
        val btnAction = findViewById<MaterialButton>(R.id.btnRequestClaim)

        if (flowType == "FOUND") {
            btnAction.text = "Confirm Similar Item"
        }

        // Updated to ImageButton to match the new layout
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnAction.setOnClickListener {
            if (flowType == "FOUND") {
                val intent = Intent(this, ItemConfirmationActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ClaimRequestActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
