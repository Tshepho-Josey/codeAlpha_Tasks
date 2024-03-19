package com.example.collegealertapp

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.collegealertapp.databinding.ActivityEventDetailsBinding

class Eventdetails : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve event details from intent extras
        val eventName = intent.getStringExtra("eventName")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val location = intent.getStringExtra("location")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set event details to TextViews
        binding.textViewEventName.text = eventName
        binding.textViewDate.text = date
        binding.textViewTime.text = time
        binding.textViewLocation.text = location
        binding.textViewDescription.text = description

        // Load event image if available
        if (!imageUrl.isNullOrEmpty()) {
            try {
                // Decode Base64 encoded image string into a Bitmap
                val imageBytes = android.util.Base64.decode(imageUrl, android.util.Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                // Set Bitmap to ImageView
                binding.imageViewEvent.setImageBitmap(bitmap)

            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        } else {
            // Handle case where imageUrl is null or empty
            binding.imageViewEvent.setImageResource(R.drawable.placeholder_image)
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