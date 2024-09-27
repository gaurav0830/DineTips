package com.example.app1.Adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.FeedbackModel
import com.example.app1.R

class FeedbackAdaptor (private val feedbackList: List<FeedbackModel>) : RecyclerView.Adapter<FeedbackAdaptor.FeedbackViewHolder>() {

    // ViewHolder class to hold references to views in each item
    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedbackText: TextView = itemView.findViewById(R.id.feedbackText)
        val feedbackRatingBar: RatingBar = itemView.findViewById(R.id.feedbackRatingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        // Inflate item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        // Bind data to views
        val feedback = feedbackList[position]
        holder.feedbackText.text = "Name: ${feedback.feedName}\nEmail: ${feedback.emailAdd}\nDescription: ${feedback.descfeed}"
        holder.feedbackRatingBar.rating = feedback.rating ?: 0f
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }
}