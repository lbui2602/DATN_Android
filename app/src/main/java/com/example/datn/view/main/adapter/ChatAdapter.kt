package com.example.datn.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datn.R
import com.example.datn.models.Message

class ChatAdapter(private var messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageText.text = messages[position].message
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }
}
