package com.example.renunite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesFragment : Fragment() {

    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageView
    private lateinit var llMessagesContainer: LinearLayout
    private lateinit var svChat: ScrollView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)

        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)
        llMessagesContainer = view.findViewById(R.id.llChatContainer)
        svChat = view.findViewById(R.id.scrollView)

        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                addMessageToChat(messageText)
                etMessage.text.clear()
                
                // Auto-scroll to bottom
                svChat.post {
                    svChat.fullScroll(View.FOCUS_DOWN)
                }
            }
        }

        return view
    }

    private fun addMessageToChat(text: String) {
        val messageView = LayoutInflater.from(requireContext()).inflate(R.layout.item_message_sent, llMessagesContainer, false)
        val tvMessage = messageView.findViewById<TextView>(R.id.tvMessageSent)
        val tvTime = messageView.findViewById<TextView>(R.id.tvTimeSent)

        tvMessage.text = text
        tvTime.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

        llMessagesContainer.addView(messageView)
    }
}
