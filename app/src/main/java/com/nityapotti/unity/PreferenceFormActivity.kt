package com.nityapotti.unity

import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.materialswitch.MaterialSwitch
import android.content.Intent
import android.content.res.ColorStateList
import com.google.android.material.slider.Slider
import android.view.View
import android.widget.RadioGroup
import android.os.Bundle
import android.widget.EditText
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

        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this,  ProfileFragment::class.java)
            startActivity(intent)

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


        cleanSlider.addOnChangeListener { _, value, _ ->
            cleanVal = value
        }

        rentSlider.addOnChangeListener { _, value, _ ->
            rentVal = (1400 * value.toInt())  - ((1400 * value.toInt()) % 50)
        }

        visibleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                visibilityVal = true
            }
            else {
                visibilityVal = false
            }
        }

        genderGroup.setOnCheckedChangeListener { _, checkedId ->
            val genderChecked = findViewById<RadioButton>(checkedId)
            genderVal = genderChecked.text.toString()
        }

        tempGroup.setOnCheckedChangeListener { _, checkedId ->
            val tempChecked = findViewById<RadioButton>(checkedId)
            tempVal = tempChecked.text.toString()
        }

        bedGroup.setOnCheckedChangeListener { _, checkedId ->
            val bedChecked = findViewById<RadioButton>(checkedId)
            bedVal = bedChecked.text.toString()
        }

        llcGroup.setOnCheckedChangeListener { _, checkedId ->
            val llcChecked = findViewById<RadioButton>(checkedId)
            llcVal = llcChecked.text.toString()
        }

        eastOrWestGroup.setOnCheckedChangeListener { _, checkedId ->
            val eastOrWestChecked = findViewById<RadioButton>(checkedId)
            eastOrWestVal = eastOrWestChecked.text.toString()
        }

        onCampusGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbOnCampus -> {
                    eastOrWestGroup.visibility = View.VISIBLE
                    llcGroup.visibility = View.VISIBLE
                    locationText.visibility = View.VISIBLE
                    llcText.visibility = View.VISIBLE
                }

                R.id.rbOffCampus -> {
                    eastOrWestGroup.visibility = View.GONE
                    llcGroup.visibility = View.GONE
                    locationText.visibility = View.GONE
                    llcText.visibility = View.GONE
                }
            }
            val onCampusChecked = findViewById<RadioButton>(checkedId)

            if (onCampusChecked.text.toString() == "On campus") {
                onCampusVal = true
            }
            else if (onCampusChecked.text.toString() == "Off campus") {
                onCampusVal = false
            }
        }

        val cleanTextView = findViewById<TextView>(R.id.tvClean)

        cleanSlider.setLabelFormatter { value: Float ->
            when (value.toInt()) {
                in 0..4 -> "Super messy"
                in 5..8 -> "A little messy"
                in 9..12 -> "Not dirty, not spotless"
                in 13..16 -> "tidy"
                in 17..20 -> "spotless"

                else -> "${value.toInt()}"
            }
        }

        cleanSlider.addOnChangeListener { slider, value, fromUser ->
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
            val remainder = rent % 50
            rent -= remainder
            rent.toString()
        }

        rentSlider.addOnChangeListener { slider, value, fromUser ->
            var rent = (value * 1400).toInt()
            val remainder = rent % 50
            rent -= remainder
            rent.toString()
            val rentvlaue = "Maximum expected rent: \$$rent"

            rentText.text = rentvlaue
        }

        val goldColor = ContextCompat.getColor(this, R.color.gt_gold)
        val navyColor = ContextCompat.getColor(this, R.color.gt_navy)

        visibleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                visibleSwitch.trackTintList = ColorStateList.valueOf(goldColor)
            } else {
                visibleSwitch.trackTintList = ColorStateList.valueOf(navyColor)
            }
        }

        val skipButton = findViewById<MaterialButton>(R.id.btnBack)
        skipButton.setOnClickListener {
            val intent = Intent(this, ProfileFragment::class.java)
            startActivity(intent)
        }

        val submitButton = findViewById<MaterialButton>(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val nameVal = nameText.text.toString()
            val bioVal = bioText.text.toString()
            val uid = user?.uid

            if (uid != null) {
                val userPref = hashMapOf(
                    "id" to uid,
                    "visible" to visibilityVal,
                    "name" to nameVal,
                    "gender" to genderVal,
                    "temperature" to tempVal,
                    "bedtime" to bedVal,
                    "cleanliness" to cleanVal,
                    "oncampus" to onCampusVal,
                    "location" to eastOrWestVal,
                    "llc" to llcVal,
                    "maxrent" to rentVal,
                    "bio" to bioVal
                )
                db. collection("userPref").document(uid)
                    .set(userPref)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,  ProfileFragment::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
            }


        }



    }

    }









