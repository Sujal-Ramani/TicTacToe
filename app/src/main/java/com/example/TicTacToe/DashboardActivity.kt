package com.example.TicTacToe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

// Main dashboard screen where user chooses how to play the game
class DashboardActivity : AppCompatActivity() {

    // Launcher to handle result from AddPlayerActivity using Activity Result API
    private val addPlayerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Check if result is OK (i.e., players successfully entered names)
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Retrieve player names or use default values
            val playerOne = data?.getStringExtra("playerOne") ?: "Player One"
            val playerTwo = data?.getStringExtra("playerTwo") ?: "Player Two"

            // Start MainActivity (the game screen) and pass player names
            val gameIntent = Intent(this, MainActivity::class.java)
            gameIntent.putExtra("playerOne", playerOne)
            gameIntent.putExtra("playerTwo", playerTwo)
            startActivity(gameIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard) // Set the dashboard layout

        // Button to play against AI
        findViewById<Button>(R.id.btnPlayAI).setOnClickListener {
            startActivity(Intent(this, PlayAIActivity::class.java)) // Launch AI game activity
        }

        // Button to play against a human (starts AddPlayerActivity to get names)
        findViewById<Button>(R.id.btnPlayHuman).setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            addPlayerLauncher.launch(intent) // Launch AddPlayerActivity for result
        }

        // Button to view the list of previous players or game history
        findViewById<Button>(R.id.btnViewPlayers).setOnClickListener {
            startActivity(Intent(this, ViewPlayersActivity::class.java)) // Launch player view screen
        }
    }
}
