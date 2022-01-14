package com.example.suduka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.suduka.DataClasses.CreateRoomRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FindSession() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_session)

        val token = intent.extras?.getString("token")
        val joinButton = findViewById<Button>(R.id.joinButton)
        joinButton.setOnClickListener {
            println(token)
        }
        val createButton = findViewById<Button>(R.id.createButton)
        val size = findViewById<TextView>(R.id.editTextBoxSize)
        createButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val id = userApi.createRoom(CreateRoomRequest(token ?: throw(Throwable("null error")), size.text.toString().toInt()))
                println(id.id)
            }
        }
    }
}