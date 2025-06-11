package com.example.TicTacToe

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// DBHelper class to manage SQLite database for storing game history
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PlayerDB"          // Database file name
        private const val DATABASE_VERSION = 2                // Database version (increment on schema change)

        const val TABLE_NAME = "Players"                      // Table name
        const val COLUMN_ID = "id"                            // Primary key column
        const val COLUMN_NAME = "name"                        // Column for player names
        const val COLUMN_TIME = "time"                        // Column for game time
        const val COLUMN_WINNER = "winner"                    // Column for winner's name
    }

    // Called when the database is first created
    override fun onCreate(db: SQLiteDatabase) {
        // SQL statement to create the Players table
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_WINNER TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    // Called when the database needs to be upgraded (e.g., version is increased)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing table and recreate it (simplest upgrade strategy)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Inserts a new game record into the database
    fun addGame(playerNames: String, time: String, winner: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, playerNames)
            put(COLUMN_TIME, time)
            put(COLUMN_WINNER, winner)
        }
        val result = db.insert(TABLE_NAME, null, values) // Insert new record
        db.close()
        return result != -1L // Return true if insert succeeded
    }

    // Retrieves all game records from the database and returns them as a list of Player objects
    fun getAllPlayers(): List<Player> {
        val playerList = mutableListOf<Player>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        // Iterate over all rows and create Player objects
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                val winner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WINNER))
                playerList.add(Player(id, name, time, winner))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return playerList
    }

    // Deletes all records from the Players table
    fun clearPlayers() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null) // Delete all rows
        db.close()
    }
}
