package com.nityapotti.unity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nityapotti.unity.R
import com.nityapotti.unity.models.Preference
import com.google.android.material.card.MaterialCardView

class UserAdapter(private val userList: List<Preference>, private val onUserClick: (Preference) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTextView)

        val userCard: MaterialCardView = itemView.findViewById(R.id.recyclerUser)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.name
        holder.genderTextView.text = user.gender

        holder.userCard.setOnClickListener {
            onUserClick(user)
        }
    }

    override fun getItemCount(): Int = userList.size
}