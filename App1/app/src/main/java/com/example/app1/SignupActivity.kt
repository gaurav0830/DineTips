package com.example.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app1.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        // Sign Up Button Click Listener
        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signUsername.text.toString().trim()
            val signupPassword = binding.signupPassword.text.toString().trim()
            val signupCPassword = binding.signupCPassword.text.toString().trim()

            // Check if any field is empty
            if (signupUsername.isEmpty() || signupPassword.isEmpty() || signupCPassword.isEmpty()) {
                Toast.makeText(this@SignupActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            } else if (signupPassword != signupCPassword) {
                // Check if passwords match
                Toast.makeText(this@SignupActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                signupUser(signupUsername, signupPassword)
            }
        }

        // Redirect to Login Screen
        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

    // Function to Sign Up the User
    private fun signupUser(username: String, password: String) {
        // Check if the username already exists
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // If username does not exist, create new user
                    if (!dataSnapshot.exists()) {
                        val id = databaseReference.push().key
                        if (id != null) {
                            val userData = UserData(id, username, password)
                            databaseReference.child(id).setValue(userData)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this@SignupActivity, "SignUp Successful", Toast.LENGTH_SHORT).show()
                                        // Redirect to LoginActivity after successful signup
                                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this@SignupActivity, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this@SignupActivity, "Error: Failed to generate User ID", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SignupActivity, "User already exists!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
