package com.example.suduka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        val token = intent.extras?.getString("token")
        val context = this

        val games = findViewById<ListView>(R.id.games)
        MainScope().launch {
            if (token != null) {
                val list = userApi.getGames(token)
                val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list.list.entries.toTypedArray())
                games.setOnItemClickListener { _, _, position, _ ->
                    val element = adapter.getItem(position)
                    val intent = Intent(context, BoardActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("id", element?.key)
                    startActivity(intent)
                }
                games.adapter = adapter
            }
        }
    }
}