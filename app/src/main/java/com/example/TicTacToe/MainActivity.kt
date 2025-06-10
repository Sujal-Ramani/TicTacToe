package com.example.TicTacToe

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.TicTacToe.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var dbHelper: DBHelper
    private lateinit var imageViews: List<ImageView>

    private val combinationList = listOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    private var boxPositions = IntArray(9) { 0 }
    private var playerTurn = 1
    private var totalSelectedBoxes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize imageViews AFTER binding is ready
        imageViews = listOf(
            binding.image1, binding.image2, binding.image3,
            binding.image4, binding.image5, binding.image6,
            binding.image7, binding.image8, binding.image9
        )

        dbHelper = DBHelper(this)

        val playerOne = intent.getStringExtra("playerOne") ?: "Player 1"
        val playerTwo = intent.getStringExtra("playerTwo") ?: "Player 2"
        binding.playerOneName.text = playerOne
        binding.playerTwoName.text = playerTwo


        mediaPlayer = MediaPlayer.create(this, R.raw.click).apply {
            isLooping = false
            setVolume(1.0f, 1.0f)
        }

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (boxPositions[index] == 0) {
                    performMove(imageView, index)
                }
            }
        }
    }

    private fun performMove(imageView: ImageView, position: Int) {
        boxPositions[position] = playerTurn
        playClickSound()

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.xx)
        } else {
            imageView.setImageResource(R.drawable.oo)
        }

        totalSelectedBoxes++

        if (checkWin()) {
            val winnerName = if (playerTurn == 1) binding.playerOneName.text else binding.playerTwoName.text
            showResultDialog("$winnerName is a Winner!")
        } else if (totalSelectedBoxes == 9) {
            showResultDialog("It's a Draw!")
        } else {
            playerTurn = if (playerTurn == 1) 2 else 1
        }
    }

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

    private fun showResultDialog(message: String) {
        val winnerName = if (message.contains("Winner")) {
            if (playerTurn == 1) binding.playerOneName.text.toString()
            else binding.playerTwoName.text.toString()
        } else {
            "Draw"
        }

        val playerNames = "${binding.playerOneName.text} vs ${binding.playerTwoName.text}"
        val currentTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

        dbHelper.addGame(playerNames, currentTime, winnerName)

        val dialog = ResultDialog(this, message) {
            resetGame()
        }
        dialog.setCancelable(false)
        dialog.show()
    }


    private fun resetGame() {
        boxPositions.fill(0)
        totalSelectedBoxes = 0
        playerTurn = 1
        imageViews.forEach { it.setImageResource(0) } // clear images
    }

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
