package com.example.datn.view.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datn.databinding.ItemMessageReceivedBinding
import com.example.datn.databinding.ItemMessageSentBinding
import com.example.datn.models.message.Message
import com.example.datn.util.Util

class ChatAdapter(private val userId: String) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == userId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(inflater, parent, false)
            SentMessageViewHolder(binding, parent.context)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(inflater, parent, false)
            ReceivedMessageViewHolder(binding, parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            val type = if (position == 0 || message.senderId != getItem(position - 1).senderId) 1 else 0
            holder.bind(message, type)
        }
    }

    fun updateMessages(newMessages: List<Message>) {
        submitList(newMessages.toList())
    }

    class SentMessageViewHolder(private val binding: ItemMessageSentBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessageSent.text = message.message
            binding.tvTimeSent.text = Util.formatTime(message.createdAt)
        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemMessageReceivedBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, type: Int) {
            binding.tvMessageReceived.text = message.message
            binding.tvTimeReceived.text = Util.formatTime(message.createdAt)

            if (type == 1) {
                binding.cvAvatar.visibility = View.VISIBLE
                Glide.with(context).load(Util.url + message.senderImage).into(binding.imgAvatar)
                binding.tvName.text = message.senderName
                binding.tvName.visibility = View.VISIBLE
            } else {
                binding.tvName.visibility = View.GONE
                binding.cvAvatar.visibility = View.INVISIBLE
            }
        }
    }

    class MessageDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem._id == newItem._id // hoặc oldItem.createdAt == newItem.createdAt nếu không có _id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}

