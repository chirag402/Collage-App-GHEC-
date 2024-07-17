package com.example.ghec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SigninActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    companion object {
        const val KEY1 = "com.example.splashscreen.SigninActivity.name"
        const val KEY2 = "com.example.splashscreen.SigninActivity.Password"
        const val KEY3 = "com.example.splashscreen.SigninActivity.phone"
        const val KEY4 = "com.example.splashscreen.SigninActivity.uniqueid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is already logged in, redirect to HomeActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity
            return // Exit the onCreate method
        }

        val signinBtn = findViewById<Button>(R.id.button)
        val mobile = findViewById<TextInputEditText>(R.id.eTPhone)
        val roll = findViewById<TextInputEditText>(R.id.eTroll)
        val passWord = findViewById<TextInputEditText>(R.id.eTpass)
        val signup = findViewById<Button>(R.id.btnSign)

        signinBtn.setOnClickListener {
            val uniqueId = roll.text.toString().trim()
            val mobileNumber = mobile.text.toString().trim()
            val password = passWord.text.toString().trim()

            if (uniqueId.isNotEmpty() && mobileNumber.isNotEmpty() && password.isNotEmpty()) {
                readData(uniqueId, mobileNumber, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        signup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun readData(uniqueId: String, mobileNumber: String, password: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uniqueId).get().addOnSuccessListener {
            // If user exists or not
            if (it.exists() && it.child("phone").value == mobileNumber && it.child("password").value == password) {
                // Welcome user in app with intent
                val name = it.child("name").value
                val Password = it.child("password").value
                val phone = it.child("phone").value
                val uniqueid = it.child("uniqueId").value

                // Save login status in SharedPreferences
                val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                val intentWelcome = Intent(this, MainActivity::class.java)
                intentWelcome.putExtra(KEY1, name.toString())
                intentWelcome.putExtra(KEY2, Password.toString())
                intentWelcome.putExtra(KEY3, phone.toString())
                intentWelcome.putExtra(KEY4, uniqueid.toString())
                startActivity(intentWelcome)
                finish() // Close the current activity
            } else {
                Toast.makeText(this, "USER DOES NOT EXIST OR INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "TRY AGAIN AFTER SOMETIME", Toast.LENGTH_SHORT).show()
        }
    }
}

