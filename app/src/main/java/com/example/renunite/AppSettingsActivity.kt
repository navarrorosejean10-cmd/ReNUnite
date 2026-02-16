package com.example.renunite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class AppSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("DarkMode", false)
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)

        val isNotificationsEnabled = sharedPref.getBoolean("NotificationsEnabled", true)
        val currentLanguage = sharedPref.getString("AppLanguage", "English (US)")

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val switchNotifications = findViewById<SwitchMaterial>(R.id.switchNotifications)
        val switchDarkMode = findViewById<SwitchMaterial>(R.id.switchDarkMode)
        val layoutLanguage = findViewById<View>(R.id.layoutLanguage)
        val tvCurrentLanguage = findViewById<TextView>(R.id.tvCurrentLanguage)
        val layoutHelpCenter = findViewById<View>(R.id.layoutHelpCenter)
        val layoutPrivacyPolicy = findViewById<View>(R.id.layoutPrivacyPolicy)

        // Set initial states
        switchNotifications.isChecked = isNotificationsEnabled
        switchDarkMode.isChecked = isDarkMode
        tvCurrentLanguage.text = currentLanguage

        btnBack.setOnClickListener {
            finish()
        }

        // Notifications Switch logic
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            showNotificationConfirmationDialog(isChecked)
        }

        // Dark Mode Switch logic
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            showDarkModeConfirmationDialog(isChecked)
        }

        // Language Selection logic
        layoutLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }

        // Help Center logic
        layoutHelpCenter.setOnClickListener {
            val intent = Intent(this, HelpCenterActivity::class.java)
            startActivity(intent)
        }

        // Privacy Policy logic
        layoutPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English (US)", "Filipino", "Spanish", "Japanese")
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val currentLanguage = sharedPref.getString("AppLanguage", "English (US)")
        
        var checkedItem = languages.indexOf(currentLanguage)

        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setTitle("Select Language")
            .setSingleChoiceItems(languages, checkedItem) { _, which ->
                checkedItem = which
            }
            .setPositiveButton("Select") { _, _ ->
                val selectedLanguage = languages[checkedItem]
                sharedPref.edit().putString("AppLanguage", selectedLanguage).apply()
                findViewById<TextView>(R.id.tvCurrentLanguage).text = selectedLanguage
                Toast.makeText(this, "Language changed to $selectedLanguage", Toast.LENGTH_SHORT).show()
                // In a real app, you would trigger a locale change here
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDarkModeConfirmationDialog(isChecked: Boolean) {
        val title = if (isChecked) "Enable Dark Mode?" else "Disable Dark Mode?"
        val message = if (isChecked) 
            "The app will switch to a dark theme for a better night-time experience." 
            else "The app will switch back to the light theme."
        val switchDarkMode = findViewById<SwitchMaterial>(R.id.switchDarkMode)

        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean("DarkMode", isChecked).apply()
                
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                switchDarkMode.setOnCheckedChangeListener(null)
                switchDarkMode.isChecked = !isChecked
                switchDarkMode.setOnCheckedChangeListener { _, newChecked ->
                    showDarkModeConfirmationDialog(newChecked)
                }
            }
            .setOnCancelListener {
                switchDarkMode.setOnCheckedChangeListener(null)
                switchDarkMode.isChecked = !isChecked
                switchDarkMode.setOnCheckedChangeListener { _, newChecked ->
                    showDarkModeConfirmationDialog(newChecked)
                }
            }
            .show()
    }

    private fun showNotificationConfirmationDialog(isChecked: Boolean) {
        val title = if (isChecked) "Enable Notifications?" else "Disable Notifications?"
        val message = if (isChecked) "You will receive alerts for new matches and updates." else "You will no longer receive alerts."
        val switchNotifications = findViewById<SwitchMaterial>(R.id.switchNotifications)

        AlertDialog.Builder(this, R.style.BrandedAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Confirm") { _, _ ->
                val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean("NotificationsEnabled", isChecked).apply()
                val toastMessage = if (isChecked) "Notifications Enabled" else "Notifications Disabled"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->
                switchNotifications.setOnCheckedChangeListener(null)
                switchNotifications.isChecked = !isChecked
                switchNotifications.setOnCheckedChangeListener { _, newChecked ->
                    showNotificationConfirmationDialog(newChecked)
                }
            }
            .setOnCancelListener {
                switchNotifications.setOnCheckedChangeListener(null)
                switchNotifications.isChecked = !isChecked
                switchNotifications.setOnCheckedChangeListener { _, newChecked ->
                    showNotificationConfirmationDialog(newChecked)
                }
            }
            .show()
    }
}
