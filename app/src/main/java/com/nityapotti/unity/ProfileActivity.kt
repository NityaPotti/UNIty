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


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val btnPreferenceForm = findViewById<Button>(R.id.btnPreferenceForm)
        val user = auth.currentUser
        val textView = findViewById<TextView>(R.id.user_details);

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
    }
}