package com.nityapotti.unity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nityapotti.unity.R

class YearAdapter(
    private val yearItems: MutableList<String>,
) : RecyclerView.Adapter<YearAdapter.DealBreakViewHolder>() {

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
        val item = yearItems[position]
        holder.preferenceText.text = item

        // Use adapterPosition to get the current position at click time
        holder.xButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                removeItemAt(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = yearItems.size

    private fun removeItemAt(position: Int) {
        if (position >= 0 && position < yearItems.size) {
            yearItems.removeAt(position)
            notifyItemRemoved(position)
            // Optional: notify about range change for smoother animations
            if (position < yearItems.size) {
                notifyItemRangeChanged(position, yearItems.size - position)
            }
        }
    }

    fun addItem(item: String) {
        if (item !in yearItems) {
            yearItems.add(item)
            notifyItemInserted(yearItems.size - 1)
        }
    }

    fun clear() {
        val size = yearItems.size
        yearItems.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun updateList(newItems: List<String>) {
        yearItems.clear()
        yearItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getItems(): List<String> {
        return yearItems.toList()
    }
}