package com.example.collegealertapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.example.collegealertapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply user's theme preference
        applyTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val register = findViewById<TextView>(R.id.SignUp)
        val googleSignInButton = findViewById<SignInButton>(R.id.signInButton)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.Login.setOnClickListener {
            val email = binding.Email.text.toString()
            val pass = binding.Password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                signInUser(email, pass)
                navigateToHome()
            } else {
                showToast("Fill all fields")
            }
        }



        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set click listener for Google Sign In button
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // Set click listener for Register text
        register.setOnClickListener {
            // Proceed to the sign-up page
            startActivity(Intent(this, SignUp::class.java))
            finish() // Finish current activity to prevent going back to the authentication screen
        }

        // Set click listener for Login button
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

    // Handle sign-in result for Google Sign In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(this, "Logged in with Google: ${account?.email}", Toast.LENGTH_SHORT).show()

                // Proceed to the dashboard
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Finish current activity to prevent going back to the authentication screen
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                handleSuccessfulLogin()
            } else {
                handleUnsuccessfulLogin(task.exception?.message)
            }
        }
    }

    private fun handleSuccessfulLogin() {
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        if (currentUser?.email == "josey1mogashoa@gmail.com" && currentUser.isEmailVerified) {
            // Admin login logic (example: admin email and specific password check)
            // Handle admin-specific functionality here if needed
        } else {
            // navigateToHome(currentUser)
            showToast("Welcome to College Alerts $currentUser")
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

    private fun handleUnsuccessfulLogin(errorMessage: String?) {
        showToast("Login failed: $errorMessage")
        // Optional: Redirect the user to the login screen again in case of failure
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 123 // Request code for Google Sign In
    }
}