package com.example.TicTacToe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayAIActivity : AppCompatActivity() {

    private lateinit var statusText: TextView         // Displays current game status
    private lateinit var gridLayout: GridLayout       // The 3x3 game grid

    private val board = Array(9) { ' ' }              // Board state: ' ' = empty, 'X' = player, 'O' = AI
    private val PLAYER = 'X'                          // Player symbol
    private val COMPUTER = 'O'                        // Computer symbol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_ai)

        // Initialize views
        statusText = findViewById(R.id.statusText)
        gridLayout = findViewById(R.id.gridLayout)

        resetBoard()  // Initialize board state
    }

    // Triggered when a player taps a tile (linked in XML via android:onClick)
    fun onTileClick(view: View) {
        val button = view as Button
        val tileIndex = when (button.id) {
            R.id.button0 -> 0
            R.id.button1 -> 1
            R.id.button2 -> 2
            R.id.button3 -> 3
            R.id.button4 -> 4
            R.id.button5 -> 5
            R.id.button6 -> 6
            R.id.button7 -> 7
            R.id.button8 -> 8
            else -> -1
        }

        // Ignore click if invalid or tile already used or game is over
        if (tileIndex == -1 || board[tileIndex] != ' ' || isGameOver()) return

        // Player move
        makeMove(tileIndex, PLAYER)
        updateBoardUI()

        // Check if player won
        if (checkWin(PLAYER)) {
            statusText.text = "You won!"
            disableBoard()
            return
        } else if (isBoardFull()) {
            statusText.text = "It's a draw!"
            disableBoard()
            return
        }

        // AI turn with delay for better UX
        statusText.text = "Computer is thinking..."
        disableBoard()

        gridLayout.postDelayed({
            val aiMove = getBestMove()
            if (aiMove != -1) {
                makeMove(aiMove, COMPUTER)
                updateBoardUI()

                // Check if AI won
                if (checkWin(COMPUTER)) {
                    statusText.text = "Computer won!"
                    disableBoard()
                    return@postDelayed
                } else if (isBoardFull()) {
                    statusText.text = "It's a draw!"
                    disableBoard()
                    return@postDelayed
                }
            }

            // Player's turn again
            statusText.text = "Your turn!"
            enableBoard()
        }, 1000)
    }

    // Called from "Reset" button (linked via android:onClick in XML)
    fun resetGame(view: View) {
        resetBoard()
        updateBoardUI()
        statusText.text = "Your turn!"
        enableBoard()
    }

    // Clears the board state
    private fun resetBoard() {
        for (i in board.indices) {
            board[i] = ' '
        }
    }

    // Sets the symbol at a board index
    private fun makeMove(index: Int, player: Char) {
        board[index] = player
    }

    // Updates UI to reflect current board state
    private fun updateBoardUI() {
        for (i in 0 until 9) {
            val button = findViewById<Button>(
                resources.getIdentifier("button$i", "id", packageName)
            )
            button.text = board[i].toString()
            button.isEnabled = (board[i] == ' ') // Enable only empty buttons
        }
    }

    // Enables all empty buttons
    private fun enableBoard() {
        setBoardEnabled(true)
    }

    // Disables all buttons
    private fun disableBoard() {
        setBoardEnabled(false)
    }

    // Helper to set board interactivity
    private fun setBoardEnabled(enabled: Boolean) {
        for (i in 0 until 9) {
            val button = findViewById<Button>(
                resources.getIdentifier("button$i", "id", packageName)
            )
            button.isEnabled = enabled && (board[i] == ' ')
        }
    }

    // Checks if the board is completely filled
    private fun isBoardFull(): Boolean {
        return board.all { it != ' ' }
    }

    // Checks if the game has ended (win or draw)
    private fun isGameOver(): Boolean {
        return checkWin(PLAYER) || checkWin(COMPUTER) || isBoardFull()
    }

    // Checks if the given player has a winning combination
    private fun checkWin(player: Char): Boolean {
        val winPositions = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )
        for (pos in winPositions) {
            if (board[pos[0]] == player &&
                board[pos[1]] == player &&
                board[pos[2]] == player
            ) {
                return true
            }
        }
        return false
    }

    // Very simple AI: chooses the first available empty tile
    private fun getBestMove(): Int {
        for (i in board.indices) {
            if (board[i] == ' ') return i
        }
        return -1 // No valid moves
    }
}
