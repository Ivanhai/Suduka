package com.example.suduka

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.suduka.DataClasses.CreateRoomRequest
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

var numbers = mutableListOf(mutableListOf(1))

class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        val token = intent.extras?.getString("token")
        val id = intent.extras?.getString("id")

        val board = findViewById<Board>(R.id.board)
        board.drawNumber(numbers)

        MainScope().launch(Dispatchers.IO) {
            if (token != null && id != null) {
                join(id, token, board, supportFragmentManager)
            }
        }
    }
}

suspend fun join(id: String, token: String, board : Board, supportFragmentManager : FragmentManager) {
    ktorSocketClient.webSocket(method = HttpMethod.Get, host = "sudokos.herokuapp.com", port=80, path = "/join/$id", request = {header("Authorization", "Bearer $token")}) {
        board.setOnClickPlaceListener { x,y ->
            val alert = Alert{
                MainScope().launch(Dispatchers.IO) {
                    outgoing.send(Frame.Text("put $y $x $it"))
                }
                numbers[y][x] = it
                board.drawNumber(numbers)
            }
            alert.show(supportFragmentManager, "numbers")
        }
        for(frame in incoming) {
            when(frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    if(text.contains("[")) {
                        numbers = Json.decodeFromString(text)
                        board.drawNumber(numbers)
                    } else if(text.contains("you win!")) {
                        val myDialogFragment = MyDialogFragment()
                        val manager = supportFragmentManager
                        myDialogFragment.show(manager, "myDialog")
                    }
                }
                else -> {
                    throw(Throwable("not text"))
                }
            }
        }
    }
}

class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Вы выиграли")
                .setMessage("рейтинг + 1!")
                .setPositiveButton("ОК") { dialog, id ->
                    dialog.cancel()
                    it.finish()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}