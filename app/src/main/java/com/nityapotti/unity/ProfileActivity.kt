package com.nityapotti.unity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val btnPreferenceForm = findViewById<Button>(R.id.btnPreferenceForm)
        val btnAddPhotos = findViewById<Button>(R.id.btnAddPhotos)
        val user = auth.currentUser
        val textView = findViewById<TextView>(R.id.user_details);
        val btnFindRoommates = findViewById<Button>(R.id.btnFindRoommates)


        if (user == null) {
            textView.setText("You are not logged in. ");
            btnLogOut.visibility = View.INVISIBLE
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        } else {
            textView.setText(user.email);
            btnLogOut.visibility = View.VISIBLE
        }


        val uid = auth.currentUser?.uid
        val userDoc = db.collection("users").document(uid.toString())
        val visibilitySwitch =
            findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.visibilitySwitch)
        userDoc.get().addOnSuccessListener { DocumentSnapshot ->
            if (!DocumentSnapshot.exists()) {
                val preference = Preference(uid.toString(), false)
                db.collection("users")
                    .document(uid.toString())
                    .set(preference)
            }
            visibilitySwitch.isChecked = DocumentSnapshot.getBoolean("visible") ?: false

        }

        visibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            userDoc.update("visible", isChecked)
        }

        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            btnLogOut.visibility = View.INVISIBLE
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnPreferenceForm.setOnClickListener {
            val intent = Intent(this, PreferenceFormActivity::class.java)
            startActivity(intent)
        }

        btnFindRoommates.setOnClickListener {
            val intent = Intent(this, RoommateFinderActivity::class.java)
            startActivity(intent)
        }

        btnAddPhotos.setOnClickListener {
            //selectImageFromGallery()
        }
    }
}
    /*
    private fun selectImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImageToFirebase(it)
            }
        }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = "user_photos/$userId/${UUID.randomUUID()}.jpg"
        val fileRef = FirebaseStorage.getInstance().reference.child(fileName)

        val imageBytes = imageToBlob(imageUri, this)
        if (imageBytes == null) {
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
            return
        }

        fileRef.putBytes(imageBytes)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToFirestore(userId, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
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
        val db = FirebaseFirestore.getInstance()
        val userImageRef = db.collection("users").document(userId)

        userImageRef.update("profileImage", imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save image URL", Toast.LENGTH_SHORT).show()
            }

    }
*/