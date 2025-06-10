package com.example.TicTacToe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayAIActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var gridLayout: GridLayout
    private val board = Array(9) { ' ' }  // ' ' = empty, 'X' = player, 'O' = AI
    private val PLAYER = 'X'
    private val COMPUTER = 'O'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_ai)

        statusText = findViewById(R.id.statusText)
        gridLayout = findViewById(R.id.gridLayout)

        resetBoard()
    }

    // Called by buttons in XML onClick="onTileClick"
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

        if (tileIndex == -1 || board[tileIndex] != ' ') return
        if (isGameOver()) return

        // Player move
        makeMove(tileIndex, PLAYER)
        updateBoardUI()

        if (checkWin(PLAYER)) {
            statusText.text = "You won!"
            disableBoard()
            return
        } else if (isBoardFull()) {
            statusText.text = "It's a draw!"
            disableBoard()
            return
        }

        statusText.text = "Computer is thinking..."

        // Disable buttons during AI move
        disableBoard()

        // Delay AI move by 1 second
        gridLayout.postDelayed({
            val aiMove = getBestMove()
            if (aiMove != -1) {
                makeMove(aiMove, COMPUTER)
                updateBoardUI()

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

            statusText.text = "Your turn!"
            enableBoard()
        }, 1000)
    }

    fun resetGame(view: View) {
        resetBoard()
        updateBoardUI()
        statusText.text = "Your turn!"
        enableBoard()
    }

    private fun resetBoard() {
        for (i in board.indices) {
            board[i] = ' '
        }
    }

    private fun makeMove(index: Int, player: Char) {
        board[index] = player
    }

    private fun updateBoardUI() {
        for (i in 0 until 9) {
            val button = findViewById<Button>(resources.getIdentifier("button$i", "id", packageName))
            button.text = board[i].toString()
            button.isEnabled = (board[i] == ' ')
        }
    }

    private fun disableBoard() {
        setBoardEnabled(false)
    }

    private fun enableBoard() {
        setBoardEnabled(true)
    }

    private fun setBoardEnabled(enabled: Boolean) {
        for (i in 0 until 9) {
            val button = findViewById<Button>(resources.getIdentifier("button$i", "id", packageName))
            button.isEnabled = enabled && (board[i] == ' ')
        }
    }

    private fun isBoardFull(): Boolean {
        return board.all { it != ' ' }
    }

    private fun isGameOver(): Boolean {
        return checkWin(PLAYER) || checkWin(COMPUTER) || isBoardFull()
    }

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
            if (board[pos[0]] == player && board[pos[1]] == player && board[pos[2]] == player) {
                return true
            }
        }
        return false
    }

    // Simple AI: pick first empty spot (you can improve this)
    private fun getBestMove(): Int {
        for (i in board.indices) {
            if (board[i] == ' ') return i
        }
        return -1
    }
}
