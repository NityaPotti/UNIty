package com.nityapotti.unity.views

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import com.nityapotti.unity.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.nityapotti.unity.models.Preference
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nityapotti.unity.Adapters.UserAdapter
import android.content.ContentValues.TAG
import com.nityapotti.unity.models.RankPair
import android.widget.Toast


class SuggestedFragment: Fragment() {
    private var userList = mutableListOf<Preference>()
    private val filteredList = mutableListOf<Preference>()
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView

//

    private var llcVal: String = ""
    private var eastOrWestVal: String = ""
    private var onCampusVal: String = ""
    private var genderVal: String = ""
    private var noiseVal: Int = 0
    private var tempVal: String = ""
    private var bedVal: String = ""
    private var cleanVal: Int = 0
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
    private var rankingVal: List<RankPair> = mutableListOf()

    data class RankedUser(val score: Int, val user: Preference)

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_suggested, container, false)

        recyclerView = view.findViewById(R.id.recyclerView2)
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

        val rankedUserList = mutableListOf<RankedUser>()

        val db = FirebaseFirestore.getInstance()
        val theUser = FirebaseAuth.getInstance().currentUser

        val uid = theUser?.uid



        db.collection("userPref").get()
            .addOnSuccessListener { documents ->
                userList =
                    documents.map { it.toObject(Preference::class.java) } as MutableList<Preference>

                if (uid != null) {

                    db.collection("userPref").document(uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val currentUser = document.toObject(Preference::class.java)

                            if (currentUser == null) {
                                return@addOnSuccessListener
                            }

                            genderVal = currentUser.gender
                            yearVal = currentUser.year
                            majorVal = currentUser.major
                            extroVal = currentUser.extroversion
                            socialVal = currentUser.sociability
                            tempVal = currentUser.temperature
                            bedVal = currentUser.bedtime
                            wakeVal = currentUser.wakeup
                            onCampusVal = currentUser.oncampus
                            eastOrWestVal = currentUser.location
                            llcVal = currentUser.llc
                            leaseVal = currentUser.leaselength
                            roommateVal = currentUser.roommates
                            guestsVal = currentUser.guests
                            shareVal = currentUser.sharing
                            noiseVal = currentUser.noiselevel
                            cleanVal = currentUser.cleanliness
                            rentVal = currentUser.maxrent
                            rankingVal = currentUser.ranking

                            for (user in userList) {
                                if (user.id == uid) continue
                                var matchScore = 0
                                for (pair in rankingVal) {
                                    when (pair.key) {
                                        "Gender" -> if (user.gender != null && user.gender == genderVal) matchScore += pair.value.toInt()
                                        "Year" -> if (user.year != null && user.year == yearVal) matchScore += pair.value.toInt()
                                        "Major" -> if (user.major != null && user.major == majorVal) matchScore += pair.value.toInt()
                                        "Extroversion" -> if (user.extroversion != null && user.extroversion == extroVal) matchScore += pair.value.toInt()
                                        "Sociability" -> if (user.sociability != null && user.sociability == socialVal) matchScore += pair.value.toInt()
                                        "Noise Level" -> if (user.noiselevel == noiseVal) matchScore += pair.value.toInt()
                                        "Guests" -> if (user.guests != null && user.guests == guestsVal) matchScore += pair.value.toInt()
                                        "Temperature" -> if (user.temperature != null && user.temperature == tempVal) matchScore += pair.value.toInt()
                                        "Bedtime" -> if (user.bedtime != null && user.bedtime == bedVal) matchScore += pair.value.toInt()
                                        "Wakeup" -> if (user.wakeup != null && user.wakeup == wakeVal) matchScore += pair.value.toInt()
                                        "Cleanliness" -> if (user.cleanliness == cleanVal) matchScore += pair.value.toInt()
                                        "On Campus" -> if (user.oncampus != null && user.oncampus == onCampusVal) matchScore += pair.value.toInt()
                                        "Location" -> if (user.location != null && user.location == eastOrWestVal) matchScore += pair.value.toInt()
                                        "LLC" -> if (user.llc != null && user.llc == llcVal) matchScore += pair.value.toInt()
                                        "Lease Length" -> if (user.leaselength != null && user.leaselength == leaseVal) matchScore += pair.value.toInt()
                                        "Roommates" -> if (user.roommates != null && user.roommates == roommateVal) matchScore += pair.value.toInt()
                                        "Max Rent" -> if (user.maxrent == rentVal) matchScore += pair.value.toInt()
                                        "Sharing" -> if (user.sharing != null && user.sharing == shareVal) matchScore += pair.value.toInt()
                                    }
                                }
                                rankedUserList.add(RankedUser(matchScore, user))
                            }
                            val fullyRankedUserList = rankedUserList.sortedByDescending { it.score }

                            filteredList.clear()
                            for (pair in fullyRankedUserList) {
                                filteredList.add(pair.user)
                            }

                            recyclerView.post {
                                userAdapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener { exception ->
                        }

                        .addOnFailureListener { exception ->
                        }

//            var matchGender = false
//            var matchYear = false
//            var matchMajor = false
//            var matchExtro = false
//            var matchSocial = false
//            var matchTemp = false
//            var matchBed = false
//            var matchWake = false
//            var matchOn = false
//            var matchLocation = false
//            var matchLLC = false
//            var matchLease = false
//            var matchRoommate = false
//            var matchGuest = false
//            var matchShare = false
//            var matchNoise = false
//            var matchClean = false
//            var matchRent = false
//            "Gender" -> if (userGender == genderVal) {matchGender = true}
//            "Year" -> if (userYear == yearVal) { matchYear = true }
//            "Major" -> if (userMajor == majorVal) { matchMajor = true }
//            "Extroversion" -> if (userExtro == extroVal) { matchExtro = true }
//            "Sociability" -> if (userSocial == socialVal) { matchSocial = true }
//            "Noise Level" -> if (userTemp == tempVal) { matchTemp = true }
//            "Guests" -> if (userGuest == guestsVal) { matchGuest = true }
//            "Temperature" -> if (userTemp == tempVal) { matchTemp = true }
//            "Bedtime" -> if (userBed == bedVal) { matchBed = true }
//            "Wakeup" -> if (userWake == wakeVal) { matchWake = true }
//            "Cleanliness" -> if (userClean == cleanVal) { matchClean = true }
//            "On Campus" -> if (userOn == onCampusVal) { matchOn = true }
//            "Location" -> if (userLocation == eastOrWestVal) { matchLocation = true }
//            "LLC" -> if (userLLC == llcVal) { matchLLC = true }
//            "Lease Length" -> if (userLease == leaseVal) { matchLease = true }
//            "Roommates" -> if (userRoommate == roommateVal) { matchRoommate = true }
//            "Max Rent" -> if (userRent == rentVal) { matchRent = true }
//            "Sharing" -> if (userShare == shareVal) { matchShare = true }


                }
            }
        return view
    }
}