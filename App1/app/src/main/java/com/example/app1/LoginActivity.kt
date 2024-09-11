package com.example.app1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        // Check if the user is already logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is already logged in, redirect to MainActivity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        // Login Button Click Listener
        binding.loginButton.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString().toLowerCase().trim()
            val loginPassword = binding.loginPassword.text.toString().trim()

            // Check if both fields are not empty
            if (loginUsername.isNotEmpty() && loginPassword.isNotEmpty()) {
                // Check network connectivity
                if (isNetworkAvailable()) {
                    FirebaseAuth.getInstance().signOut() // Ensure the user is signed out
                    loginUser(loginUsername, loginPassword)
                } else {
                    Toast.makeText(this@LoginActivity, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        // Signup Redirect
        binding.signUpRedirect.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish() // Optional: Finish current activity
        }
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            // Validate password
                            if (userData != null && userData.password == password) {
                                Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                                // Redirect to MainActivity
                                startActivity(Intent(this@LoginActivity, MainActivity3::class.java))
                                finish() // Finish current activity
                                return
                            } else {
                                Log.e("LoginError", "Password does not match for user: $username")
                            }
                        }
                    } else {
                        Log.e("LoginError", "Username does not exist: $username")
                    }
                    Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Utility function to check network connectivity
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
