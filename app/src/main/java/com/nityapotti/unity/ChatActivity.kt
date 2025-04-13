package com.nityapotti.unity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp


class ChatActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        input = findViewById(R.id.input)
        btnBack = findViewById(R.id.btnBack)
        btnSend = findViewById(R.id.btnSend)

        otherId = intent.getStringExtra("otherId").toString()
        val otherName = intent.getStringExtra("otherName")

        val nameText = findViewById<TextView>(R.id.name)

        nameText.setText(otherName)

        uid = auth.currentUser?.uid.toString()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageAdapter = MessageAdapter(messageList, uid.toString())

        recyclerView.adapter = messageAdapter

        btnBack.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {
            val text = input.getText().toString()
            if (text.trim().length > 0) {

                val currentTimestamp = Timestamp.now()

                db.collection("chats")
                    .document(concat(uid, otherId))
                    .collection("messages")
                    .add(Message(uid, otherId, text, false, currentTimestamp))
                    .addOnFailureListener { e: Exception ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            }
            input.setText("")

            fetchMessages()

        }

        fetchMessages()

    }

    private fun concat(a: String , b: String): String {
        if (a < b) {
            return a + "#" + b;
        }
        return b + "#" + a;
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