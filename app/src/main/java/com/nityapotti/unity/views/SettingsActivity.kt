package com.nityapotti.unity.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
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
                // If you also use GoogleSignInClient, sign out there too.
                startLoginAndClearBackStack()
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