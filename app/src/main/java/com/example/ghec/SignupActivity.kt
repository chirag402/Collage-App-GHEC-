package com.example.ghec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Signup : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupButton = findViewById<Button>(R.id.button)
        val bName = findViewById<TextInputEditText>(R.id.eTname)
        val bPhone = findViewById<TextInputEditText>(R.id.eTPhone)
        val bId = findViewById<TextInputEditText>(R.id.eTroll)
        val bPassword = findViewById<TextInputEditText>(R.id.eTpass)

        // Initialize the database reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        signupButton.setOnClickListener {
            val name = bName.text.toString().trim()
            val phone = bPhone.text.toString().trim()
            val password = bPassword.text.toString().trim()
            val uniqueId = bId.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && uniqueId.isNotEmpty()) {
                val user = User(name, phone, uniqueId, password)

                // Save user data in the database
                database.child(uniqueId).setValue(user).addOnSuccessListener {
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
