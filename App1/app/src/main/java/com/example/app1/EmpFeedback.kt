package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app1.modal.EmpFeedbackModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmpFeedback : AppCompatActivity() {
    private lateinit var EFeedName: EditText
    private lateinit var EEmailAdd: EditText
    private lateinit var Edesc: EditText
    private lateinit var EratingBar: RatingBar
    private lateinit var EbtnSavefeed: Button

    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp_feedback)

        // Initialize views
        EFeedName = findViewById(R.id.EFeedName)
        EEmailAdd = findViewById(R.id.EEmailAdd)
        Edesc = findViewById(R.id.Edesc)
        EratingBar = findViewById(R.id.EratingBar)
        EbtnSavefeed = findViewById(R.id.EbtnSavefeed)

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Empfeedback")

        // Set onClickListener for the save button
        EbtnSavefeed.setOnClickListener {
            saveEmployeeData()
        }
        val back2 = findViewById<ImageView>(R.id.back2)
        back2.setOnClickListener{
            val er = Intent(this,MainActivity2::class.java)
            startActivity(er)
        }
    }

    // Function to save employee data
    private fun saveEmployeeData() {
        val EfeedName = EFeedName.text.toString()
        val EemailAdd = EEmailAdd.text.toString()
        val EdescFeed = Edesc.text.toString()
        val Erating = EratingBar.rating // Get the rating value from the RatingBar

        // Validation checks
        if (EfeedName.isEmpty()) {
            EFeedName.error = "Please enter Employee Name"
            return
        }
        if (EemailAdd.isEmpty()) {
            EFeedName.error = "Please enter Employee Email"
            return
        }
        if (EdescFeed.isEmpty()) {
            Edesc.error = "Please enter Feedback"
            return
        }

        // Generate a unique ID for the feedback entry
        val EFId = dbRef.push().key!!

        // Create a FeedbackModel object
        val feedback = EmpFeedbackModel(EFId, EfeedName, EemailAdd, EdescFeed, Erating)

        // Store the feedback data in Firebase Realtime Database
        dbRef.child(EFId).setValue(feedback)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                // Clear the fields after successful insertion
                EFeedName.text.clear()
                EEmailAdd.text.clear()
                Edesc.text.clear()
                EratingBar.rating = 0f // Reset the rating bar to 0
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    fun saveEmployeeData(view: View) {}
}