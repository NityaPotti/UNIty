package com.nityapotti.unity.views

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nityapotti.unity.R


class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnChat = findViewById<Button>(R.id.btnChat)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")

        btnBack.setOnClickListener {
            finish()
        }

        btnChat.setOnClickListener {

            intent.putExtra("otherId", id)
            intent.putExtra("otherName", name)
            startActivity(intent)
        }

        val gender = intent.getStringExtra("gender")
        val bio = intent.getStringExtra("bio")
        val temperature = intent.getStringExtra("temperature")
        val bedtime = intent.getStringExtra("bedtime")
        val cleanliness = intent.getStringExtra("cleanliness")
        val oncampus = intent.getStringExtra("oncampus")
        val location = intent.getStringExtra("location")
        val llc = intent.getStringExtra("llc")
        val maxrent = intent.getStringExtra("maxrent")

        val nameTextView = findViewById<TextView>(R.id.detailNameTextView)
        val genderTextView = findViewById<TextView>(R.id.detailGenderTextView)
        val bioTextView = findViewById<TextView>(R.id.detailBioTextView)
        val tempTextView = findViewById<TextView>(R.id.Temp)
        val bedTextView = findViewById<TextView>(R.id.Bed)
        val cleTextView = findViewById<TextView>(R.id.Clean)
        val oncTextView = findViewById<TextView>(R.id.OnCampus)
        val locTextView = findViewById<TextView>(R.id.Loc)
        val llcTextView = findViewById<TextView>(R.id.LLC)
        val maxTextView = findViewById<TextView>(R.id.Maxrent)

        val cleanlinessValue = cleanliness?.toIntOrNull()?.div(2) ?: -1

        nameTextView.text = name
        genderTextView.text = gender
        bioTextView.text = bio
        tempTextView.text = "Preferred temperature: " + temperature
        bedTextView.text = "Bedtime: " + bedtime
        cleTextView.text = "Cleaniness: " + cleanlinessValue.toString() + " / 10"
        oncTextView.text = "Will be living: " + oncampus
        if (oncampus == "On campus") {
            locTextView.text = "Location: " + location + " campus"
        }
        else {
            locTextView.text = "Location: Off campus"
        }
        llcTextView.text = "LLC: " + llc
        maxTextView.text = "Max. rent: " + maxrent.toString()
        //TODO: I don't know the scale formula, should ask Kseniia

        btnChat.setOnClickListener({ v ->
            getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, ChatFragment())
                .addToBackStack(null)
                .commit()
        })

    }

}