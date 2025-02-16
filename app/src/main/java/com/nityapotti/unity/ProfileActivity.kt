package com.nityapotti.unity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val user = auth.currentUser
        val textView = findViewById<TextView>(R.id.user_details);


        if (user == null) {
            // Go to Login Page
            textView.setText("You are not logged in. ");
        }
        else {
            textView.setText(user.email);
        }

        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Go to Login Page
        }
    }
}