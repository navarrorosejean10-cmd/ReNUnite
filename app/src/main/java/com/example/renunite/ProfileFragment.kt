package com.example.renunite

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvInitials: TextView

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                ivProfileImage.setImageBitmap(imageBitmap)
                showImage(true)
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            ivProfileImage.setImageURI(uri)
            showImage(true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvInitials = view.findViewById(R.id.tvInitials)

        view.findViewById<CardView>(R.id.btnEditPhoto).setOnClickListener {
            showChangePhotoDialog()
        }

        view.findViewById<LinearLayout>(R.id.btnSecurityPassword).setOnClickListener {
            val intent = Intent(requireContext(), AccountSettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.btnAppSettings).setOnClickListener {
            val intent = Intent(requireContext(), AppSettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            showLogoutConfirmation()
        }

        return view
    }

    private fun showImage(hasImage: Boolean) {
        if (hasImage) {
            ivProfileImage.visibility = View.VISIBLE
            tvInitials.visibility = View.GONE
        } else {
            ivProfileImage.visibility = View.GONE
            tvInitials.visibility = View.VISIBLE
        }
    }

    private fun showChangePhotoDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_profile_photo, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnTakePhoto = dialogView.findViewById<MaterialButton>(R.id.btnTakePhoto)
        val btnChooseGallery = dialogView.findViewById<MaterialButton>(R.id.btnChooseGallery)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelPhoto)

        btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(intent)
            dialog.dismiss()
        }

        btnChooseGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLogoutConfirmation() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_custom_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivDialogIcon)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnPositive = dialog.findViewById<MaterialButton>(R.id.btnPositive)
        val btnNegative = dialog.findViewById<MaterialButton>(R.id.btnNegative)

        ivIcon.setImageResource(R.drawable.ic_logout)
        tvTitle.text = "Logout"
        tvMessage.text = "Are you sure you want to log out of your account?"
        btnPositive.text = "Logout"
        btnNegative.text = "Cancel"
        btnNegative.visibility = View.VISIBLE

        btnPositive.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnNegative.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
