//package com.nityapotti.unity.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.nityapotti.unity.R
//
//class TagAdapter(
//    private val tagItems: MutableList<String>,
//) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {
//
//    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val preferenceText: TextView = itemView.findViewById(R.id.dealBreakText)
//        val xButton: ImageButton = itemView.findViewById(R.id.xButton)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_deal_breakers, parent, false)
//        return TagViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
//        val item = tagItems[position]
//        holder.preferenceText.text = item
//
//        holder.xButton.setOnClickListener {
//            val currentPosition = holder.adapterPosition
//            if (currentPosition != RecyclerView.NO_POSITION) {
//                removeItemAt(currentPosition)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = tagItems.size
//
//    private fun removeItemAt(position: Int) {
//        if (position >= 0 && position < tagItems.size) {
//            tagItems.removeAt(position)
//            notifyItemRemoved(position)
//            if (position < tagItems.size) {
//                notifyItemRangeChanged(position, tagItems.size - position)
//            }
//        }
//    }
//
//    fun addItem(item: String) {
//        if (item !in tagItems) {
//            tagItems.add(item)
//            notifyItemInserted(tagItems.size - 1)
//        }
//    }
//
//    fun clear() {
//        val size = tagItems.size
//        tagItems.clear()
//        notifyItemRangeRemoved(0, size)
//    }
//
//    fun updateList(newItems: List<String>) {
//        tagItems.clear()
//        tagItems.addAll(newItems)
//        notifyDataSetChanged()
//    }
//
//    fun getItems(): List<String> {
//        return tagItems.toList()
//    }
//}