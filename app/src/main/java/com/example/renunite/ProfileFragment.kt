package com.example.renunite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.findViewById<CardView>(R.id.btnEditPhoto).setOnClickListener {
            showChangePhotoDialog()
        }

        view.findViewById<LinearLayout>(R.id.btnSecurityPassword).setOnClickListener {
            val intent = Intent(requireContext(), SecurityPasswordActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.btnAppPreferences).setOnClickListener {
            val intent = Intent(requireContext(), AppSettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            showLogoutConfirmation()
        }

        return view
    }

    private fun showChangePhotoDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Remove Photo")
        AlertDialog.Builder(requireContext(), R.style.BrandedAlertDialog)
            .setTitle("Change Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(requireContext(), "Camera coming soon", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), "Gallery coming soon", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), "Photo removed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext(), R.style.BrandedAlertDialog)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out of your account?")
            .setPositiveButton("Logout") { _, _ ->
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}