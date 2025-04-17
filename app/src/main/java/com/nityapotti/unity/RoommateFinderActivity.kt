package com.nityapotti.unity.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nityapotti.unity.Preference
import com.nityapotti.unity.R
import com.nityapotti.unity.UserAdapter
import com.nityapotti.unity.UserDetailActivity

class RoommateFinderFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<Preference>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roommate_finder, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userAdapter = UserAdapter(userList) { selectedUser ->
            val intent = Intent(requireContext(), UserDetailActivity::class.java).apply {
                putExtra("id", selectedUser.id)
                putExtra("name", selectedUser.name)
                putExtra("gender", selectedUser.gender)
                putExtra("bio", selectedUser.bio)
                putExtra("temperature", selectedUser.temperature)
                putExtra("bedtime", selectedUser.bedtime)
                putExtra("cleaniness", selectedUser.cleaniness.toString())
                putExtra("oncampus", selectedUser.oncampus)
                putExtra("location", selectedUser.location)
                putExtra("llc", selectedUser.llc)
                putExtra("maxrent", selectedUser.maxrent.toString())
            }
            startActivity(intent)
        }

        recyclerView.adapter = userAdapter

        fetchUsers()

        return view
    }

    private fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("users")
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
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Error fetching users: ", exception)
                Toast.makeText(requireContext(), "Failed to fetch users", Toast.LENGTH_SHORT).show()
            }
    }
}
