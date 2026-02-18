package com.example.renunite

import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
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

        fun sendMessage() {
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

        btnSend.setOnClickListener {
            sendMessage()
        }

        etMessage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun addMessageToChat(text: String, isSent: Boolean, container: LinearLayout) {
        val outerLayout = LinearLayout(this)
        val outerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        outerParams.topMargin = (resources.displayMetrics.density * 16).toInt()
        if (isSent) {
            outerParams.gravity = Gravity.END
        } else {
            outerParams.gravity = Gravity.START
        }
        outerLayout.layoutParams = outerParams
        outerLayout.orientation = LinearLayout.VERTICAL

        val bubbleLayout = LinearLayout(this)
        bubbleLayout.orientation = LinearLayout.VERTICAL
        val hPadding = (resources.displayMetrics.density * 16).toInt()
        val vPadding = (resources.displayMetrics.density * 12).toInt()
        bubbleLayout.setPadding(hPadding, vPadding, hPadding, vPadding)
        
        if (isSent) {
            bubbleLayout.background = ContextCompat.getDrawable(this, R.drawable.bg_message_bubble_sent)
        } else {
            bubbleLayout.background = ContextCompat.getDrawable(this, R.drawable.bg_message_bubble_received)
        }

        val textView = TextView(this)
        textView.text = text
        textView.textSize = 14f
        if (isSent) {
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.brand_blue_dark))
        }
        textView.maxWidth = (resources.displayMetrics.widthPixels * 0.7).toInt()

        val timeView = TextView(this)
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        timeView.text = sdf.format(Date())
        timeView.textSize = 11f
        timeView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = (resources.displayMetrics.density * 4).toInt()
        }
        
        if (isSent) {
            timeView.setTextColor(ContextCompat.getColor(this, R.color.light_blue_50))
        } else {
            timeView.setTextColor(ContextCompat.getColor(this, R.color.text_muted))
        }

        bubbleLayout.addView(textView)
        bubbleLayout.addView(timeView)
        outerLayout.addView(bubbleLayout)
        container.addView(outerLayout)
    }
}
