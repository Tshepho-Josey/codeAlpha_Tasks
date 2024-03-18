package com.example.collegealertapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.collegealertapp.databinding.ActivitySignUpBinding
import android.util.Patterns
import androidx.appcompat.app.AppCompatDelegate
import java.util.regex.Pattern

private lateinit var binding: ActivitySignUpBinding
private lateinit var firebaseAuth: FirebaseAuth

// Define a regex pattern for password validation
private val PASSWORD_PATTERN: Pattern =
    Pattern.compile("^" +
            "(?=.*[0-9])" +         // At least one digit
            "(?=.*[a-zA-Z])" +      // At least one letter
            "(?=.*[@#$%^&+=_*-+])" +    // At least one special character
            "(?=\\S+$)" +           // No whitespace allowed
            ".{8,}" +               // At least 8 characters
            "$")

class SignUp : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.Password)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        val Back: TextView = findViewById(R.id.textViewGoBack)


        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (validateEmail(email) && validatePassword(password, confirmPassword)) {
                registerUser(email, password)
            }
        }

        Back.setOnClickListener {
            // Handle navigation to the login page here
            startActivity(Intent(this, Login::class.java))
            finish() // Finish the current activity to prevent going back to the sign-up screen
        }


    }
    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Invalid email address"
            false
        } else {
            true
        }
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        return if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            editTextPassword.error = "Password must contain at least one digit, one Capital letter, one special character, and have no whitespace and contain atleast 8 characters"
            false
        } else if (confirmPassword != password) {
            editTextConfirmPassword.error = "Passwords do not match"
            false
        } else {
            true
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
    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToHome()
            } else {
                showToast("Registration failed: ${task.exception?.message}")
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Optionally finish() this activity to prevent the user from navigating back to the registration screen.
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}