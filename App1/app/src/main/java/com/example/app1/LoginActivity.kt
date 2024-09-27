package com.example.app1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app1.databinding.ActivityLoginBinding
import com.example.app1.modal.EmployeeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var employeesReference: DatabaseReference
    private lateinit var adminsReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance()
        employeesReference = firebaseDatabase.reference.child("Employees")
        adminsReference = firebaseDatabase.reference.child("Admins")

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
        // First check in Employees table
        employeesReference.orderByChild("empName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User found in Employees, validate password
                        validateEmployeeLogin(dataSnapshot, username, password)
                    } else {
                        // If not found in Employees, check in Admins table
                        checkInAdminsTable(username, password)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun checkInAdminsTable(username: String, password: String) {
        adminsReference.orderByChild("adminName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User found in Admins, validate password
                        validateAdminLogin(dataSnapshot, username, password)
                    } else {
                        // If not found in either table
                        Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun validateEmployeeLogin(dataSnapshot: DataSnapshot, username: String, password: String) {
        for (userSnapshot in dataSnapshot.children) {
            val employeeData = userSnapshot.getValue(EmployeeModel::class.java)

            // Validate password
            if (employeeData != null && employeeData.empPassword == password) {
                Toast.makeText(this@LoginActivity, "Login Successful as Employee!", Toast.LENGTH_SHORT).show()

                // Redirect to Employee MainActivity
                startActivity(Intent(this@LoginActivity, MainActivity3::class.java))
                finish()
                return
            }
        }
        // Invalid password for Employees
        Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
    }

    private fun validateAdminLogin(dataSnapshot: DataSnapshot, username: String, password: String) {
        for (userSnapshot in dataSnapshot.children) {
            val adminData = userSnapshot.getValue(UserData::class.java)

            // Validate password
            if (adminData != null && adminData.password == password) {
                Toast.makeText(this@LoginActivity, "Login Successful as Admin!", Toast.LENGTH_SHORT).show()

                // Redirect to Admin MainActivity
                startActivity(Intent(this@LoginActivity, MainActivity3::class.java))
                finish()
                return
            }
        }
        // Invalid password for Admins
        Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
    }

    // Utility function to check network connectivity
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
