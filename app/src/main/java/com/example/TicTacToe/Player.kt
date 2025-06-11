package com.example.TicTacToe

// Data class representing a game record or player entry in the database
data class Player(
    val id: Int,        // Unique ID for the game (auto-incremented primary key)
    val name: String,   // Names of the two players (e.g., "Alice vs Bob")
    val time: String,   // Date and time when the game was played
    val winner: String  // Name of the winner or "Draw"
)
