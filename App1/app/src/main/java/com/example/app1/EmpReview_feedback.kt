package com.example.app1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.Adaptors.EmpfeedbackAdaptor
import com.example.app1.Adaptors.FeedbackAdaptor
import com.example.app1.modal.EmpFeedbackModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmpReview_feedback : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var feedbackRecyclerView: RecyclerView
    private lateinit var feedbackList: ArrayList<FeedbackModel>
    private lateinit var feedbackAdapter: FeedbackAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_retrieve)

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Empfeedback")

        // Initialize RecyclerView and its properties
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this)
        feedbackRecyclerView.setHasFixedSize(true)

        // Initialize the list and the adapter
        feedbackList = ArrayList()
        feedbackAdapter = FeedbackAdaptor(feedbackList)
        feedbackRecyclerView.adapter = feedbackAdapter

        // Call method to retrieve and display feedback data
        retrieveFeedbackData()
    }

    // Function to retrieve and display feedback data from Firebase
    private fun retrieveFeedbackData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()

                // Iterate through all feedback entries
                for (feedbackSnapshot in snapshot.children) {
                    val feedback = feedbackSnapshot.getValue(FeedbackModel::class.java)
                    feedback?.let {
                        feedbackList.add(it)
                    }
                }

                // Notify the adapter that data has changed
                feedbackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EmpReview_feedback, "Failed to read data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}