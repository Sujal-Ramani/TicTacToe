package com.example.TicTacToe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

// Activity to collect player names before starting the Tic Tac Toe game
class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player) // Set the layout for this activity

        // Initialize input fields and button from the layout
        val playerOneInput: EditText = findViewById(R.id.playerOne)
        val playerTwoInput: EditText = findViewById(R.id.playerTwo)
        val saveButton: Button = findViewById(R.id.startGameButton)

        // Set a click listener on the Start Game button
        saveButton.setOnClickListener {
            // Retrieve and trim player names from input fields
            val playerOneName = playerOneInput.text.toString().trim()
            val playerTwoName = playerTwoInput.text.toString().trim()

            // Check if both names are entered
            if (playerOneName.isNotEmpty() && playerTwoName.isNotEmpty()) {
                // Create an intent to pass back the player names to the calling activity
                val resultIntent = Intent()
                resultIntent.putExtra("playerOne", playerOneName)
                resultIntent.putExtra("playerTwo", playerTwoName)

                // Set the result as OK and attach the intent with data
                setResult(Activity.RESULT_OK, resultIntent)

                // Close the AddPlayerActivity and return to the previous activity
                finish()
            } else {
                // Show error messages if any input field is empty
                if (playerOneName.isEmpty()) playerOneInput.error = "Enter Player One Name"
                if (playerTwoName.isEmpty()) playerTwoInput.error = "Enter Player Two Name"
            }
        }
    }
}
