package com.nityapotti.unity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RoommateFinderActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<Preference>()
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roommate_finder)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(userList) { selectedUser ->
            val intent = Intent(this, UserDetailActivity::class.java).apply {
                putExtra("name", selectedUser.name)
                putExtra("gender", selectedUser.gender)
                putExtra("bio", selectedUser.bio)
            }
            startActivity(intent)
        }

        recyclerView.adapter = userAdapter

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        fetchUsers()
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
                Log.e("Error", "Error: ", exception)
            }
    }
}