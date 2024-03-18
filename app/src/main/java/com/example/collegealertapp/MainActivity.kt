package com.example.collegealertapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val back = findViewById<Button>(R.id.Logout)
        val addEvent = findViewById<Button>(R.id.add)
        val upcomingEve = findViewById<Button>(R.id.Upcoming)
        val settings = findViewById<Button>(R.id.settings)

        back.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        addEvent.setOnClickListener {
            startActivity(Intent(this, AddUpcomingEvents::class.java))
        }

        upcomingEve.setOnClickListener {
            startActivity(Intent(this, AlertsList::class.java))
        }

        settings.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
    }
    private fun applyTheme() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
