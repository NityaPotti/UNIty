package com.nityapotti.unity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // create an instance to store firebase auth.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // retrieving instance of firebase auth
        auth = FirebaseAuth.getInstance();

        // initializing fields from which data is to be retrieved
        val emailField = findViewById<TextInputEditText>(R.id.email)
        val passwordField = findViewById<TextInputEditText>(R.id.password)
        val btn_login = findViewById<Button>(R.id.btn_login)

        btn_login.setOnClickListener{
            // retrieving email
            val email = emailField.text.toString().trim()

            // retrieving password
            val password = passwordField.text.toString().trim()

            if (email.isBlank() || email.isEmpty() || password.isBlank() || password.isEmpty()) {
                // Toast if user has left a field blank/empty
                Toast.makeText(this, "Email/Password has not been entered.", Toast.LENGTH_SHORT).show()
            } else {
                // calling the login function
                loginUser(email, password)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(email: String, password: String) {
        // using the sign in with email and password feature
        auth.signInWithEmailAndPassword(email, password)
            // when task is complete
            .addOnCompleteListener(this) { task ->
                // if email and password are correct
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    // move to the next activity here!
                }
                // login details are incorrect.
                else {
                    Toast.makeText(this, "Incorrect password/email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}