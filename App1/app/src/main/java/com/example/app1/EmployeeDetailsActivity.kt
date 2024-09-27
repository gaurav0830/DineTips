package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app1.modal.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpAge: TextView
    private lateinit var ivEmpImage: ImageView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }

        // Cancel button to go back to the previous activity
        val cancel = findViewById<ImageView>(R.id.cancel1)
        cancel.setOnClickListener {
            val er = Intent(this, MainActivity3::class.java)
            startActivity(er)
        }
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        ivEmpImage = findViewById(R.id.ivEmpImage)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("empId") ?: "N/A"
        tvEmpName.text = intent.getStringExtra("empName") ?: "N/A"
        tvEmpAge.text = intent.getStringExtra("empAge") ?: "N/A"

        val imageUrl = intent.getStringExtra("imageUrl") // Ensure this matches the key used in EmployeeList
        Log.d("EmployeeDetailsActivity", "Image URL: $imageUrl")  // Log the image URL for debugging
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.error_placeholder) // Use a placeholder in case of error
                .into(ivEmpImage)
        } else {
            ivEmpImage.setImageResource(R.drawable.error_placeholder) // Use a default image for null or empty URL
            Log.e("EmployeeDetailsActivity", "Image URL is empty or null")
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(empId: String, empName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        // Update employee details in the dialog
        // Implementation of dialog handling and updating logic

        mDialog.setTitle("Updating $empName Record")
        val alertDialog = mDialog.create()
        alertDialog.show()
    }
}
