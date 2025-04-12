package com.example.mainapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2.launchers.ChatLauncher

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchChatButton = findViewById<Button>(R.id.launch_chat_button)

        launchChatButton.setOnClickListener {
            // Launching the ChatActivity using the library
            ChatLauncher.start(this)
        }
    }
}

