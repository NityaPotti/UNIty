package com.nityapotti.unity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PreferenceFormActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var rgGender: RadioGroup
    private lateinit var rgTemperature: RadioGroup
    private lateinit var rgBedtime: RadioGroup
    private lateinit var btnSubmit: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_form)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid;
        rgGender = findViewById(R.id.rgGender)
        rgTemperature = findViewById(R.id.rgTemperature)
        rgBedtime = findViewById(R.id.rgBedtime)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            savePreferences(uid)
        }
    }

    private fun savePreferences(uid: String?) {
        var chosen = rgGender.checkedRadioButtonId
        val gender = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgTemperature.checkedRadioButtonId
        val temperature = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgBedtime.checkedRadioButtonId
        val bedtime = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        if (gender.isEmpty() || temperature.isEmpty() || bedtime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val preference = Preference(gender, temperature, bedtime)
        db.collection("users")
            .document(uid.toString())
            .set(preference)
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences saved successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}