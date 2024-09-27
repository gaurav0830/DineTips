package com.example.app1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class feedback : AppCompatActivity() {
    private lateinit var FeedName: EditText
    private lateinit var EmailAdd: EditText
    private lateinit var desc: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var btnSavefeed: Button

    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        // Initialize views
        FeedName = findViewById(R.id.FeedName)
        EmailAdd = findViewById(R.id.EmailAdd)
        desc = findViewById(R.id.desc)
        ratingBar = findViewById(R.id.ratingBar)
        btnSavefeed = findViewById(R.id.btnSavefeed)

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("feedback")

        // Set onClickListener for the save button
        btnSavefeed.setOnClickListener {
            saveEmployeeData()
        }
    }

    // Function to save employee data
    private fun saveEmployeeData() {
        val feedName = FeedName.text.toString()
        val emailAdd = EmailAdd.text.toString()
        val descFeed = desc.text.toString()
        val rating = ratingBar.rating // Get the rating value from the RatingBar

        // Validation checks
        if (feedName.isEmpty()) {
            FeedName.error = "Please enter Employee Name"
            return
        }
        if (emailAdd.isEmpty()) {
            FeedName.error = "Please enter Employee Email"
            return
        }
        if (descFeed.isEmpty()) {
            desc.error = "Please enter Feedback"
            return
        }

        // Generate a unique ID for the feedback entry
        val FId = dbRef.push().key!!

        // Create a FeedbackModel object
        val feedback = FeedbackModel(FId, feedName, emailAdd, descFeed, rating)

        // Store the feedback data in Firebase Realtime Database
        dbRef.child(FId).setValue(feedback)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                // Clear the fields after successful insertion
                FeedName.text.clear()
                EmailAdd.text.clear()
                desc.text.clear()
                ratingBar.rating = 0f // Reset the rating bar to 0
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun saveEmployeeData(view: View) {}
}