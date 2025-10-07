package com.nityapotti.unity.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nityapotti.unity.R
import com.nityapotti.unity.models.Preference
import com.nityapotti.unity.ui.fragments.RoommateFinderFragment
import java.io.IOException
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadImageToFirebase(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()

        val btnLogOut = view.findViewById<Button>(R.id.btnLogOut)
        val btnPreferenceForm = view.findViewById<Button>(R.id.btnPreferenceForm)
        val btnAddPhotos = view.findViewById<Button>(R.id.btnAddPhotos)
        val btnFindRoommates = view.findViewById<Button>(R.id.btnFindRoommates)
        val visibilitySwitch = view.findViewById<MaterialSwitch>(R.id.visibilitySwitch)
        val userDetailsText = view.findViewById<TextView>(R.id.user_details)
        val settingsButton = view.findViewById<ImageView>(R.id.topRightSettingsIcon)

        val user = auth.currentUser
        if (user == null) {
            userDetailsText.text = "You are not logged in."
            btnLogOut.visibility = View.INVISIBLE
            startLoginAndClearBackStack()
            return view
        } else {
            userDetailsText.text = user.email
            btnLogOut.visibility = View.VISIBLE
        }

        val uid = user.uid
        val userDoc = db.collection("users").document(uid)

        userDoc.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                val preference = Preference(id = uid, visible = false)
                userDoc.set(preference)
            }
            visibilitySwitch.isChecked = doc.getBoolean("visible") ?: false
        }

        visibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            userDoc.update("visible", isChecked)
        }

        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // If you also use GoogleSignInClient, sign out there too.
            startLoginAndClearBackStack()
        }

        btnPreferenceForm.setOnClickListener {
            startActivity(Intent(requireContext(), PreferenceFormActivity::class.java))
        }

        btnFindRoommates.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper, RoommateFinderFragment())
                .addToBackStack(null)
                .commit()
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        btnAddPhotos.setOnClickListener { selectImageFromGallery() }

        return view
    }

    private fun startLoginAndClearBackStack() {
        // Your login screen is LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun selectImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = "user_photos/$userId/${UUID.randomUUID()}.jpg"
        val fileRef = FirebaseStorage.getInstance().reference.child(fileName)

        val imageBytes = imageToBlob(imageUri, requireContext()) ?: run {
            Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show()
            return
        }

        fileRef.putBytes(imageBytes)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToFirestore(userId, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun imageToBlob(imageUri: Uri, context: Context): ByteArray? =
        try { context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() } }
        catch (e: IOException) { e.printStackTrace(); null }

    private fun saveImageUrlToFirestore(userId: String, imageUrl: String) {
        db.collection("users").document(userId)
            .update("profileImage", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save image URL", Toast.LENGTH_SHORT).show()
            }
    }
}
