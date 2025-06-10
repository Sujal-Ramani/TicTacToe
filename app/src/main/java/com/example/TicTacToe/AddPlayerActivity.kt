package com.example.TicTacToe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val playerOneInput: EditText = findViewById(R.id.playerOne)
        val playerTwoInput: EditText = findViewById(R.id.playerTwo)
        val saveButton: Button = findViewById(R.id.startGameButton)

        saveButton.setOnClickListener {
            val playerOneName = playerOneInput.text.toString().trim()
            val playerTwoName = playerTwoInput.text.toString().trim()

            if (playerOneName.isNotEmpty() && playerTwoName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("playerOne", playerOneName)
                resultIntent.putExtra("playerTwo", playerTwoName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                if (playerOneName.isEmpty()) playerOneInput.error = "Enter Player One Name"
                if (playerTwoName.isEmpty()) playerTwoInput.error = "Enter Player Two Name"
            }
        }
    }
}
