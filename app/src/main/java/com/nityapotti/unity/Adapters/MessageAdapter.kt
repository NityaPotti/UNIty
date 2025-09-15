package com.nityapotti.unity.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nityapotti.unity.R
import com.nityapotti.unity.models.Message

class MessageAdapter(private val messageList: List<Message>, private val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message2, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SentViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(message)
        }
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.findViewById<TextView>(R.id.text).text = message.text
        }
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.findViewById<TextView>(R.id.text).text = message.text
        }
    }
}
