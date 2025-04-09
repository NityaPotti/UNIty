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

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        val name = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val bio = intent.getStringExtra("bio")

        val nameTextView = findViewById<TextView>(R.id.detailNameTextView)
        val genderTextView = findViewById<TextView>(R.id.detailGenderTextView)
        val bioTextView = findViewById<TextView>(R.id.detailBioTextView)

        nameTextView.text = name
        genderTextView.text = gender
        bioTextView.text = bio
    }

}