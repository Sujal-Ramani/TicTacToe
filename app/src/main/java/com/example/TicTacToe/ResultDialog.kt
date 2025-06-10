package com.example.TicTacToe

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultDialog(
    context: Context,
    private val message: String,
    private val mainActivity: () -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_dialog)

        val messageText: TextView = findViewById(R.id.messageText)!!
        val startAgainButton: Button = findViewById(R.id.startAgainButton)!!

        messageText.text = message

        startAgainButton.setOnClickListener {
            mainActivity()
            dismiss()
        }
    }
}
