package com.example.app1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val ffeed = findViewById<LinearLayout>(R.id.ffeed)
        ffeed.setOnClickListener{
            val f = Intent(this, feedback::class.java)
            startActivity(f)
        }
        val rfeed=findViewById<LinearLayout>(R.id.Rfeed)
        rfeed.setOnClickListener{
            val  r = Intent(this,feedbackRetrieve::class.java)
            startActivity(r)
        }
        val list = findViewById<LinearLayout>(R.id.list)
        list.setOnClickListener{
            val l = Intent(this,EmployeeList::class.java)
            startActivity(l)
        }
        val efeed = findViewById<LinearLayout>(R.id.efeed)
        efeed.setOnClickListener{
            val ef = Intent(this,EmpFeedback::class.java)
            startActivity(ef)
        }
        val setting = findViewById<LinearLayout>(R.id.settings)
        setting.setOnClickListener{
            val s = Intent(this,settingActivity::class.java)
            startActivity(s)
        }


    }
}
