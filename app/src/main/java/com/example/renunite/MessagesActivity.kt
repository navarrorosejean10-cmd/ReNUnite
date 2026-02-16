package com.example.renunite

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageView>(R.id.btnSend)
        val llChatContainer = findViewById<LinearLayout>(R.id.llChatContainer)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                addMessageToChat(messageText, true, llChatContainer)
                etMessage.text.clear()

                // Auto scroll to bottom
                scrollView.post {
                    scrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    private fun addMessageToChat(text: String, isSent: Boolean, container: LinearLayout) {
        val messageLayout = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (resources.displayMetrics.density * 16).toInt()

        if (isSent) {
            params.gravity = Gravity.END
        } else {
            params.gravity = Gravity.START
        }
        messageLayout.layoutParams = params
        messageLayout.orientation = LinearLayout.VERTICAL

        val textView = TextView(this)
        textView.text = text
        textView.textSize = 14f
        textView.setPadding(
            (resources.displayMetrics.density * 16).toInt(),
            (resources.displayMetrics.density * 12).toInt(),
            (resources.displayMetrics.density * 16).toInt(),
            (resources.displayMetrics.density * 12).toInt()
        )

        textView.background = ContextCompat.getDrawable(this, R.drawable.input_field_bg)
        if (isSent) {
            textView.backgroundTintList = ContextCompat.getColorStateList(this, R.color.brand_blue)
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            textView.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_blue_50)
            textView.setTextColor(ContextCompat.getColor(this, R.color.brand_blue_dark))
        }

        // Max width for messages
        textView.maxWidth = (resources.displayMetrics.widthPixels * 0.7).toInt()

        val timeView = TextView(this)
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        timeView.text = sdf.format(Date())
        timeView.textSize = 11f
        timeView.setTextColor(ContextCompat.getColor(this, R.color.text_muted))

        val timeParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        timeParams.topMargin = (resources.displayMetrics.density * 4).toInt()
        if (isSent) {
            timeParams.gravity = Gravity.END
        }
        timeView.layoutParams = timeParams

        messageLayout.addView(textView)
        messageLayout.addView(timeView)
        container.addView(messageLayout)
    }
}
