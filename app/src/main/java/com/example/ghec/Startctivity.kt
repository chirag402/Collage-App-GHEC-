package com.example.ghec

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnStudentLogin = findViewById<TextView>(R.id.textStudentLogin)

        btnStudentLogin.setOnClickListener()
        {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
        val btnSkip = findViewById<TextView>(R.id.skip)

        btnSkip.setOnClickListener()
        {
            val intent = Intent (this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val btnFacultyLogin = findViewById<TextView>(R.id.textFacultyLogin)
        btnFacultyLogin.setOnClickListener()
        {
            val intent = Intent(this , FacultyLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
