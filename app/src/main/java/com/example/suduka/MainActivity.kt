package com.example.suduka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var numbers = mutableListOf<MutableList<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val board = findViewById<Board>(R.id.board)
        board.drawNumber(listOf(listOf(1, 2, 3, 4), listOf(2, 1, 4, 3)))
        board.setOnClickPlaceListener { x,y ->
            numbers[y][x] = 1
            board.drawNumber(numbers)
        }
    }
}