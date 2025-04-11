package com.example.lab2.adapters

import com.example.lab2.models.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R


class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSender) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            SentMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false))
        } else {
            ReceivedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) holder.bind(message)
        else if (holder is ReceivedMessageViewHolder) holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.text_sent)

        fun bind(message: Message) {
            textView.text = message.text // Correct access to 'content'
        }
    }

    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.text_received)

        fun bind(message: Message) {
            textView.text = message.text // Correct access to 'content'
        }
    }
}
