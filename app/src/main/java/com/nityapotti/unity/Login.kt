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

            // returning message to user if email data is invalid
            if (!isValidEmail(email)) {
                emailField.error = "Please enter a valid email."
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (email.isBlank() || email.isEmpty() || password.isBlank() || password.isEmpty()) {
                // Toast if user has left a field blank/empty
                Toast.makeText(this, "Email/Password has not been entered.", Toast.LENGTH_SHORT).show()

                // Focus on email
                if (email.isEmpty() || email.isBlank()) {
                    emailField.error = "Email is required"
                    emailField.requestFocus()
                    return@setOnClickListener
                }

                // Focus on password
                if (password.isEmpty() || password.isBlank()) {
                    passwordField.error = "Password is required"
                    passwordField.requestFocus()
                    return@setOnClickListener
                }

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

    // login function
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

    // email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}