package com.example.suduka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.suduka.DataClasses.CreateRoomRequest
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

var numbers = mutableListOf<MutableList<Int>>()

class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        val token = intent.extras?.getString("token")
        val id = intent.extras?.getString("id")

        val board = findViewById<Board>(R.id.board)
        board.setOnClickPlaceListener { x,y ->
             val alert = Alert{
               numbers[y][x] = it
                board.drawNumber(numbers)
             }
            alert.show(supportFragmentManager, "numbers")
        }

        GlobalScope.launch(Dispatchers.IO) {
            if (token != null && id != null) {
                join(id, token, board)
            }
        }
    }
}

suspend fun join(id: String, token: String, board : Board) {
    ktorSocketClient.webSocket(method = HttpMethod.Get, host = "sudokos.herokuapp.com", port=80, path = "/join/$id", request = {header("Authorization", "Bearer $token")}) {
        for(frame in incoming) {
            when(frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    numbers = Json.decodeFromString(text)
                    board.drawNumber(Json.decodeFromString(text))
                }
                else -> {
                    throw(Throwable("not text"))
                }
            }
        }
    }
}