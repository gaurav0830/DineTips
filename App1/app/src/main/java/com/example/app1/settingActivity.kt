package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class settingActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val set = findViewById<ImageView>(R.id.back)
        set.setOnClickListener{
            val l = Intent(this,MainActivity2::class.java)
            startActivity(l)
        }
        val admin = findViewById<CardView>(R.id.admin)
        admin.setOnClickListener{
            val l = Intent(this,LoginActivity::class.java)
            startActivity(l)
        }
    }
}