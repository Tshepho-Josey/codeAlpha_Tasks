package com.example.collegealertapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AlertsAdapter(private val alertList: ArrayList<itemDs>, private val context: Context) : RecyclerView.Adapter<AlertsAdapter.AlertHolder>() {

    class AlertHolder(alertView: View) : RecyclerView.ViewHolder(alertView) {
        val eventName: TextView = itemView.findViewById(R.id.eventNameTextView)
        val eventDate: TextView = itemView.findViewById(R.id.dateTextView)
        val eventTime: TextView = itemView.findViewById(R.id.timeTextView)
        val eventLocation: TextView = itemView.findViewById(R.id.locationTextView)
        val eventDescription: TextView = itemView.findViewById(R.id.descriptionTextView)
        val eventImage: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alerts, parent, false)
        return AlertHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        val currentItem = alertList[position]
        // Set item details to TextViews
        holder.eventName.text = currentItem.eventNameInput
        holder.eventDate.text = currentItem.dateInput
        holder.eventTime.text = currentItem.timeInput
        holder.eventLocation.text = currentItem.locationInput
        holder.eventDescription.text = currentItem.descriptionInput

        // Load event image if available
        currentItem.img?.let { imageUrl ->
            try {
                val imageBytes = android.util.Base64.decode(imageUrl, android.util.Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.eventImage.setImageBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                // Handle invalid Base64 encoded image
                e.printStackTrace()
                // Set placeholder image
                holder.eventImage.setImageResource(R.drawable.placeholder_image)
            } catch (e: OutOfMemoryError) {
                // Handle out of memory error
                e.printStackTrace()
                // Set placeholder image
                holder.eventImage.setImageResource(R.drawable.placeholder_image)
            } catch (e: Exception) {
                // Handle other exceptions
                e.printStackTrace()
                // Set placeholder image
                holder.eventImage.setImageResource(R.drawable.placeholder_image)
            }
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            // Create intent to start EventDetailsActivity
            val intent = Intent(context, Eventdetails::class.java)
            // Pass event data to the intent
            intent.putExtra("eventName", currentItem.eventNameInput)
            intent.putExtra("date", currentItem.dateInput)
            intent.putExtra("time", currentItem.timeInput)
            intent.putExtra("location", currentItem.locationInput)
            intent.putExtra("description", currentItem.descriptionInput)
            intent.putExtra("imageUrl", currentItem.img)
            // Start the activity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return alertList.size
    }
}