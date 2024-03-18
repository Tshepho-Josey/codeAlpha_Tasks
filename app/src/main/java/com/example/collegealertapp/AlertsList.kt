package com.example.collegealertapp


import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AlertsList : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var itemRecyclerView: RecyclerView
    private var itemArrayList: ArrayList<itemDs> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts_list)

        itemRecyclerView = findViewById(R.id.alerts_list)
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.setHasFixedSize(true)

        try {
            getAlertsData()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that occur during data retrieval
        }

    }

    private fun getAlertsData() {
        try {
            db = FirebaseDatabase.getInstance().getReference("details")
            db.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        itemArrayList.clear() // Clear previous data
                        for (eventSnapshot in snapshot.children) {
                            val event = eventSnapshot.getValue(itemDs::class.java)
                            event?.let {
                                itemArrayList.add(it)
                            }
                        }
                        // Set up RecyclerView adapter with the retrieved data
                        itemRecyclerView.adapter = AlertsAdapter(itemArrayList, this@AlertsList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } catch (e: Exception) {
            // Handle any exceptions that occur during data retrieval
            e.printStackTrace()
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