package com.nityapotti.unity.views

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.nityapotti.unity.R
import com.nityapotti.unity.models.Preference
import com.nityapotti.unity.ui.fragments.NavigationMenu // change to .ui.activities.NavigationMenuActivity if you renamed

class PreferenceFormActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // Views
    private lateinit var rgGender: RadioGroup
    private lateinit var rgTemperature: RadioGroup
    private lateinit var rgBedtime: RadioGroup
    private lateinit var rgOnCampus: RadioGroup
    private lateinit var rgLocation: RadioGroup
    private lateinit var rgLLC: RadioGroup
    private lateinit var seekBarCleanliness: SeekBar
    private lateinit var seekBarMaxRent: SeekBar
    private lateinit var editTextAbout: EditText
    private lateinit var nameInput: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnSkip: Button

    // Data
    private var visibility: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_form)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        // --- find views ---
        rgGender = findViewById(R.id.rgGender)
        rgTemperature = findViewById(R.id.rgTemperature)
        rgBedtime = findViewById(R.id.rgBedtime)
        rgOnCampus = findViewById(R.id.rgOnCampus)
        rgLocation = findViewById(R.id.rgLocation)
        rgLLC = findViewById(R.id.rgLLC)
        seekBarCleanliness = findViewById(R.id.seekBarClean)
        seekBarMaxRent = findViewById(R.id.seekBarMaxRent)
        editTextAbout = findViewById(R.id.textEditAbout)
        nameInput = findViewById(R.id.Name)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSkip = findViewById(R.id.btnBack) // reuse the same button, relabel it

        // --- prefill from Firestore if logged in ---
        if (uid != null) {
            val userDoc = db.collection("users").document(uid)
            userDoc.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    snapshot.getString("bio")?.let { editTextAbout.setText(it) }
                    snapshot.getString("name")?.let { nameInput.setText(it) }
                    visibility = snapshot.getBoolean("visible") ?: false
                }
            }.addOnFailureListener {
                // Optional: log or toast; not critical for UX
            }
        }

        // --- submit handler ---
        btnSubmit.setOnClickListener {
            savePreferences(uid)
        }

        // --- skip for now handler ---
        btnSkip.text = "Skip for now"
        btnSkip.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                goToApp()     // already authenticated → enter the app
            } else {
                goToLogin()   // not authenticated → back to login
            }
        }
    }

    private fun savePreferences(uid: String?) {
        // If not logged in, route them to login (can't save without UID)
        if (uid == null) {
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
            goToLogin()
            return
        }

        val nameText = nameInput.text.toString().trim()
        val gender = getSelectedRadioText(rgGender)
        val temperature = getSelectedRadioText(rgTemperature)
        val bedtime = getSelectedRadioText(rgBedtime)

        // Basic validation
        when {
            nameText.isEmpty() -> {
                nameInput.error = "Name is required"
                nameInput.requestFocus()
                return
            }
            gender.isNullOrEmpty() -> {
                Toast.makeText(this, "Please select a gender.", Toast.LENGTH_SHORT).show()
                return
            }
            temperature.isNullOrEmpty() -> {
                Toast.makeText(this, "Please select a temperature preference.", Toast.LENGTH_SHORT).show()
                return
            }
            bedtime.isNullOrEmpty() -> {
                Toast.makeText(this, "Please select a bedtime.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val cleanliness = seekBarCleanliness.progress
        val onCampus = getSelectedRadioText(rgOnCampus).orEmpty()
        val location = getSelectedRadioText(rgLocation).orEmpty()
        val llc = getSelectedRadioText(rgLLC).orEmpty()
        val maxRent = seekBarMaxRent.progress
        val about = editTextAbout.text.toString()

        val preference = Preference(
            id = uid.orEmpty(),
            visible = visibility,
            name = nameText,
            gender = gender.orEmpty(),
            temperature = temperature.orEmpty(),
            bedtime = bedtime.orEmpty(),
            cleanliness = cleanliness,
            oncampus = onCampus,
            location = location,
            llc = llc,
            maxrent = maxRent,
            bio = about
        )
        // Save (merge to avoid wiping other fields if schema evolves)
        db.collection("users")
            .document(uid)
            .set(preference, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences saved successfully!", Toast.LENGTH_SHORT).show()
                goToApp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // --- helpers ---

    private fun getSelectedRadioText(group: RadioGroup): String? {
        val id = group.checkedRadioButtonId
        return if (id != -1) findViewById<RadioButton>(id).text.toString() else null
    }

    private fun goToApp() {
        // If you renamed this to NavigationMenuActivity, update class here + manifest
        val intent = Intent(this, NavigationMenu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
