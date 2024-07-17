package com.example.ghec

import com.example.ghec.com.example.ghec.Faculty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class SIgn_Up_Faculty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_faculty)

        val signupButton = findViewById<Button>(R.id.button)
        val bName = findViewById<TextInputEditText>(R.id.eTname)
        val bGmail = findViewById<TextInputEditText>(R.id.eTgmail)
        val bPassword = findViewById<TextInputEditText>(R.id.eTpass)

        // Initialize the database reference
        val database = FirebaseDatabase.getInstance().getReference("Users")

        signupButton.setOnClickListener {
            val name = bName.text.toString().trim()
            val gmail = bGmail.text.toString().trim()
            val password = bPassword.text.toString().trim()

            if (name.isNotEmpty() && gmail.isNotEmpty() && password.isNotEmpty()) {
                val user = Faculty(name, gmail, password)

                // Save user data in the database
                database.child(gmail).setValue(user).addOnSuccessListener {
                    Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                    finish() // Close current activity
                }.addOnFailureListener {
                    Toast.makeText(this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
// Initialize the database reference
