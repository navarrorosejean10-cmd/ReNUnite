package com.example.renunite

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        findViewById<Button>(R.id.btnBackHeader).setOnClickListener {
            finish()
        }
    }
}