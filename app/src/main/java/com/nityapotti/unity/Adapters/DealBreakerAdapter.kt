package com.nityapotti.unity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nityapotti.unity.R

class DealBreakerAdapter(
    private val dealBreakItems: MutableList<String>,
) : RecyclerView.Adapter<DealBreakerAdapter.DealBreakViewHolder>() {

    inner class DealBreakViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preferenceText: TextView = itemView.findViewById(R.id.dealBreakText)
        val xButton: ImageButton = itemView.findViewById(R.id.xButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealBreakViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deal_breakers, parent, false)
        return DealBreakViewHolder(view)
    }

    override fun onBindViewHolder(holder: DealBreakViewHolder, position: Int) {
        val item = dealBreakItems[position]
        holder.preferenceText.text = item

        // Use adapterPosition to get the current position at click time
        holder.xButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                removeItemAt(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = dealBreakItems.size

    private fun removeItemAt(position: Int) {
        if (position >= 0 && position < dealBreakItems.size) {
            dealBreakItems.removeAt(position)
            notifyItemRemoved(position)
            // Optional: notify about range change for smoother animations
            if (position < dealBreakItems.size) {
                notifyItemRangeChanged(position, dealBreakItems.size - position)
            }
        }
    }

    fun addItem(item: String) {
        if (item !in dealBreakItems) {
            dealBreakItems.add(item)
            notifyItemInserted(dealBreakItems.size - 1)
        }
    }

    fun clear() {
        val size = dealBreakItems.size
        dealBreakItems.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun updateList(newItems: List<String>) {
        dealBreakItems.clear()
        dealBreakItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getItems(): List<String> {
        return dealBreakItems.toList()
    }
}