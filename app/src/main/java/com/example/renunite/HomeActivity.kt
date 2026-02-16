package com.example.renunite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        // Bottom Navigation logic
        findViewById<android.view.View>(R.id.navHome).setOnClickListener {
            loadFragment(HomeFragment())
            updateNavUI(R.id.navHome)
        }

        findViewById<android.view.View>(R.id.navNotifications).setOnClickListener {
            loadFragment(NotificationsFragment())
            updateNavUI(R.id.navNotifications)
        }

        findViewById<android.view.View>(R.id.navMessages).setOnClickListener {
            loadFragment(MessagesFragment())
            updateNavUI(R.id.navMessages)
        }

        findViewById<android.view.View>(R.id.navProfile).setOnClickListener {
            loadFragment(ProfileFragment())
            updateNavUI(R.id.navProfile)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateNavUI(selectedId: Int) {
        val navIds = listOf(R.id.navHome, R.id.navNotifications, R.id.navMessages, R.id.navProfile)
        val iconIds = listOf(R.id.ivHome, R.id.ivNotifications, R.id.ivMessages, R.id.ivProfile)
        val textIds = listOf(R.id.tvHome, R.id.tvNotifications, R.id.tvMessages, R.id.tvProfile)

        for (i in navIds.indices) {
            val isSelected = navIds[i] == selectedId
            val color = if (isSelected) {
                androidx.core.content.ContextCompat.getColor(this, R.color.brand_blue)
            } else {
                androidx.core.content.ContextCompat.getColor(this, R.color.tab_unselected)
            }
            
            findViewById<android.widget.ImageView>(iconIds[i]).setColorFilter(color)
            findViewById<android.widget.TextView>(textIds[i]).setTextColor(color)
        }
    }
}
