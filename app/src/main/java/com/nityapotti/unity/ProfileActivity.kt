package com.nityapotti.unity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nityapotti.unity.ui.fragments.RoommateFinderFragment
import java.io.IOException
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImageToFirebase(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        val btnLogOut = view.findViewById<Button>(R.id.btnLogOut)
        val btnPreferenceForm = view.findViewById<Button>(R.id.btnPreferenceForm)
        val btnAddPhotos = view.findViewById<Button>(R.id.btnAddPhotos)
        val textView = view.findViewById<TextView>(R.id.user_details)
        val btnFindRoommates = view.findViewById<Button>(R.id.btnFindRoommates)
        val visibilitySwitch = view.findViewById<MaterialSwitch>(R.id.visibilitySwitch)

        val user = auth.currentUser
        if (user == null) {
            textView.text = "You are not logged in."
            btnLogOut.visibility = View.INVISIBLE
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        } else {
            textView.text = user.email
            btnLogOut.visibility = View.VISIBLE
        }

        val uid = user?.uid
        val userDoc = db.collection("users").document(uid.toString())
        userDoc.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                val preference = Preference(uid.toString(), false)
                db.collection("users").document(uid.toString()).set(preference)
            }
            visibilitySwitch.isChecked = doc.getBoolean("visible") ?: false
        }

        visibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            userDoc.update("visible", isChecked)
        }

        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), Login::class.java))
        }

        btnPreferenceForm.setOnClickListener {
            Toast.makeText(requireContext(), "Opening preference form", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), PreferenceFormActivity::class.java))
        }


        btnFindRoommates.setOnClickListener {
            btnFindRoommates.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_wrapper, RoommateFinderFragment())
                    .addToBackStack(null) // Optional: so user can press back
                    .commit()
            }


        }

        btnAddPhotos.setOnClickListener {
            selectImageFromGallery()
        }

        return view
    }

    private fun selectImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = "user_photos/$userId/${UUID.randomUUID()}.jpg"
        val fileRef = FirebaseStorage.getInstance().reference.child(fileName)

        val imageBytes = imageToBlob(imageUri, requireContext())
        if (imageBytes == null) {
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

    private fun imageToBlob(imageUri: Uri, context: Context): ByteArray? {
        return try {
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveImageUrlToFirestore(userId: String, imageUrl: String) {
        val userImageRef = db.collection("users").document(userId)
        userImageRef.update("profileImage", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save image URL", Toast.LENGTH_SHORT).show()
            }
    }
}
