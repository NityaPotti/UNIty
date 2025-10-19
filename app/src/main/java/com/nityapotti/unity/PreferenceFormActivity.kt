package com.nityapotti.unity

import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.materialswitch.MaterialSwitch
import android.content.Intent
import android.content.res.ColorStateList
import com.google.android.material.slider.Slider
import android.view.View
import android.widget.RadioGroup
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.nityapotti.unity.ui.fragments.ProfileFragment
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.textfield.TextInputEditText
import com.nityapotti.unity.models.Preference

class PreferenceFormActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private var llcVal: String = ""
    private var visibilityVal: Boolean = false
    private var eastOrWestVal: String = ""
    private var onCampusVal: Boolean = false
    private var genderVal: String = ""
    private var tempVal: String = ""
    private var bedVal: String = ""
    private var cleanVal: Float = 0f
    private var rentVal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference_form)

        Toast.makeText(this, "PreferenceFormActivity opened!", Toast.LENGTH_SHORT).show()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val goldColor = ContextCompat.getColor(this, R.color.gt_gold)
        val navyColor = ContextCompat.getColor(this, R.color.gt_navy)

        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setTitleTextColor(getColor(R.color.gt_white))
        toolbar.navigationIcon?.setTint(getColor(R.color.gt_white))

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val visibleSwitch = findViewById<MaterialSwitch>(R.id.visibilitySwitch)
        val cleanSlider = findViewById<Slider>(R.id.seekBarClean)
        val rentSlider = findViewById<Slider>(R.id.seekBarMaxRent)
        val nameText = findViewById<TextInputEditText>(R.id.Name)
        val bioText = findViewById<TextInputEditText>(R.id.textEditAbout)
        val genderGroup = findViewById<RadioGroup>(R.id.rgGender)
        val tempGroup = findViewById<RadioGroup>(R.id.rgTemperature)
        val bedGroup = findViewById<RadioGroup>(R.id.rgBedtime)
        val eastOrWestGroup = findViewById<RadioGroup>(R.id.rgLocation)
        val llcGroup = findViewById<RadioGroup>(R.id.rgLLC)
        val onCampusGroup = findViewById<RadioGroup>(R.id.rgOnCampus)

        val locationText = findViewById<TextView>(R.id.tvLocation)
        val llcText = findViewById<TextView>(R.id.tvLLC)

        val uid = user?.uid

        // -------------------- Defensive loadUserData --------------------
        fun loadUserData() {
            if (uid == null) return
            db.collection("userPref").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(Preference::class.java)
                        user?.let {
                            try {
                                nameText.setText(it.name ?: "")

                                when (it.gender) {
                                    "Male" -> genderGroup.check(R.id.rbMale)
                                    "Female" -> genderGroup.check(R.id.rbFemale)
                                    "Other" -> genderGroup.check(R.id.rbOther)
                                    else -> genderGroup.clearCheck()
                                }

                                when (it.temperature) {
                                    "Cooler ≤ 68°F" -> tempGroup.check(R.id.rbCooler)
                                    "Cool 68 - 70°F" -> tempGroup.check(R.id.Cool)
                                    "Temperate 70 – 72°F" -> tempGroup.check(R.id.rbTemperate)
                                    "Warmer ≥ 72°F" -> tempGroup.check(R.id.rbWarmer)
                                    else -> tempGroup.clearCheck()
                                }

                                when (it.bedtime) {
                                    "Before 10 PM" -> bedGroup.check(R.id.rbBedtime0)
                                    "10-11 PM" -> bedGroup.check(R.id.rbBedtime1)
                                    "11 PM - 12 AM" -> bedGroup.check(R.id.rbBedtime2)
                                    "After 12 AM" -> bedGroup.check(R.id.rbBedtime3)
                                    else -> bedGroup.clearCheck()
                                }

                                cleanSlider.value = (document.getLong("cleanliness")?.toFloat() ?: 0f)

                                when (it.oncampus) {
                                    "On campus" -> onCampusGroup.check(R.id.rbOnCampus)
                                    "Off campus" -> onCampusGroup.check(R.id.rbOffCampus)
                                    else -> onCampusGroup.clearCheck()

                                }

                                when (it.location) {
                                    "East" -> onCampusGroup.check(R.id.rbEast)
                                    "West" -> onCampusGroup.check(R.id.rbWest)
                                    "Either" -> onCampusGroup.check(R.id.rbEither)
                                    else -> onCampusGroup.clearCheck()
                                }

                                when (it.llc) {
                                    "None" -> onCampusGroup.check(R.id.rbNone)
                                    "Explore" -> onCampusGroup.check(R.id.rbExplore)
                                    "Grand Challenges" -> onCampusGroup.check(R.id.rbGC)
                                    "Global Leadership" -> onCampusGroup.check(R.id.rbGlobalLeadership)
                                    "Honors Program" -> onCampusGroup.check(R.id.rbHP)
                                    else -> onCampusGroup.clearCheck()
                                }

                                val rentGet = ((document.getLong("maxrent")?.toFloat() ?: 0f) / 1400)

                                rentSlider.value = rentGet

                                val visible = document.getBoolean("visible") ?: false
                                visibleSwitch.isChecked = visible

                                visibleSwitch.post {
                                    visibleSwitch.trackTintList =
                                        ColorStateList.valueOf(if (visibleSwitch.isChecked) goldColor else navyColor)
                                }

                                bioText.setText(it.bio ?: "")

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(this, "Error parsing user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        }

        loadUserData()

        // -------------------- Sliders --------------------
        cleanSlider.addOnChangeListener { _, value, _ -> cleanVal = value }
        rentSlider.addOnChangeListener { _, value, _ ->
            rentVal = (1400 * value.toInt()) - ((1400 * value.toInt()) % 50)
        }

        // -------------------- RadioGroups --------------------
        fun safeSetListener(group: RadioGroup, setter: (RadioButton) -> Unit) {
            group.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId != -1) {
                    val button = findViewById<RadioButton>(checkedId)
                    setter(button)
                }
            }
        }

        safeSetListener(genderGroup) { genderVal = it.text.toString() }
        safeSetListener(tempGroup) { tempVal = it.text.toString() }
        safeSetListener(bedGroup) { bedVal = it.text.toString() }
        safeSetListener(llcGroup) { llcVal = it.text.toString() }
        safeSetListener(eastOrWestGroup) { eastOrWestVal = it.text.toString() }

        onCampusGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                val button = findViewById<RadioButton>(checkedId)
                onCampusVal = button.text.toString() == "On campus"

                if (onCampusVal) {
                    eastOrWestGroup.visibility = View.VISIBLE
                    llcGroup.visibility = View.VISIBLE
                    locationText.visibility = View.VISIBLE
                    llcText.visibility = View.VISIBLE
                } else {
                    eastOrWestGroup.visibility = View.GONE
                    llcGroup.visibility = View.GONE
                    locationText.visibility = View.GONE
                    llcText.visibility = View.GONE
                }
            }
        }

        // -------------------- Slider Labels --------------------
        val cleanTextView = findViewById<TextView>(R.id.tvClean)
        cleanSlider.setLabelFormatter { value: Float ->
            when (value.toInt()) {
                in 0..4 -> "Super messy"
                in 5..8 -> "A little messy"
                in 9..12 -> "Not dirty, not spotless"
                in 13..16 -> "tidy"
                in 17..20 -> "spotless"
                else -> value.toInt().toString()
            }
        }
        cleanSlider.addOnChangeListener { _, value, _ ->
            val cleanText = when (value.toInt()) {
                in 0..4 -> "Cleanliness: Super messy"
                in 5..8 -> "Cleanliness: A little messy"
                in 9..12 -> "Cleanliness: Not dirty, not spotless"
                in 13..16 -> "Cleanliness: Tidy"
                in 17..20 -> "Cleanliness: Spotless"
                else -> value.toInt().toString()
            }
            cleanTextView.text = cleanText
        }

        val rentText = findViewById<TextView>(R.id.rentValue)
        rentSlider.setLabelFormatter { value ->
            var rent = (value * 1400).toInt()
            rent -= rent % 50
            rent.toString()
        }
        rentSlider.addOnChangeListener { _, value, _ ->
            var rent = (value * 1400).toInt()
            rent -= rent % 50
            rentText.text = "Maximum expected rent: \$$rent"
        }

        // -------------------- Switch --------------------
        visibleSwitch.setOnCheckedChangeListener { _, isChecked ->
            visibilityVal = isChecked
            visibleSwitch.trackTintList =
                ColorStateList.valueOf(if (isChecked) goldColor else navyColor)
        }

        // -------------------- Buttons --------------------
        findViewById<MaterialButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<MaterialButton>(R.id.btnSubmit).setOnClickListener {
            val nameVal = nameText.text.toString()
            val bioVal = bioText.text.toString()
            if (uid != null) {
                val userPref = hashMapOf(
                    "id" to uid,
                    "visible" to visibilityVal,
                    "name" to nameVal,
                    "gender" to genderVal,
                    "temperature" to tempVal,
                    "bedtime" to bedVal,
                    "cleanliness" to cleanVal,
                    "oncampus" to (if (onCampusVal) "On campus" else "Off campus"),
                    "location" to eastOrWestVal,
                    "llc" to llcVal,
                    "maxrent" to rentVal,
                    "bio" to bioVal
                )
                db.collection("userPref").document(uid)
                    .set(userPref)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
