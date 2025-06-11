package com.example.TicTacToe

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

// Custom dialog to display the game result and restart the game
class ResultDialog(
    context: Context,                     // Context from the calling activity
    private val message: String,         // Message to show (e.g., "You won!" or "It's a draw!")
    private val mainActivity: () -> Unit // Lambda function to restart the game
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_dialog) // Set the custom dialog layout

        // Find and bind views from the layout
        val messageText: TextView = findViewById(R.id.messageText)!!
        val startAgainButton: Button = findViewById(R.id.startAgainButton)!!

        // Display the result message
        messageText.text = message

        // Restart the game when "Start Again" button is clicked
        startAgainButton.setOnClickListener {
            mainActivity() // Calls the passed lambda function
            dismiss()      // Closes the dialog
        }
    }
}
