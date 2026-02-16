package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class NotificationsFragment : Fragment() {

    private lateinit var tvUnreadBadge: TextView
    private lateinit var tvBellBadge: TextView
    private lateinit var cardNotif1: CardView
    private lateinit var cardNotif2: CardView
    private lateinit var dot1: View
    private lateinit var dot2: View
    private var unreadCount = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        tvUnreadBadge = view.findViewById(R.id.tvUnreadBadge)
        tvBellBadge = view.findViewById(R.id.tvBellBadge)
        cardNotif1 = view.findViewById(R.id.cardNotif1)
        cardNotif2 = view.findViewById(R.id.cardNotif2)
        dot1 = view.findViewById(R.id.dot1)
        dot2 = view.findViewById(R.id.dot2)

        cardNotif1.setOnClickListener {
            if (dot1.visibility == View.VISIBLE) {
                dot1.visibility = View.GONE
                decrementUnread()
            }
            // Redirect to Smart Match
            val intent = Intent(requireContext(), SmartMatchActivity::class.java)
            intent.putExtra("FLOW_TYPE", "LOST")
            startActivity(intent)
        }

        cardNotif2.setOnClickListener {
            if (dot2.visibility == View.VISIBLE) {
                dot2.visibility = View.GONE
                decrementUnread()
            }
            // Redirect to Schedule Appointment (as the next step after approval)
            val intent = Intent(requireContext(), ScheduleAppointmentActivity::class.java)
            startActivity(intent)
        }

        tvUnreadBadge.setOnClickListener {
            // "Mark all as read" logic
            dot1.visibility = View.GONE
            dot2.visibility = View.GONE
            unreadCount = 0
            updateBadges()
        }

        return view
    }

    private fun decrementUnread() {
        if (unreadCount > 0) {
            unreadCount--
            updateBadges()
        }
    }

    private fun updateBadges() {
        if (unreadCount > 0) {
            tvUnreadBadge.text = "$unreadCount Unread"
            tvBellBadge.text = "$unreadCount"
            tvBellBadge.visibility = View.VISIBLE
        } else {
            tvUnreadBadge.text = "0 Unread"
            tvBellBadge.visibility = View.GONE
        }
    }
}