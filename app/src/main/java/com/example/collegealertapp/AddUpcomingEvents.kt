package com.example.collegealertapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.collegealertapp.databinding.ActivityAddUpcomingEventsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AddUpcomingEvents : AppCompatActivity() {


    var sImage: String? = ""
    private lateinit var db: DatabaseReference
    private lateinit var binding: ActivityAddUpcomingEventsBinding

    // ActivityResultLauncher for handling image selection
    private val activityResultLauncher =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                try {
                    // Convert selected image to base64 string
                    val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
                    val myBitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    myBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    sImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
                    binding.imagePreview.setImageBitmap(myBitmap)
                    inputStream!!.close()

                    Toast.makeText(this, "Image Selected", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpcomingEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        db = FirebaseDatabase.getInstance().getReference("details")

        // Subscribe to a topic for receiving notifications
        FirebaseMessaging.getInstance().subscribeToTopic("events")
    }

    fun Submit_Data(view: View) {
        val _date = binding.dateInput.text.toString()
        val _time = binding.timeInput.text.toString()
        val _location = binding.locationInput.text.toString()
        val _EventName = binding.eventNameInput.text.toString()
        val _Description = binding.descriptionInput.text.toString()

        // Check if any required field is empty
        if (_date.isEmpty() || _time.isEmpty() || _location.isEmpty() || _EventName.isEmpty() || _Description.isEmpty() || sImage.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return  // Exit the function if any field is empty or image is not selected
        }

        /*Check if the selected image is PNG
        val isPngImage = sImage!!.startsWith("data:image/png;base64,")

        if (!isPngImage) {
            Toast.makeText(this, "Please select a PNG image", Toast.LENGTH_SHORT).show()
            return  // Exit the function if the selected image is not PNG
        }*/

        // Create itemDs object with event details
        val detail = itemDs(_date, _time, _location, _EventName, _Description, sImage)

        // Push details to Firebase database
        val databaseReference = FirebaseDatabase.getInstance().reference
        val id = databaseReference.push().key
        db.child(id.toString()).setValue(detail)
            .addOnSuccessListener {
                // Clear input fields after successful submission
                binding.dateInput.text.clear()
                binding.timeInput.text.clear()
                binding.locationInput.text.clear()
                binding.eventNameInput.text.clear()
                binding.descriptionInput.text.clear()
                sImage = ""
                Toast.makeText(this, "Details submitted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Details not submitted", Toast.LENGTH_SHORT).show()
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

    fun Events(view: View){
        startActivity(Intent(this, AlertsList::class.java))
        finish()
    }

    fun Select_Image(view: View) {
        // Start activity to select image from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityResultLauncher.launch(intent)
    }
}