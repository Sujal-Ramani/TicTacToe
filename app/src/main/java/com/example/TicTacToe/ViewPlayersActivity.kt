package com.example.TicTacToe

// ViewPlayersActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class ViewPlayersActivity : AppCompatActivity() {

    // Database helper to access stored player data
    private lateinit var dbHelper: DBHelper

    // Adapter to bind player data to the RecyclerView
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_players)

        // Initialize the database helper
        dbHelper = DBHelper(this)

        // Reference to the RecyclerView from layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Reference to the "Clear Players" button from layout
        val clearButton = findViewById<Button>(R.id.clearPlayersButton)

        // Set up the adapter with all players from the database
        adapter = PlayerAdapter(dbHelper.getAllPlayers())

        // Set layout manager to show items in vertical list
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = adapter

        // Set click listener to clear all players when button is clicked
        clearButton.setOnClickListener {
            dbHelper.clearPlayers() // Delete all player records from DB
            adapter.updatePlayers(emptyList()) // Clear the list in the UI
        }
    }
}
