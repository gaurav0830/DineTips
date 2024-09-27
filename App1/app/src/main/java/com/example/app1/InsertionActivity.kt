package com.example.app1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app1.modal.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class InsertionActivity : AppCompatActivity() {
    private lateinit var etEmpName: EditText
    private lateinit var etEmpAge: EditText
    private lateinit var etEmpPassword: EditText
    private lateinit var btnSaveData: Button
    private lateinit var btnPickImage: Button
    private lateinit var imageView: ImageView

    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    private var imageUri: Uri? = null

    private val REQUEST_IMAGE_PICK = 1001
    private val REQUEST_CODE = 1002


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)


        etEmpName = findViewById(R.id.etEmpName)
        etEmpAge = findViewById(R.id.etEmpAge)
        etEmpPassword = findViewById(R.id.etEmpPassword)
        btnSaveData = findViewById(R.id.btnSave)
        btnPickImage = findViewById(R.id.btnimg)
        imageView = findViewById(R.id.imageView)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        storageRef = FirebaseStorage.getInstance().reference

        requestPermissions()

        btnPickImage.setOnClickListener {
            pickImage()
        }

        btnSaveData.setOnClickListener {
            uploadImage()
        }

        val dashpage = findViewById<LinearLayout>(R.id.dashpage)
        dashpage.setOnClickListener{
            val d = Intent(this,MainActivity3::class.java)
            startActivity(d)
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val imageRef = storageRef.child("images/${imageUri!!.lastPathSegment}")

            imageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveEmployeeData(uri.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            saveEmployeeData(null)
        }
    }

    private fun saveEmployeeData(imageUrl: String?) {
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empPassword = etEmpPassword.text.toString()

        if (empName.isEmpty()) {
            etEmpName.error = "Please enter Employee Name"
            return
        }
        if (empAge.isEmpty()) {
            etEmpAge.error = "Please enter Employee Age"
            return
        }

        if (empPassword.isEmpty()) {
            etEmpPassword.error = "Please enter Employee Password!!"
            return
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empAge, empPassword, imageUrl)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
                etEmpName.text.clear()
                etEmpAge.text.clear()
                etEmpPassword.text.clear()
                imageView.setImageResource(android.R.color.transparent) // Reset image view
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
