package com.example.TicTacToe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var loadingLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize the loading layout (e.g., a loading spinner or animation)
        loadingLayout = findViewById(R.id.loadingLayout)

        // Initially hide the loading layout
        loadingLayout.visibility = View.GONE

        // Show the loading layout after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            loadingLayout.visibility = View.VISIBLE
        }, 1500)

        // Navigate to DashboardActivity after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, DashboardActivity::class.java)) // Launch main dashboard
            finish() // Close splash screen so it's not in the back stack
        }, 5000)
    }
}
