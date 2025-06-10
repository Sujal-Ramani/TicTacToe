package com.example.TicTacToe

// ViewPlayersActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class ViewPlayersActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_players)

        dbHelper = DBHelper(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val clearButton = findViewById<Button>(R.id.clearPlayersButton)

        adapter = PlayerAdapter(dbHelper.getAllPlayers())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        clearButton.setOnClickListener {
            dbHelper.clearPlayers()
            adapter.updatePlayers(emptyList())
        }
    }
}

