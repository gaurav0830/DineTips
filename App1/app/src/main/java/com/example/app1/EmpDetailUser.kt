package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.app1.modal.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmpDetailUser : AppCompatActivity() {
    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpAge: TextView
    private lateinit var ivEmpImage: ImageView
    private lateinit var btnPay: Button
    private lateinit var btnFeedback: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp_detail_user)

        initView()
        setValuesToViews()

        // Set click listeners for Pay and Feedback buttons
        btnPay.setOnClickListener {
            handlePayment()
        }

        btnFeedback.setOnClickListener {
            openFeedbackActivity()
        }
        val cancel2 = findViewById<ImageView>(R.id.cancel2)
        cancel2.setOnClickListener{
            val s = Intent(this,EmployeeList::class.java)
            startActivity(s)
        }
    }

    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        ivEmpImage = findViewById(R.id.ivEmpImage)
        btnPay = findViewById(R.id.btnpay) // Reference for Pay button
        btnFeedback = findViewById(R.id.btnFeedback) // Reference for Feedback button
    }

    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("empId") ?: "N/A"
        tvEmpName.text = intent.getStringExtra("empName") ?: "N/A"
        tvEmpAge.text = intent.getStringExtra("empAge") ?: "N/A"

        val imageUrl = intent.getStringExtra("empImageUrl") ?: ""
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions().circleCrop())
                .placeholder(R.drawable.placeholder_image)
                .into(ivEmpImage)
        }
    }

    // Handle Payment logic (placeholder for actual payment process)
    private fun handlePayment() {
        Toast.makeText(this, "Payment processing for ${tvEmpName.text}", Toast.LENGTH_SHORT).show()
        // Add payment integration logic here, e.g., opening a payment gateway
    }

    // Open Feedback activity (replace this with the actual feedback logic or activity)
    private fun openFeedbackActivity() {
        val feedbackIntent = Intent(this, EmpFeedback::class.java)
        startActivity(feedbackIntent)
    }
}
