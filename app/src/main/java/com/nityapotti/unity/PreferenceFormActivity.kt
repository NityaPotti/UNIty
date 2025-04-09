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
    private lateinit var seekBarCleaniness: SeekBar
    private lateinit var rgOnCampus: RadioGroup
    private lateinit var rgLocation: RadioGroup
    private lateinit var rgLLC: RadioGroup
    private lateinit var seekBarMaxRent: SeekBar
    private lateinit var editTextAbout: EditText
    private lateinit var btnSubmit: Button
    private var visibility = false;

    private lateinit var nameInput: EditText

    private lateinit var btnBack: Button
    

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_form)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        rgGender = findViewById(R.id.rgGender)
        rgTemperature = findViewById(R.id.rgTemperature)
        rgBedtime = findViewById(R.id.rgBedtime)
        seekBarCleaniness = findViewById(R.id.seekBarClean)
        rgOnCampus = findViewById(R.id.rgOnCampus)
        rgLocation = findViewById(R.id.rgLocation)
        rgLLC = findViewById(R.id.rgLLC)
        btnSubmit = findViewById(R.id.btnSubmit)
        seekBarMaxRent = findViewById(R.id.seekBarMaxRent)
        editTextAbout = findViewById(R.id.textEditAbout)
        val userDoc = db.collection("users").document(uid.toString())
        nameInput = findViewById(R.id.Name)
        btnBack = findViewById(R.id.btnBack)

        val userDoc = db.collection("users").document(uid.toString())

        userDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingBio = documentSnapshot.getString("bio")
                if (!existingBio.isNullOrEmpty()) {
                    editTextAbout.setText(existingBio)
                }
                val existingName = documentSnapshot.getString("name")
                if (!existingName.isNullOrEmpty()) {
                    nameInput.setText(existingName)
                }
                visibility = documentSnapshot.getBoolean("visible") ?: false
            }
        }

        userDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                visibility = documentSnapshot.getBoolean("visible") ?: false;
            }
        }
        btnSubmit.setOnClickListener {
            savePreferences(uid)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun savePreferences(uid: String?) {

        val nameText = nameInput.getText().toString().trim()


        var chosen = rgGender.checkedRadioButtonId
        val gender = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgTemperature.checkedRadioButtonId
        val temperature = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgBedtime.checkedRadioButtonId
        val bedtime = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        val bioText = bioInput.getText().toString()

        if (nameText.isEmpty() || gender.isEmpty() || temperature.isEmpty() || bedtime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }


        val cleaniness = seekBarCleaniness.progress

        chosen = rgOnCampus.checkedRadioButtonId
        val oncampus = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgLocation.checkedRadioButtonId
        val location = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        chosen = rgLLC.checkedRadioButtonId
        val llc = if (chosen != -1) findViewById<RadioButton>(chosen).text.toString() else ""

        val maxrent = seekBarCleaniness.progress

        val about = editTextAbout.text.toString()


        val preference = Preference(visibility, nameText, gender, temperature, bedtime, cleaniness, oncampus, location, llc, maxrent, about)

        db.collection("users")
            .document(uid.toString())
            .set(preference)
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences saved successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}