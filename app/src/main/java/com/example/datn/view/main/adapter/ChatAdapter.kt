package com.example.datn.view.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
            SentMessageViewHolder(binding,parent.context)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(inflater, parent, false)
            ReceivedMessageViewHolder(binding,parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            val type = if (position == 0 || messages[position].senderId != messages[position - 1].senderId) {
                1 // Tin nhắn đầu tiên của một nhóm
            } else {
                0 // Các tin nhắn tiếp theo trong nhóm
            }
            Log.e("Receiver",message.message.toString()+position+" - "+type)
            holder.bind(message, type)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    class SentMessageViewHolder(private val binding: ItemMessageSentBinding,val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessageSent.text = message.message
            binding.tvTimeSent.text = Util.formatTime(message.createdAt)
        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemMessageReceivedBinding,val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message,type : Int) {
            binding.tvMessageReceived.text = message.message
            binding.tvTimeReceived.text = Util.formatTime(message.createdAt)
            if(type == 1){
                binding.cvAvatar.visibility = View.VISIBLE
                Glide.with(context).load(Util.url+message.senderImage).into(binding.imgAvatar)
                binding.tvName.text = message.senderName
                binding.tvName.visibility = View.VISIBLE
            }else{
                binding.tvName.visibility = View.GONE
                binding.cvAvatar.visibility = View.INVISIBLE
            }
        }
    }
}
