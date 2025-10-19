package com.nityapotti.unity.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
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
            val password = findViewById<EditText>(R.id.email_change_input_password).text.toString().trim() // add this field in XML

            if (user != null && newEmail.isNotEmpty() && password.isNotEmpty()) {
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                // Reauthenticate first
                user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.verifyBeforeUpdateEmail(newEmail)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Verification email sent to $newEmail. Please verify to complete the change.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error: ${updateTask.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Reauth failed: ${reauthTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in both email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        val btnSavePassword = findViewById<Button>(R.id.change_password_button)
        btnSavePassword.setOnClickListener {
            Log.d("Debug", "Change Password clicked")
            val user = FirebaseAuth.getInstance().currentUser
            val currentPassword = findViewById<EditText>(R.id.password_change_input_current_password).text.toString().trim()
            val newPassword = findViewById<EditText>(R.id.password_change_input_new_password).text.toString().trim()
            val confirmPassword = findViewById<EditText>(R.id.password_change_input_reenter_new_password).text.toString().trim()

            if (user == null) {
                Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("PasswordChange", "Update failed", updateTask.exception)
                            Toast.makeText(this, "Error: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("PasswordChange", "Reauth failed", reauthTask.exception)
                    Toast.makeText(this, "Reauth failed: ${reauthTask.exception?.message}", Toast.LENGTH_SHORT).show()
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