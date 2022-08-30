package com.example.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatRecyclerViewAdapter : ListAdapter<Chat, ChatRecyclerViewAdapter.ChatViewHolder>(
    object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

    }
) {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val itemAtIndex = getItem(position)
        holder.tvName.text = itemAtIndex.userName
        holder.tvMessage.text = itemAtIndex.message
    }
}