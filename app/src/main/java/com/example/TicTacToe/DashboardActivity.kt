package com.example.TicTacToe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private val addPlayerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val playerOne = data?.getStringExtra("playerOne") ?: "Player One"
            val playerTwo = data?.getStringExtra("playerTwo") ?: "Player Two"

            val gameIntent = Intent(this, MainActivity::class.java)
            gameIntent.putExtra("playerOne", playerOne)
            gameIntent.putExtra("playerTwo", playerTwo)
            startActivity(gameIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.btnPlayAI).setOnClickListener {
            startActivity(Intent(this, PlayAIActivity::class.java))
        }

        findViewById<Button>(R.id.btnPlayHuman).setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            addPlayerLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btnViewPlayers).setOnClickListener {
            startActivity(Intent(this, ViewPlayersActivity::class.java))
        }
    }
}
