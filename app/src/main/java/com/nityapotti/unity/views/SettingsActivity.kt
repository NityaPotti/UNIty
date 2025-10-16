package com.nityapotti.unity.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.nityapotti.unity.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.fragment_settings)
            findViewById<ImageButton>(R.id.back_button_settings).setOnClickListener {
                finish()
            }
            val btnLogOut = findViewById<Button>(R.id.logout_button_settings)
            btnLogOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startLoginAndClearBackStack()
            }
            val btnSaveEmail = findViewById<Button>(R.id.save_email_button);
            btnSaveEmail.setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser
                val newEmail = findViewById<EditText>(R.id.email_change_input).text.toString().trim()
                if (user != null && newEmail.isNotEmpty()) {
                    user.updateEmail(newEmail).addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        private fun startLoginAndClearBackStack() {
            val intent = Intent(this, LoginActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            }
            startActivity(intent)
            finishAffinity()
        }
    }