package com.example.TicTacToe

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater

// Adapter class for displaying a list of Player objects in a RecyclerView
class PlayerAdapter(private var players: List<Player>) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    // ViewHolder class that holds the views for each item in the list
    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.playerName)     // Shows player names (e.g., "Alice vs Bob")
        val timeTextView: TextView = itemView.findViewById(R.id.gameTime)       // Shows the time when the game was played
        val winnerTextView: TextView = itemView.findViewById(R.id.winnerName)   // Shows the name of the winner
    }

    // Inflates the layout for each list item and returns a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    // Binds data to the views in each ViewHolder
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.nameTextView.text = player.name
        holder.timeTextView.text = "Time: ${player.time}"
        holder.winnerTextView.text = "Winner: ${player.winner}"
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int = players.size

    // Updates the list of players and refreshes the RecyclerView
    fun updatePlayers(newPlayers: List<Player>) {
        players = newPlayers
        notifyDataSetChanged()
    }
}
