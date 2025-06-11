package com.example.TicTacToe

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.TicTacToe.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding       // View binding for layout elements
    private lateinit var mediaPlayer: MediaPlayer           // Media player for click sound
    private lateinit var dbHelper: DBHelper                 // Database helper instance
    private lateinit var imageViews: List<ImageView>        // List of image views (the game board cells)

    // All possible winning combinations for Tic Tac Toe
    private val combinationList = listOf(
        intArrayOf(0, 1, 2), // Top row
        intArrayOf(3, 4, 5), // Middle row
        intArrayOf(6, 7, 8), // Bottom row
        intArrayOf(0, 3, 6), // Left column
        intArrayOf(1, 4, 7), // Middle column
        intArrayOf(2, 5, 8), // Right column
        intArrayOf(0, 4, 8), // Left-to-right diagonal
        intArrayOf(2, 4, 6)  // Right-to-left diagonal
    )

    private var boxPositions = IntArray(9) { 0 }  // Tracks state of each cell: 0 = empty, 1 = Player 1, 2 = Player 2
    private var playerTurn = 1                    // Tracks which player's turn it is (1 or 2)
    private var totalSelectedBoxes = 0            // Tracks total number of moves made

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize image view references for the game grid
        imageViews = listOf(
            binding.image1, binding.image2, binding.image3,
            binding.image4, binding.image5, binding.image6,
            binding.image7, binding.image8, binding.image9
        )

        // Initialize database helper
        dbHelper = DBHelper(this)

        // Get player names from intent or set default values
        val playerOne = intent.getStringExtra("playerOne") ?: "Player 1"
        val playerTwo = intent.getStringExtra("playerTwo") ?: "Player 2"
        binding.playerOneName.text = playerOne
        binding.playerTwoName.text = playerTwo

        // Set up the media player for click sound
        mediaPlayer = MediaPlayer.create(this, R.raw.click).apply {
            isLooping = false
            setVolume(1.0f, 1.0f)
        }

        // Set click listeners for each image view (cell)
        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (boxPositions[index] == 0) { // If cell is empty
                    performMove(imageView, index) // Make a move
                }
            }
        }
    }

    // Handles a player's move
    private fun performMove(imageView: ImageView, position: Int) {
        boxPositions[position] = playerTurn // Update the game state
        playClickSound() // Play click sound

        // Update UI with player's symbol
        imageView.setImageResource(if (playerTurn == 1) R.drawable.xx else R.drawable.oo)

        totalSelectedBoxes++ // Increment move count

        // Check for win or draw
        if (checkWin()) {
            val winnerName = if (playerTurn == 1) binding.playerOneName.text else binding.playerTwoName.text
            showResultDialog("$winnerName is a Winner!")
        } else if (totalSelectedBoxes == 9) {
            showResultDialog("It's a Draw!")
        } else {
            playerTurn = if (playerTurn == 1) 2 else 1 // Switch turn
        }
    }

    // Checks if current player has won
    private fun checkWin(): Boolean {
        for (combo in combinationList) {
            val (a, b, c) = combo
            if (boxPositions[a] != 0 &&
                boxPositions[a] == boxPositions[b] &&
                boxPositions[b] == boxPositions[c]
            ) {
                return true
            }
        }
        return false
    }

    // Displays the result dialog and saves the game to the database
    private fun showResultDialog(message: String) {
        // Determine the winner name or if it was a draw
        val winnerName = if (message.contains("Winner")) {
            if (playerTurn == 1) binding.playerOneName.text.toString()
            else binding.playerTwoName.text.toString()
        } else {
            "Draw"
        }

        // Build player names as a string and get current time
        val playerNames = "${binding.playerOneName.text} vs ${binding.playerTwoName.text}"
        val currentTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

        // Save game result to database
        dbHelper.addGame(playerNames, currentTime, winnerName)

        // Show result dialog and reset game when dismissed
        val dialog = ResultDialog(this, message) {
            resetGame()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    // Resets the game state and clears the board
    private fun resetGame() {
        boxPositions.fill(0)
        totalSelectedBoxes = 0
        playerTurn = 1
        imageViews.forEach { it.setImageResource(0) } // Clear all images
    }

    // Plays click sound when a cell is tapped
    private fun playClickSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            try {
                mediaPlayer.prepare()
            } catch (e: IOException) {
                Log.e("MainActivity", "Error preparing MediaPlayer", e)
            }
        }
        mediaPlayer.start()
    }
}
