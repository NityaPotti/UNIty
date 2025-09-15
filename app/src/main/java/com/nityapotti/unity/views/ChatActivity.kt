//package com.nityapotti.unity
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.Timestamp
//
//
//class ChatActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var messageAdapter: MessageAdapter
//    private val messageList = mutableListOf<Message>()
//    private lateinit var btnBack: Button
//    private lateinit var btnSend: Button
//    private lateinit var input: EditText
//    private lateinit var otherId: String
//    private lateinit var uid: String
//
//    private lateinit var auth: FirebaseAuth
//    private val db = FirebaseFirestore.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat)
//
//        auth = FirebaseAuth.getInstance()
//        input = findViewById(R.id.input)
//        btnBack = findViewById(R.id.btnBack)
//        btnSend = findViewById(R.id.btnSend)
//
//        otherId = intent.getStringExtra("otherId").toString()
//        val otherName = intent.getStringExtra("otherName")
//
//        val nameText = findViewById<TextView>(R.id.name)
//
//        nameText.setText(otherName)
//
//        uid = auth.currentUser?.uid.toString()
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        messageAdapter = MessageAdapter(messageList, uid.toString())
//
//        recyclerView.adapter = messageAdapter
//
//        btnBack.setOnClickListener {
//            finish()
//        }
//
//        btnSend.setOnClickListener {
//            val text = input.getText().toString()
//            if (text.trim().length > 0) {
//
//                val currentTimestamp = Timestamp.now()
//
//                db.collection("chats")
//                    .document(concat(uid, otherId))
//                    .collection("messages")
//                    .add(Message(uid, otherId, text, false, currentTimestamp))
//                    .addOnFailureListener { e: Exception ->
//                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//
//            }
//            input.setText("")
//
//            fetchMessages()
//
//        }
//
//        fetchMessages()
//
//    }
//
//    private fun concat(a: String , b: String): String {
//        if (a < b) {
//            return a + "#" + b;
//        }
//        return b + "#" + a;
//    }
//
//    private fun fetchMessages() {
//        messageList.clear()
//
//        db.collection("chats")
//            .document(concat(uid, otherId))
//            .collection("messages").get().addOnSuccessListener { sentDocs ->
//
//                for (doc in sentDocs) {
//                    val message = doc.toObject(Message::class.java)
//                    messageList.add(message)
//                }
//                messageList.sortBy { it.timestamp }
//                messageAdapter.notifyDataSetChanged()
//        }
//
//    }
//}

package com.nityapotti.unity.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.nityapotti.unity.MessageAdapter
import com.nityapotti.unity.R
import com.nityapotti.unity.models.Message

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    private lateinit var btnBack: Button
    private lateinit var btnSend: Button
    private lateinit var input: EditText
    private lateinit var otherId: String
    private lateinit var uid: String

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        auth = FirebaseAuth.getInstance()
        input = view.findViewById(R.id.input)
        btnBack = view.findViewById(R.id.btnBack)
        btnSend = view.findViewById(R.id.btnSend)

        // Get passed data
        otherId = arguments?.getString("otherId").toString()
        val otherName = arguments?.getString("otherName")

        val nameText = view.findViewById<TextView>(R.id.name)
        nameText.setText(otherName)

        uid = auth.currentUser?.uid.toString()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        messageAdapter = MessageAdapter(messageList, uid)
        recyclerView.adapter = messageAdapter

        btnBack.setOnClickListener {
            requireActivity().onBackPressed()  // Go back to the previous fragment
        }

        btnSend.setOnClickListener {
            val text = input.text.toString()
            if (text.trim().isNotEmpty()) {
                val currentTimestamp = Timestamp.now()

                db.collection("chats")
                    .document(concat(uid, otherId))
                    .collection("messages")
                    .add(Message(uid, otherId, text, false, currentTimestamp))
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                input.setText("")
                fetchMessages()
            }
        }

        fetchMessages()

        return view
    }

    private fun concat(a: String, b: String): String {
        return if (a < b) a + "#" + b else b + "#" + a
    }

    private fun fetchMessages() {
        messageList.clear()
        db.collection("chats")
            .document(concat(uid, otherId))
            .collection("messages").get().addOnSuccessListener { sentDocs ->
                for (doc in sentDocs) {
                    val message = doc.toObject(Message::class.java)
                    messageList.add(message)
                }
                messageList.sortBy { it.timestamp }
                messageAdapter.notifyDataSetChanged()
            }
    }
}
