package com.nityapotti.unity.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.nityapotti.unity.PreferenceFormActivity
import com.nityapotti.unity.R
import com.nityapotti.unity.views.SettingsActivity
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.SpinnerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.common.collect.ArrayTable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.SetOptions

class PreferenceFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_preference_form_page2, container, false)
        return view
    }

    private lateinit var toolbar: MaterialToolbar
    private lateinit var spinners: List<Spinner>
    private val selectedList = mutableListOf<Int>()
    private val spinnerAdapters = mutableMapOf<Spinner, ArrayAdapter<Any>>()
    private var isUpdating = false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.topAppBar2)

        toolbar.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val layout = view.findViewById<LinearLayout>(R.id.container)
            layout.visibility = View.VISIBLE
            fragmentManager.popBackStack()
        }

        val spinnerGender = view.findViewById<Spinner>(R.id.spinner_gender)
        val spinnerExtroversion = view.findViewById<Spinner>(R.id.spinner_extroversion)
        val spinnerSociability = view.findViewById<Spinner>(R.id.spinner_sociability)
        val spinnerMajor = view.findViewById<Spinner>(R.id.spinner_major)
        val spinnerYear = view.findViewById<Spinner>(R.id.spinner_year)
        val spinnerNoiseLevel = view.findViewById<Spinner>(R.id.spinner_noiselevel)
        val spinnerGuests = view.findViewById<Spinner>(R.id.spinner_guests)
        val spinnerTemperature = view.findViewById<Spinner>(R.id.spinner_temperature)
        val spinnerBedtime = view.findViewById<Spinner>(R.id.spinner_bedtime)
        val spinnerWakeup = view.findViewById<Spinner>(R.id.spinner_wakeup)
        val spinnerCleanliness = view.findViewById<Spinner>(R.id.spinner_cleanliness)
        val spinnerOnCampus = view.findViewById<Spinner>(R.id.spinner_oncampus)
        val spinnerLocation = view.findViewById<Spinner>(R.id.spinner_location)
        val spinnerLLC = view.findViewById<Spinner>(R.id.spinner_llc)
        val spinnerLeaseLength = view.findViewById<Spinner>(R.id.spinner_leaselength)
        val spinnerRoommates = view.findViewById<Spinner>(R.id.spinner_roommates)
        val spinnerMaxRent = view.findViewById<Spinner>(R.id.spinner_maxrent)
        val spinnerSharing = view.findViewById<Spinner>(R.id.spinner_sharing)

        spinners = listOf(
            spinnerGender,
            spinnerExtroversion,
            spinnerSociability,
            spinnerMajor,
            spinnerYear,
            spinnerNoiseLevel,
            spinnerGuests,
            spinnerTemperature,
            spinnerBedtime,
            spinnerWakeup,
            spinnerCleanliness,
            spinnerOnCampus,
            spinnerLocation,
            spinnerLLC,
            spinnerLeaseLength,
            spinnerRoommates,
            spinnerMaxRent,
            spinnerSharing
        )

        for (spinner in spinners) {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, UpdateList())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinnerAdapters[spinner] = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isUpdating) return

                    val selectedItem =parent.getItemAtPosition(position)
                    val prevVal = spinner.tag as? Int
                    prevVal?.let { selectedList.remove(it) }

                    if (selectedItem is Int) {
                        selectedList.add(selectedItem)
                        spinner.tag = selectedItem
                    }
                    Update()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        }
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        val layout = view.findViewById<LinearLayout>(R.id.textSpinnerLayout)

        val rankList = mutableListOf<Any>()

        val skipButton = view.findViewById<MaterialButton>(R.id.btnBack2)
        val submitButton = view.findViewById<MaterialButton>(R.id.btnSubmit2)


        skipButton.setOnClickListener {
            activity?.finish()
        }
        submitButton.setOnClickListener {
            for (i in 0 until 18) {
                val card = layout.getChildAt(i)
                if (card is MaterialCardView) {
                    val linearLayout = card.getChildAt(0)

                    if (linearLayout is LinearLayout) {
                        val textView = linearLayout.getChildAt(0) as? TextView
                        val spinner = linearLayout.getChildAt(1) as? Spinner

                        if (textView != null && spinner != null) {
                            val text = textView.text.toString()
                            val value = spinner.selectedItem as String

                            val pair = Pair(text, value)
                            rankList.add(pair)

                        }
                    }
                }
            }
            if (uid != null) {
                val rankPref = hashMapOf(
                    "ranking" to rankList
                )
                db.collection("userPref").document(uid)
                    .set(rankPref, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(view.context, "Ranking Saved!", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(view.context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
    private fun UpdateList(): MutableList<Any> {
        val numList = mutableListOf<Any>("None")
        for (num in 1..18) {
            if (num !in selectedList) {
                numList.add(num)
            }
        }
        return numList
    }

    private fun Update() {
        isUpdating = true
        for ((spinner, adapter) in spinnerAdapters) {
            adapter.clear()
            adapter.add("None")
            val currentSelection = spinner.tag as? Int

            for (num in 1..18) {
                if (num !in selectedList || num == currentSelection) {
                    adapter.add(num)
                }
            }
            adapter.notifyDataSetChanged()

            if (currentSelection != null ) {
                val newPosition = (0 until adapter.count).firstOrNull {
                    adapter.getItem(it) == currentSelection
                } ?: 0
                spinner.setSelection(newPosition)
            }
        }
        isUpdating = false
    }
}
