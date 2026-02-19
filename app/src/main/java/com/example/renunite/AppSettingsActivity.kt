package com.example.renunite

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
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
            val title = if (isChecked) "Enable Notifications?" else "Disable Notifications?"
            val message = if (isChecked) "You will receive alerts for new matches and updates." else "You will no longer receive alerts."
            
            showBrandedConfirmDialog(title, message, R.drawable.ic_notifications) {
                val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean("NotificationsEnabled", isChecked).apply()
            } ?: run {
                // If cancelled, revert switch state
                switchNotifications.setOnCheckedChangeListener(null)
                switchNotifications.isChecked = !isChecked
                switchNotifications.setOnCheckedChangeListener { _, newChecked ->
                    // Re-attach logic
                }
            }
        }

        // Dark Mode Switch logic
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val title = if (isChecked) "Enable Dark Mode?" else "Disable Dark Mode?"
            val message = if (isChecked) 
                "The app will switch to a dark theme for a better night-time experience." 
                else "The app will switch back to the light theme."

            showBrandedConfirmDialog(title, message, R.drawable.ic_home) {
                val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean("DarkMode", isChecked).apply()
                
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            } ?: run {
                switchDarkMode.setOnCheckedChangeListener(null)
                switchDarkMode.isChecked = !isChecked
                switchDarkMode.setOnCheckedChangeListener { _, newChecked ->
                    // Re-attach logic
                }
            }
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
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_photo_options_branded) // Reusing the branded layout structure
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btn1 = dialog.findViewById<MaterialButton>(R.id.btnTakePhoto)
        val btn2 = dialog.findViewById<MaterialButton>(R.id.btnChooseGallery)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancelPhoto)

        ivIcon.setImageResource(R.drawable.ic_program)
        tvTitle.text = "Select Language"
        tvMessage.text = "Choose your preferred language for the application"
        
        btn1.text = "English (US)"
        btn2.text = "Filipino"
        
        btn1.setOnClickListener {
            updateLanguage("English (US)")
            dialog.dismiss()
        }
        
        btn2.setOnClickListener {
            updateLanguage("Filipino")
            dialog.dismiss()
        }
        
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun updateLanguage(lang: String) {
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        sharedPref.edit().putString("AppLanguage", lang).apply()
        findViewById<TextView>(R.id.tvCurrentLanguage).text = lang
        showBrandedAlertDialog("Language Updated", "App language has been changed to $lang.", R.drawable.ic_check_circle)
    }

    private fun showBrandedConfirmDialog(title: String, message: String, iconRes: Int, onConfirm: () -> Unit): Boolean? {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btnPositive)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btnNegative)

        ivIcon.setImageResource(iconRes)
        tvTitle.text = title
        tvMessage.text = message
        btnPositive.text = "Confirm"
        btnNegative.text = "Cancel"

        var result = false
        btnPositive.setOnClickListener {
            onConfirm()
            result = true
            dialog.dismiss()
        }
        
        btnNegative.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
        return result
    }

    private fun showBrandedAlertDialog(title: String, message: String, iconRes: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btnPositive)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btnNegative)

        ivIcon.setImageResource(iconRes)
        tvTitle.text = title
        tvMessage.text = message
        btnPositive.text = "OK"
        btnNegative.visibility = View.GONE

        btnPositive.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}
