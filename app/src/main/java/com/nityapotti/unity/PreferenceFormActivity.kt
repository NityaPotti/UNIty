package com.nityapotti.unity

import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.materialswitch.MaterialSwitch
import android.content.Intent
import android.content.res.ColorStateList
import com.google.android.material.slider.Slider
import android.view.View
import android.widget.RadioGroup
import android.os.Bundle
import android.widget.LinearLayout
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.textfield.TextInputEditText
import com.nityapotti.unity.models.Preference
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.nityapotti.unity.adapters.DealBreakerAdapter
import android.util.Log
import com.google.common.collect.ArrayTable
import com.nityapotti.unity.adapters.MiscAdapter
//import com.nityapotti.unity.adapters.TagAdapter

class PreferenceFormActivity : AppCompatActivity() {
    companion object {
//        val tagSpinnerList = mutableListOf<String>()
//        val tagList = mutableListOf<String>()
    }

    private lateinit var toolbar: MaterialToolbar
    private var llcVal: String = ""
    private var visibilityVal: Boolean = false
    private var eastOrWestVal: String = ""
    private var onCampusVal: String = ""
    private var genderVal: String = ""
    private var noiseVal: Float = 0f
    private var tempVal: String = ""
    private var bedVal: String = ""
    private var cleanVal: Float = 0f
    private var rentVal: Int = 0
    private var yearVal: String = ""
    private var majorVal: String = ""
    private var extroVal: String = ""
    private var socialVal: String = ""
    private var wakeVal: String = ""
    private var leaseVal: String = ""
    private var roommateVal: String = ""
    private var guestsVal: String = ""
    private var shareVal: String = ""
    private var dealspinnerInitialized = false
    private var miscspinnerInitialized = false
//    private var tagspinnerInitialized = false

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

        val years = listOf("Select a year", "First year", "Second year", "Third year", "Fourth year",
            "Fifth year", "Grad student")

        val majors = listOf("Undecided", "Aerospace Engineering", "Applied Language and Intercultural " +
                "Studies", "Applied Physics", "Architecture", "Arts, Entertainment, and Creative Technologies",
            "Astrophysics", "Atmospheric and Oceanic Sciences", "Biochemistry", "Biology",
            "Biomedical Engineering", "Business Administration",
            "Chemical and Biomolecular Engineering", "Chemistry", "Civil Engineering",
            "Computational Media", "Computer Engineering", "Computer Engineering",
            "Computer Engineering Dual", "Computer Science", "Construction Science and Management",
            "Earth and Atmospheric Sciences", "Economics", "Economics and International Affairs",
            "Electrical Engineering", "Environmental Engineering", "Environmental Science",
            "History, Technology, and Society", "Industrial Design", "Industrial Engineering",
            "International Affairs", "International Affairs and Modern Languages",
            "Literature, Media, and Communication", "Materials Science and Engineering",
            "Mathematics", "Mathematics and Computing", "Mechanical Engineering",
            "Music Technology", "Neuroscience", "Nuclear and Radiological Engineering",
            "Physics", "Psychology", "Public Policy", "Solid Earth and Planetary Sciences",
            "Urban Planning and Spatial Analytics")

        val preferenceFields = listOf("None", "Gender", "Major", "Year", "Extroversion",
            "How often you go out", "Noise Level", "Temperature", "Bed time", "Wakeup time",
            "Cleanliness", "On or off campus", "East or West", "LLC", "Lease Length", "Max rent (Range)",
            "Number of roommates", "OK with guests", "Sharing"
        )

        val miscListSpinner = listOf("None",
            "Adventurous 🧗‍♂️", "Book Lover 📚", "Creative 🎨", "Funny 😄", "Calm 🧘‍♀️", "Quiet Worker 🤫",
            "Sociable at Events 🥳", "Introvert Friendly 🤝", "Extrovert Friendly 🌟", "Flexible 🤸‍♂️",
            "Patient ⏳", "Empathetic 💛", "Independent 🦅", "Motivated 🚀", "Organized 📅", "Procrastinator 💤",
            "Night Owl 🦉", "Early Riser 🌅", "Stress-Resilient 💪", "Debate Enthusiast 🗣️",
            "Gamer 🎮", "Music Enthusiast 🎵", "Artist 🎭", "Fitness Enthusiast 🏋️‍♂️", "Yoga Practitioner 🧘‍♂️",
            "Pet Owner Friendly 🐾", "Culinary Enthusiast 🍳", "Movie Buff 🎬", "Environmentally Conscious 🌱",
            "Tech-Savvy 💻", "Collector 🏺", "DIY Enthusiast 🛠️", "Club/Organization Member 🏫", "Sports Fan ⚽",
            "Volunteer-Oriented 🤲", "Research-Oriented 🔬", "Campus Event Goer 🎉", "Internship-Focused 💼",
            "Likes Plants 🌿", "Likes Board Games 🎲", "Likes Cooking Together 🍲", "Coffee Lover ☕",
            "Tea Drinker 🍵", "Minimalist 🪶", "Social Gatherings Friendly 🥂", "Occasional Entertainer 🎶",
            "Quiet Evenings 🌙", "Flexible Schedule 🕰️", "Weekend Traveler 🧳", "Remote Worker Friendly 🏡",
            "Study Group Friendly 📖", "Library Frequent 📚", "Online Class Friendly 💻", "Snack Lover 🍿",
            "Late Night Snacker 🌙🍫", "Respectful of Privacy 🚪", "Party-Friendly 🎉", "Good Communicator 🗨️",
            "Responsible ✅", "Conflict Resolver ✌️", "Gym Buddy 🏃‍♂️", "Study Buddy 📘", "Likes to Socialize 👫",
            "Independent Lifestyle Friendly 🦅", "Academic Excellence 🎓", "Strong Community 🤝", "Campus Events 🎪",
            "Networking Opportunities 🌐", "Diverse Student Body 🌍", "Research Opportunities 🔬",
            "Internships & Career Support 💼", "Study Abroad Programs ✈️", "Clubs & Organizations 🏫",
            "Sports & Recreation ⚽", "Creative Outlets 🎨", "Cultural Events 🎭", "Volunteer Opportunities 🤲",
            "Flexible Learning Options 📖", "Library & Study Spaces 📚", "On-Campus Dining Options 🍔",
            "Student Housing Experience 🏘️", "Quiet Study Environment 🤫", "Collaborative Learning 👥",
            "Accessible Resources ♿"
        )

        val preferenceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, preferenceFields )
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
        val layoutOff = findViewById<LinearLayout>(R.id.layoutOff)
        val layoutOn = findViewById<LinearLayout>(R.id.layoutOn)
        val dealSpinner = findViewById<Spinner>(R.id.dealBreak)
        val noiseSlider = findViewById<Slider>(R.id.seekBarNoise)
        val yearSpinner = findViewById<Spinner>(R.id.year)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        val majorSpinner = findViewById<Spinner>(R.id.major)
        val majorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, majors )
        val miscSpinner = findViewById<Spinner>(R.id.miscSpinner)
//        val tagSpinner = findViewById<Spinner>(R.id.tagSpinner)
        val extroGroup = findViewById<RadioGroup>(R.id.rgIntroversion)
        val socialGroup = findViewById<RadioGroup>(R.id.rgSocial)
        val wakeUpGroup = findViewById<RadioGroup>(R.id.rgWakeUp)
        val leaseGroup = findViewById<RadioGroup>(R.id.rgLease)
        val roommateGroup = findViewById<RadioGroup>(R.id.rgRoommates)
        val guestsGroup = findViewById<RadioGroup>(R.id.rgGuests)
        val shareGroup = findViewById<RadioGroup>(R.id.rgSharing)

        val dealBreakList = mutableListOf<String>()
        val dealAdapter = DealBreakerAdapter(dealBreakList)
        val dealBreakRecycler = findViewById<RecyclerView>(R.id.dealBreakRecycler)
        dealBreakRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dealBreakRecycler.adapter = dealAdapter

        val miscList = mutableListOf<String>()
        val miscRecycler = findViewById<RecyclerView>(R.id.miscRecycler)
        val miscAdapter = MiscAdapter(miscList)
        val miscSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, miscListSpinner)
        miscSpinner.adapter = miscSpinnerAdapter
        miscRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        miscRecycler.adapter = miscAdapter

//        tagSpinnerList.addAll(preferenceFields)
//        mutableSetOf(tagSpinnerList.addAll(miscAdapter.getItems()))
//        val tagRecycler = findViewById<RecyclerView>(R.id.tagRecycler)
//        val tagAdapter = TagAdapter(tagList)
//        val tagSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tagSpinnerList)
//        tagSpinner.adapter = tagSpinnerAdapter
//        tagRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        tagRecycler.adapter = tagAdapter

        val locationText = findViewById<TextView>(R.id.tvLocation)
        val llcText = findViewById<TextView>(R.id.tvLLC)

        if (dealAdapter.getItems().isEmpty()) {
            dealBreakRecycler.visibility = View.GONE
        }
        else {
            dealBreakRecycler.visibility = View.VISIBLE
        }

        if (miscAdapter.getItems().isEmpty()) {
            miscRecycler.visibility = View.GONE
        }
        else {
            miscRecycler.visibility = View.VISIBLE
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                yearVal = selectedItem
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        majorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                majorVal = selectedItem
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        dealSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (!dealspinnerInitialized)
            {
                dealspinnerInitialized = true
                return
            }
                dealBreakRecycler.visibility = View.VISIBLE
                val selectedItem = parent.getItemAtPosition(position) as String

                if (!dealBreakList.contains(selectedItem)) {
                    dealAdapter.addItem(selectedItem)
                }

                if (selectedItem == "None") {
                    dealAdapter.clear()
                    dealBreakRecycler.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        miscSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!miscspinnerInitialized)
                {
                    miscspinnerInitialized = true
                    return
                }
                miscRecycler.visibility = View.VISIBLE
                val selectedItem = parent.getItemAtPosition(position) as String

                if (!miscList.contains(selectedItem)) {
                    miscAdapter.addItem(selectedItem)
//                    tagSpinnerList.add(selectedItem)
                }

                if (selectedItem == "None") {
                    miscAdapter.clear()
                    miscRecycler.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

//        tagSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                if (!tagspinnerInitialized)
//                {
//                    tagspinnerInitialized = true
//                    return
//                }
//                tagRecycler.visibility = View.VISIBLE
//                val selectedItem = parent.getItemAtPosition(position) as String
//
//                if (!tagList.contains(selectedItem)) {
//                    tagAdapter.addItem(selectedItem)
//                    if (tagList.size > 3) {
//                        tagList.removeAt(tagList.lastIndex)
//                    }
//                }
//
//                if (selectedItem == "None") {
//                    tagAdapter.clear()
//                    tagRecycler.visibility = View.GONE
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }

        val uid = user?.uid

        fun loadUserData() {
            if (uid == null) return
            db.collection("userPref").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) return@addOnSuccessListener
                    val user = document.toObject(Preference::class.java)
                    user?.let {
                        try {
                            nameText.setText(it.name)
                            bioText.setText(it.bio)
                            yearVal = it.year
                            majorVal = it.major
                            extroVal = it.extroversion
                            socialVal = it.sociability
                            tempVal = it.temperature
                            bedVal = it.bedtime
                            wakeVal = it.wakeup
                            onCampusVal = it.oncampus
                            eastOrWestVal = it.location
                            llcVal = it.llc
                            leaseVal = it.leaselength
                            roommateVal = it.roommates
                            guestsVal = it.guests
                            shareVal = it.sharing
                            noiseSlider.value = it.noiselevel.toFloat()
                            cleanSlider.value = it.cleanliness.toFloat()
                            rentSlider.value = it.maxrent / 1400f

                            when (it.gender) {
                                "Male" -> genderGroup.check(R.id.rbMale)
                                "Female" -> genderGroup.check(R.id.rbFemale)
                                "Other" -> genderGroup.check(R.id.rbOther)
                                else -> genderGroup.clearCheck()
                            }

                            when (it.extroversion) {
                                "Introvert" -> extroGroup.check(R.id.rbIntro)
                                "Ambivert" -> extroGroup.check(R.id.rbAmbi)
                                "Extrovert" -> extroGroup.check(R.id.rbExtro)
                                else -> extroGroup.clearCheck()
                            }

                            when (it.sociability) {
                                "Never" -> socialGroup.check(R.id.rbNever)
                                "Sometimes" -> socialGroup.check(R.id.rbSometimes)
                                "Often" -> socialGroup.check(R.id.rbOften)
                                else -> socialGroup.clearCheck()
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

                            when (it.wakeup) {
                                "Before 5 AM" -> wakeUpGroup.check(R.id.rbWake0)
                                "5 - 7 AM" -> wakeUpGroup.check(R.id.rbWake1)
                                "7 - 9 AM" -> wakeUpGroup.check(R.id.rbWake2)
                                "9 - 11 AM" -> wakeUpGroup.check(R.id.rbWake3)
                                "After 11 AM" -> wakeUpGroup.check(R.id.rbWake4)
                                else -> wakeUpGroup.clearCheck()
                            }

                            when (it.oncampus) {
                                "On campus" -> onCampusGroup.check(R.id.rbOnCampus)
                                "Off campus" -> onCampusGroup.check(R.id.rbOffCampus)
                                else -> onCampusGroup.clearCheck()
                            }

                            when (it.location) {
                                "East" -> eastOrWestGroup.check(R.id.rbEast)
                                "West" -> eastOrWestGroup.check(R.id.rbWest)
                                "Either" -> eastOrWestGroup.check(R.id.rbEither)
                                else -> eastOrWestGroup.clearCheck()
                            }

                            when (it.llc) {
                                "None" -> llcGroup.check(R.id.rbNone)
                                "Explore" -> llcGroup.check(R.id.rbExplore)
                                "Grand Challenges" -> llcGroup.check(R.id.rbGC)
                                "Global Leadership" -> llcGroup.check(R.id.rbGlobalLeadership)
                                "Honors Program" -> llcGroup.check(R.id.rbHP)
                                else -> llcGroup.clearCheck()
                            }

                            when (it.roommates) {
                                "1 Roommate" -> roommateGroup.check(R.id.rbr1)
                                "2 Roommates" -> roommateGroup.check(R.id.rbr2)
                                "3 Roommates" -> roommateGroup.check(R.id.rbr3)
                                "More than 3 Roommates" -> roommateGroup.check(R.id.rbr4)
                                else -> roommateGroup.clearCheck()
                            }

                            when (it.leaselength) {
                                "Semester" -> leaseGroup.check(R.id.rbSemester)
                                "2 Semesters" -> leaseGroup.check(R.id.rbYes)
                                "Flexible" -> leaseGroup.check(R.id.rbFlexible)
                                else -> leaseGroup.clearCheck()
                            }

                            when (it.guests) {
                                "No" -> guestsGroup.check(R.id.rbGuestNo)
                                "From time to time" -> guestsGroup.check(R.id.rbGuestSome)
                                "Yes" -> guestsGroup.check(R.id.rbGuestYes)
                                else -> guestsGroup.clearCheck()
                            }

                            when (it.sharing) {
                                "No" -> shareGroup.check(R.id.rbNo)
                                "Some things" -> shareGroup.check(R.id.rbSome)
                                "Yes" -> shareGroup.check(R.id.rbYes)
                                else -> shareGroup.clearCheck()
                            }

                            yearSpinner.setSelection(yearAdapter.getPosition(it.year))
                            majorSpinner.setSelection(majorAdapter.getPosition(it.major))

                            dealAdapter.updateList(it.dealbreakers)
                            dealBreakRecycler.visibility = if (it.dealbreakers.isEmpty()) View.GONE else View.VISIBLE

                            miscAdapter.updateList(it.misc)
                            miscRecycler.visibility = if (it.misc.isEmpty()) View.GONE else View.VISIBLE

                            visibleSwitch.isChecked = it.visible
                            visibleSwitch.trackTintList =
                                ColorStateList.valueOf(if (it.visible) goldColor else navyColor)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        }


        loadUserData()

        noiseSlider.addOnChangeListener { _, value, _ -> noiseVal = value }
        cleanSlider.addOnChangeListener { _, value, _ -> cleanVal = value }
        rentSlider.addOnChangeListener { _, value, _ ->
            rentVal = (1400 * value.toInt()) - ((1400 * value.toInt()) % 50)
        }

        fun safeSetListener(group: RadioGroup, setter: (RadioButton) -> Unit) {
            group.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId != -1) {
                    val button = findViewById<RadioButton>(checkedId)
                    setter(button)
                }
            }
        }

        safeSetListener(genderGroup) { genderVal = it.text.toString() }
        safeSetListener(extroGroup) { extroVal = it.text.toString()}
        safeSetListener(socialGroup) { socialVal = it.text.toString()}
        safeSetListener(tempGroup) { tempVal = it.text.toString() }
        safeSetListener(bedGroup) { bedVal = it.text.toString() }
        safeSetListener(wakeUpGroup) { wakeVal = it.text.toString()}
        safeSetListener(roommateGroup) { roommateVal = it.text.toString()}
        safeSetListener(guestsGroup) { guestsVal = it.text.toString()}
        safeSetListener(shareGroup) { shareVal = it.text.toString()}


        onCampusGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                val button = findViewById<RadioButton>(checkedId)
                onCampusVal = button.text.toString()

                if (onCampusVal == "On campus") {
                    layoutOn.visibility = View.VISIBLE
                    layoutOff.visibility = View.GONE
                    leaseGroup.clearCheck()
                    safeSetListener(llcGroup) { llcVal = it.text.toString() }
                    safeSetListener(eastOrWestGroup) { eastOrWestVal = it.text.toString() }
                    safeSetListener(leaseGroup) { leaseVal = it.text.toString()}
                }
                else if (onCampusVal == "Off campus") {
                    layoutOff.visibility = View.VISIBLE
                    layoutOn.visibility = View.GONE
                    eastOrWestGroup.clearCheck()
                    llcGroup.clearCheck()
                    safeSetListener(llcGroup) { llcVal = it.text.toString() }
                    safeSetListener(eastOrWestGroup) { eastOrWestVal = it.text.toString() }
                    safeSetListener(leaseGroup) { leaseVal = it.text.toString()}
                }
            }
        }

        layoutOn.visibility = View.GONE
        layoutOff.visibility = View.GONE

        val noiseTextView = findViewById<TextView>(R.id.tvNoise)
        noiseSlider.setLabelFormatter { value: Float ->
            when (value.toInt()) {
                in 0..4 -> "Won't hear anything"
                in 5..8 -> "A little noise "
                in 9..12 -> "A moderate amount"
                in 13..16 -> "Can be a little loud"
                in 17..20 -> "Pretty Noisy"
                else -> value.toInt().toString()
            }
        }
        noiseSlider.addOnChangeListener { _, value, _ ->
            val noiseText = when (value.toInt()) {
                in 0..4 -> "Won't hear anything"
                in 5..8 -> "A little noise "
                in 9..12 -> "A moderate amount"
                in 13..16 -> "Can be a little loud"
                in 17..20 -> "Pretty Noisy"
                else -> value.toInt().toString()
            }
            noiseTextView.text = noiseText
        }

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

        visibleSwitch.setOnCheckedChangeListener { _, isChecked ->
            visibilityVal = isChecked
            visibleSwitch.trackTintList =
                ColorStateList.valueOf(if (isChecked) goldColor else navyColor)
        }
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
                    "extroversion" to extroVal,
                    "sociability" to socialVal,
                    "year" to yearVal,
                    "major" to majorVal,
                    "noiselevel" to noiseVal,
                    "temperature" to tempVal,
                    "bedtime" to bedVal,
                    "wakeup" to wakeVal,
                    "cleanliness" to cleanVal,
                    "oncampus" to onCampusVal,
                    "location" to eastOrWestVal,
                    "llc" to llcVal,
                    "leaselength" to leaseVal,
                    "roommates" to roommateVal,
                    "guests" to guestsVal,
                    "sharing" to shareVal,
                    "maxrent" to rentVal,
                    "dealbreakers" to dealAdapter.getItems(),
                    "misc" to miscAdapter.getItems(),
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
