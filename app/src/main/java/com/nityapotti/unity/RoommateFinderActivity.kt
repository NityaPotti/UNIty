package com.nityapotti.unity.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nityapotti.unity.models.Preference
import com.nityapotti.unity.R
import com.nityapotti.unity.Adapters.UserAdapter
import android.widget.EditText
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import android.widget.CheckBox
import android.widget.GridLayout
import android.widget.TextView
import com.google.android.material.materialswitch.MaterialSwitch
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import com.nityapotti.unity.adapters.DealBreakerAdapter
import com.nityapotti.unity.adapters.YearAdapter
import com.nityapotti.unity.views.UserDetailActivity

//import com.nityapotti.unity.views.UserDetailActivity
class RoommateFinderFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var filterSwitch: MaterialSwitch
    private val userList = mutableListOf<Preference>()
    private val filteredList = mutableListOf<Preference>()
    private val checkedCheckBoxes = mutableListOf<CheckBox>()
    private var yearVal: String = ""
    private var yearspinnerInitialized = false
    private var majorspinnerInitialized = false


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roommate_finder, container, false)

        val searchBar = view.findViewById<EditText>(R.id.searchEditText)
        val searchCard = view.findViewById<MaterialCardView>(R.id.searchCard)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userAdapter = UserAdapter(filteredList) { selectedUser ->
            val intent = Intent(requireContext(), UserDetailActivity::class.java).apply {
                putExtra("id", selectedUser.id)
                putExtra("name", selectedUser.name)
                putExtra("gender", selectedUser.gender)
                putExtra("bio", selectedUser.bio)
                putExtra("temperature", selectedUser.temperature)
                putExtra("bedtime", selectedUser.bedtime)
                putExtra("cleanliness", selectedUser.cleanliness.toString())
                putExtra("oncampus", selectedUser.oncampus)
                putExtra("location", selectedUser.location)
                putExtra("llc", selectedUser.llc)
                putExtra("maxrent", selectedUser.maxrent.toString())
            }
            startActivity(intent)
        }

        recyclerView.adapter = userAdapter
        recyclerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gt_white))


        searchBar.setOnFocusChangeListener { view: View, hasFocus: Boolean ->
            val color = if (hasFocus)
                ContextCompat.getColor(requireContext(), R.color.gt_navy)
            else
                ContextCompat.getColor(requireContext(), R.color.gt_gold)

            val width = if (hasFocus)
                (3 * resources.displayMetrics.density).toInt()
            else
                (2 * resources.displayMetrics.density).toInt()
            searchCard.strokeColor = color
            searchCard.strokeWidth = width
        }

        val goldColor = ContextCompat.getColor(requireContext(), R.color.gt_gold)
        val navyColor = ContextCompat.getColor(requireContext(), R.color.gt_navy)

        val searchEdit = view.findViewById<EditText>(R.id.searchEditText)
        val filterCard = view.findViewById<MaterialCardView>(R.id.filterCard)
        val filterBtn = view.findViewById<ImageButton>(R.id.filterBtn)
        val clearLayout = view.findViewById<LinearLayout>(R.id.clearLayout)
        val filterVis = view.findViewById<LinearLayout>(R.id.filterVis)
        filterSwitch = view.findViewById<MaterialSwitch>(R.id.filterSwitch)
        val filterText = view.findViewById<TextView>(R.id.switchText)
        val line = view.findViewById<View>(R.id.line)
        val clearText = view.findViewById<LinearLayout>(R.id.marginLayout)
        val applyButton = view.findViewById<Button>(R.id.applyBtn)

        val genderPref = view.findViewById<GridLayout>(R.id.genderLayout)
        val tempPref = view.findViewById<GridLayout>(R.id.tempLayout)
        val bedPref = view.findViewById<GridLayout>(R.id.bedLayout)
        val cleanPref = view.findViewById<GridLayout>(R.id.cleanLayout)
        val onCampusPref = view.findViewById<GridLayout>(R.id.onCampusLayout)
        val eastOrWestPref = view.findViewById<GridLayout>(R.id.eastOrWestLayout)
        val llcPref = view.findViewById<GridLayout>(R.id.llcLayout)

        val clearBtn = view.findViewById<ImageButton>(R.id.clearBtn)
        val clearFilter = view.findViewById<ImageButton>(R.id.filterClear)
        val rentEdit = view.findViewById<EditText>(R.id.rentEdit)
        val userCard = view.findViewById<MaterialCardView>(R.id.recyclerUser)

        val yearSpinnerSearch = view.findViewById<Spinner>(R.id.yearSearch)
        val yearList = mutableListOf<String>()
        val yearAdapter = YearAdapter(yearList)
        val yearRecycler = view.findViewById<RecyclerView>(R.id.yearRecycler)
        yearRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        yearRecycler.adapter = yearAdapter

        if (yearAdapter.getItems().isEmpty()) {
            yearRecycler.visibility = View.GONE
        }
        else {
            yearRecycler.visibility = View.VISIBLE
        }

        yearSpinnerSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!yearspinnerInitialized) {
                    yearspinnerInitialized = true
                    return
                }

                val selectedItem = parent.getItemAtPosition(position) as String
                android.util.Log.d("YearSpinner", "Selected: $selectedItem, Current list size: ${yearList.size}")

                if (selectedItem == "Select a Year") {
                    yearAdapter.clear()
                    yearRecycler.visibility = View.GONE
                } else {
                    if (!yearList.contains(selectedItem)) {
                        yearAdapter.addItem(selectedItem)
                        yearRecycler.post {
                            yearRecycler.requestLayout()
                        }
                        android.util.Log.d("YearSpinner", "Added item. New list size: ${yearList.size}")
                        android.util.Log.d("YearSpinner", "Adapter item count: ${yearAdapter.itemCount}")
                        android.util.Log.d("YearSpinner", "RecyclerView visibility: ${yearRecycler.visibility}")
                        android.util.Log.d("YearSpinner", "RecyclerView height: ${yearRecycler.height}")
                        android.util.Log.d("YearSpinner", "RecyclerView child count: ${yearRecycler.childCount}")
                    }

                    yearRecycler.visibility = View.VISIBLE  // Move this here
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val majorSearch = view.findViewById<Spinner>(R.id.majorSearch)

        val majorList = mutableListOf<String>()
        val majorAdapter = DealBreakerAdapter(majorList)
        val majorRecycler = view.findViewById<RecyclerView>(R.id.majorRecycler)
        majorRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        majorRecycler.adapter = majorAdapter

        if (majorAdapter.getItems().isEmpty()) {
            majorRecycler.visibility = View.GONE
        }
        else {
            majorRecycler.visibility = View.VISIBLE
        }

        majorSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!majorspinnerInitialized) {
                    majorspinnerInitialized = true
                    return
                }

                val selectedItem = parent.getItemAtPosition(position) as String


                    if (!majorList.contains(selectedItem)) {
                        majorAdapter.addItem(selectedItem)
                    }
                    majorRecycler.visibility = View.VISIBLE
                }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        clearBtn.setOnClickListener {
            searchEdit.setText("")
        }

        val checkboxLayouts = listOf(
            genderPref,
            tempPref,
            bedPref,
            cleanPref,
            onCampusPref,
            eastOrWestPref,
            llcPref
        )

        val params = clearText.layoutParams as ViewGroup.MarginLayoutParams
        val scale = resources.displayMetrics.density

        filterSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (filterSwitch.isChecked) {
                filterSwitch.trackTintList = ColorStateList.valueOf(goldColor)
                filterText.text = "Matches all"
                params.leftMargin = (127.6 * scale).toInt()
                clearText.layoutParams = params

            } else {
                filterSwitch.trackTintList = ColorStateList.valueOf(navyColor)
                filterText.text = "Matches one"
                params.leftMargin = (117.6 * scale).toInt()
                clearText.layoutParams = params
            }
        }

        searchEdit.addTextChangedListener { query ->
            updateFilteredList(query.toString())
        }

        applyButton.setOnClickListener {
            checkedCheckBoxes.clear()
            for (layout in checkboxLayouts) {
                for (i in 0 until layout.childCount) {
                    val view = layout.getChildAt(i)
                    if (view is CheckBox && view.isChecked) {
                        checkedCheckBoxes.add(view)
                    }
                }
            }
            updateFilteredList(searchEdit.text.toString())
        }

        clearFilter.setOnClickListener {
            for (layout in checkboxLayouts) {
                for (i in 0 until layout.childCount) {
                    val view = layout.getChildAt(i)
                    if (view is CheckBox && view.isChecked) {
                        view.setChecked(false)
                    }
                }
            }
            rentEdit.setText("")
        }

        var isExpanded = false

        filterBtn.setOnClickListener {
            isExpanded = !isExpanded

            if (isExpanded) {
                filterBtn.rotation = 180f
                filterCard.radius = 25f * resources.displayMetrics.density
                filterVis.visibility = View.VISIBLE
                clearLayout.visibility = View.VISIBLE
                line.visibility = View.VISIBLE

            } else {
                filterBtn.rotation = 0f
                filterCard.radius = 50f * resources.displayMetrics.density
                filterVis.visibility = View.GONE
                clearLayout.visibility = View.GONE
                line.visibility = View.GONE

            }

        }

        fetchUsers()

        return view
    }

    private fun updateFilteredList(query: String = "") {
        val lowerQuery = query.lowercase()
        filteredList.clear()

        filteredList.addAll(userList.filter { user ->
            if (filterSwitch.isChecked) {
                val matchesCheckbox =
                    if (checkedCheckBoxes.isEmpty()) true else checkedCheckBoxes.all { checkbox ->
                        val checked = checkbox.text.toString()
                        val cleanText = when (user.cleanliness) {
                            in 0..4 -> "Cleanliness: Super messy"
                            in 5..8 -> "Cleanliness: A little messy"
                            in 9..12 -> "Cleanliness: Not dirty, not spotless"
                            in 13..16 -> "Cleanliness: Tidy"
                            in 17..20 -> "Cleanliness: Spotless"
                            else -> user.cleanliness.toString()
                        }
                        user.gender?.contains(checked) == true ||
                                user.temperature?.contains(checked) == true ||
                                user.bedtime?.contains(checked) == true ||
                                user.oncampus?.contains(checked) == true ||
                                user.location?.contains(checked) == true ||
                                user.llc?.contains(checked) == true ||
                                cleanText.contains(checked, ignoreCase = true)
                    }
                val matchesSearch = user.name?.lowercase()?.contains(lowerQuery) == true ||
                        user.bio?.lowercase()?.contains(lowerQuery) == true

                matchesCheckbox && (query.isEmpty() || matchesSearch)
            }

            else {
                val matchesCheckbox =
                    if (checkedCheckBoxes.isEmpty()) true else checkedCheckBoxes.any { checkbox ->
                        val checked = checkbox.text.toString()
                        val cleanText = when (user.cleanliness) {
                            in 0..4 -> "Cleanliness: Super messy"
                            in 5..8 -> "Cleanliness: A little messy"
                            in 9..12 -> "Cleanliness: Not dirty, not spotless"
                            in 13..16 -> "Cleanliness: Tidy"
                            in 17..20 -> "Cleanliness: Spotless"
                            else -> user.cleanliness.toString()
                        }
                        user.gender?.contains(checked) == true ||
                                user.temperature?.contains(checked) == true ||
                                user.bedtime?.contains(checked) == true ||
                                user.oncampus?.contains(checked) == true ||
                                user.location?.contains(checked) == true ||
                                user.llc?.contains(checked) == true ||
                                cleanText.contains(checked, ignoreCase = true)
                    }
                val matchesSearch = user.name?.lowercase()?.contains(lowerQuery) == true ||
                        user.bio?.lowercase()?.contains(lowerQuery) == true

                matchesCheckbox && (query.isEmpty() || matchesSearch)
            }

        })

        userAdapter.notifyDataSetChanged()
    }


    private fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("userPref")
            .whereEqualTo("visible", true)
            .get()
            .addOnSuccessListener { documents ->
                userList.clear()
                for (document in documents) {
                    if (document.id != uid) {
                        val user = document.toObject(Preference::class.java)
                        userList.add(user)
                    }
                }
                filteredList.clear()
                filteredList.addAll(userList)
                userAdapter.notifyDataSetChanged()
            }
    }
}
