package com.example.suduka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var numbers = mutableListOf<MutableList<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, EnterActivity::class.java))
//        val board = findViewById<Board>(R.id.board)
//        numbers = MutableList(9){
//            MutableList(9){
//                0
//            }
//        }
//        board.drawNumber(numbers)
//        board.setOnClickPlaceListener { x,y ->
//            val alert = Alert{
//                numbers[y][x] = it
//                board.drawNumber(numbers)
//            }
//            alert.show(supportFragmentManager, "numbers")
//        }
    }
}