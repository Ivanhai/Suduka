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
        val idtext = findViewById<TextView>(R.id.editTextSession).text
        joinButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                if (token != null) {
                    userApi.joinRoom(idtext.toString(), token)
                }
            }
        }
        val createButton = findViewById<Button>(R.id.createButton)
        val size = findViewById<TextView>(R.id.editTextBoxSize)
        createButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val id = userApi.createRoom(CreateRoomRequest(size.text.toString().toInt()), token ?: throw(Throwable("null error")))
                println(id.id)
                userApi.joinRoom(id.id, token)
            }
        }
    }
}