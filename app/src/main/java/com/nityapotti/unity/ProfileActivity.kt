package com.nityapotti.unity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val btnPreferenceForm = findViewById<Button>(R.id.btnPreferenceForm)
        val user = auth.currentUser
        val textView = findViewById<TextView>(R.id.user_details)
        val btnFindRoommates = findViewById<Button>(R.id.btnFindRoommates)

        if (user == null) {
            textView.setText("You are not logged in. ");
            btnLogOut.visibility = View.INVISIBLE
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        else {
            textView.setText(user.email);
            btnLogOut.visibility = View.VISIBLE
        }

        val uid = auth.currentUser?.uid
        val userDoc = db.collection("users").document(uid.toString())
        val visibilitySwitch = findViewById<com.google.android.material.materialswitch.MaterialSwitch>(R.id.visibilitySwitch)

        userDoc.get().addOnSuccessListener { DocumentSnapshot ->
            if (!DocumentSnapshot.exists()) {
                val preference = Preference(false)
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
    }
}