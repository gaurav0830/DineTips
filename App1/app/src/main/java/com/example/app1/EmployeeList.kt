package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.adapters.EmpAdaptor
import com.example.app1.modal.EmployeeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeList : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        empRecyclerView = findViewById(R.id.rvEmp)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)

        empList = arrayListOf()

        getEmployeesData()

        val userpage = findViewById<LinearLayout>(R.id.userpage)
        userpage.setOnClickListener{
            val s = Intent(this,MainActivity2::class.java)
            startActivity(s)
        }
    }

    private fun getEmployeesData() {
        // Show loading indicator and hide RecyclerView initially
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empData?.let { empList.add(it) }
                    }

                    // Update the RecyclerView with the fetched data
                    val mAdaptor = EmpAdaptor(empList)
                    empRecyclerView.adapter = mAdaptor

                    // Set click listener for RecyclerView items
                    mAdaptor.setOnItemClickListener(object : EmpAdaptor.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@EmployeeList, EmpDetailUser::class.java).apply {
                                putExtra("empId", empList[position].empId)
                                putExtra("empName", empList[position].empName)
                                putExtra("empAge", empList[position].empAge)
                                putExtra("imageUrl", empList[position].imageUrl) // Ensure the correct key is used
                            }
                            startActivity(intent)
                        }
                    })

                    // Hide loading indicator and show RecyclerView
                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                } else {
                    // Handle case where no data is available
                    empRecyclerView.visibility = View.GONE
                    tvLoadingData.visibility = View.VISIBLE
                    tvLoadingData.text = "No data available"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors here
                empRecyclerView.visibility = View.GONE
                tvLoadingData.visibility = View.VISIBLE
                tvLoadingData.text = "Failed to load data: ${error.message}"
            }
        })
    }
}
