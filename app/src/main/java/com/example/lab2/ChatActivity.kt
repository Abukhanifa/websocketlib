package com.example.lab2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.adapters.ChatAdapter
import com.example.lab2.databinding.ActivityChatBinding
import com.example.lab2.models.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.decodeHex

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var webSocket: WebSocket
    private lateinit var chatAdapter: ChatAdapter
    private val client = OkHttpClient()

    private val TAG = "WebSocketLog"  // Tag for Logcat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatAdapter = ChatAdapter()
        binding.recyclerView.adapter = chatAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        setupWebSocket()

        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotEmpty()) {
                Log.d(TAG, "Sending message: $message")  // Log the message being sent
                webSocket.send(message)
                chatAdapter.addMessage(Message(message, true))  // Add message to RecyclerView
                binding.messageInput.text.clear()  // Clear input field
            }
        }
    }

    private fun setupWebSocket() {
        val request = Request.Builder()
            .url("wss://echo.websocket.org/")  // WebSocket URL
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "WebSocket Opened: $response")  // Log WebSocket opened
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")  // Log received text message
                runOnUiThread {
                    if (text == "\u00cb") {  // Special predefined message (0xcb)
                        chatAdapter.addMessage(Message("Received special predefined message!", false))
                    } else {
                        chatAdapter.addMessage(Message(text, false))
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Received bytes: ${bytes.hex()}")  // Log received byte message
                runOnUiThread {
                    if (bytes == "cb".decodeHex()) {
                        chatAdapter.addMessage(Message("Received special predefined message!", false))
                    } else {
                        chatAdapter.addMessage(Message(bytes.hex(), false))
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "WebSocket Failure: ${t.message}", t)  // Log error in case of failure
                runOnUiThread {
                    chatAdapter.addMessage(Message("Error: ${t.message}", false))
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1000, "ChatActivity closed")
        client.dispatcher.executorService.shutdown()
    }
}

