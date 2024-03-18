package com.example.collegealertapp

import android.content.Context
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Get current notification preference (default to enabled)
        val areNotificationsEnabled = sharedPreferences.getBoolean("notifications", false)

        // Get notification switch
        val notificationSwitch = findViewById<Switch>(R.id.notificationSwitch)

        // Set initial state of switch based on notification preference
        notificationSwitch.isChecked = areNotificationsEnabled

        // Set listener to update notification preference when switch state changes
        notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
            // You can handle the notification settings here, such as subscribing or unsubscribing to topics
        }


        // Get current theme preference (default to light mode)
        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)

        // Get theme switch
        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)

        // Set initial state of switch based on theme preference
        themeSwitch.isChecked = isDarkModeEnabled

        // Set listener to update theme preference when switch state changes
        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean("darkMode", isChecked).apply()
            // Restart the activity to apply the new theme
            recreate()
        }
    }
}