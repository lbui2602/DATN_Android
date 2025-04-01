package com.example.datn.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datn.R
import com.example.datn.databinding.ItemMessageReceivedBinding
import com.example.datn.databinding.ItemMessageSentBinding
import com.example.datn.models.message.Message
import com.example.datn.util.Util

class ChatAdapter(private var messages: List<Message>, private val userId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId.equals(userId)) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(inflater, parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(inflater, parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            if(position==0){
                holder.bind(message,1)
            }else if(!message.senderId.equals(messages[position-1].senderId)){
                holder.bind(message,1)
            }else if(position < messages.size && message.senderId.equals(messages[position+1].senderId)){
                holder.bind(message,2)
            }else{
                holder.bind(message,3)
            }
        } else if (holder is ReceivedMessageViewHolder) {
            if(position==0){
                holder.bind(message,1)
            }else if(!message.senderId.equals(messages[position-1].senderId)){
                holder.bind(message,1)
            }else if(position < messages.size-1 && message.senderId.equals(messages[position+1].senderId)){
                holder.bind(message,2)
            }else{
                holder.bind(message,3)
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    class SentMessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,type : Int) {
            binding.tvMessageSent.text = message.message
        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,type : Int) {
            binding.tvMessageReceived.text = message.message
            binding.tvTimeReceived.text = Util.formatTime(message.createdAt)
            if(type ==1){
                binding.tvName.text = message.senderName
                binding.tvName.visibility = View.VISIBLE
            }
        }
    }
}
