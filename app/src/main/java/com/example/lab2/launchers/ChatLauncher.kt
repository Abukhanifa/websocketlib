package com.example.lab2.launchers
import android.content.Context
import android.content.Intent
import com.example.lab2.ChatActivity

object ChatLauncher {
    fun start(context: Context) {
        val intent = Intent(context, ChatActivity::class.java)
        context.startActivity(intent)
    }
}
