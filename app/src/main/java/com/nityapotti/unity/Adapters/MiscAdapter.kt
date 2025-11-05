package com.nityapotti.unity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nityapotti.unity.PreferenceFormActivity
import com.nityapotti.unity.R

class MiscAdapter(
    private val miscItems: MutableList<String>,
) : RecyclerView.Adapter<MiscAdapter.MiscViewHolder>() {

    inner class MiscViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preferenceText: TextView = itemView.findViewById(R.id.dealBreakText)
        val xButton: ImageButton = itemView.findViewById(R.id.xButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiscViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deal_breakers, parent, false)
        return MiscViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiscViewHolder, position: Int) {
        val item = miscItems[position]
        holder.preferenceText.text = item

        holder.xButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val removable = miscItems.elementAt(currentPosition)
//                PreferenceFormActivity.tagSpinnerList.remove(removable)
//                PreferenceFormActivity.tagList.remove(removable)
                removeItemAt(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = miscItems.size

    private fun removeItemAt(position: Int) {
        if (position >= 0 && position < miscItems.size) {
            miscItems.removeAt(position)
            notifyItemRemoved(position)
            if (position < miscItems.size) {
                notifyItemRangeChanged(position, miscItems.size - position)
            }
        }
    }

    fun addItem(item: String) {
        if (item !in miscItems) {
            miscItems.add(item)
            notifyItemInserted(miscItems.size - 1)
        }
    }

    fun clear() {
        val size = miscItems.size
        miscItems.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun updateList(newItems: List<String>) {
        miscItems.clear()
        miscItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getItems(): List<String> {
        return miscItems.toList()
    }
}