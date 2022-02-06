package com.ivanhai.suduka

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
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
        val status = findViewById<TextView>(R.id.statusView)
        status.text = "session id - $id\nplayers - 1"
        board.drawNumber(numbers)

        MainScope().launch(Dispatchers.IO) {
            if (token != null && id != null) {
                join(id, token, board, supportFragmentManager, status)
            }
        }
    }
}

suspend fun join(id: String, token: String, board : Board, supportFragmentManager : FragmentManager, status: TextView) {
    ktorSocketClient.webSocket(method = HttpMethod.Get, host = "sudokos.herokuapp.com", port=80, path = "/join/$id", request = {header("Authorization", "Bearer $token")}) {
        board.setOnClickPlaceListener { x,y ->
            val alert = Alert(numbers.size){
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
                    val json = Json.decodeFromString<Map<String, String>>(text)
                    println(json)
                    when(json["type"]) {
                        "board" -> {
                            numbers = Json.decodeFromString(json["board"]!!)
                            board.drawNumber(numbers)
                        }
                        "winner" -> {
                            val myDialogFragment = WinDialogFragment()
                            val manager = supportFragmentManager
                            myDialogFragment.show(manager, "win")
                        }
                        "lose" -> {
                            val myDialogFragment = LoseDialogFragment()
                            val manager = supportFragmentManager
                            myDialogFragment.show(manager, "lose")
                        }
                        "joined" -> {
                            println(json)
                            withContext(Dispatchers.Main) {
                                status.text = "${json["uuid"]} joined, now players - ${json["size"]}"
                                if(json["size"]?.toInt()!! >= 2) {
                                    status.text = "loading game..."
                                    status.visibility = View.GONE
                                    board.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                else -> {
                    throw(Throwable("not text"))
                }
            }
        }
    }
}

class WinDialogFragment : DialogFragment() {
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

class LoseDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Вы проиграли")
                .setMessage("рейтинг - 1!")
                .setPositiveButton("ОК") { dialog, id ->
                    dialog.cancel()
                    it.finish()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}