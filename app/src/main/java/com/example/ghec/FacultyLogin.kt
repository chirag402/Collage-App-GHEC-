package com.example.ghec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FacultyLoginActivity : AppCompatActivity() {

    lateinit var databaseReference: DatabaseReference

    companion object {
        const val KEY1 = "com.example.splashscreen.SigninActivity.name"
        const val KEY2 = "com.example.splashscreen.SigninActivity.Password"
        const val KEY3 = "com.example.splashscreen.SigninActivity.phone"
        const val KEY4 = "com.example.splashscreen.SigninActivity.uniqueid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_login)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val signinBtn = findViewById<Button>(R.id.button)
        val email = findViewById<TextInputEditText>(R.id.eTgmail)
        val password = findViewById<TextInputEditText>(R.id.eTroll)
        val signupBtn = findViewById<Button>(R.id.btnSignFaculty)

        signinBtn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                readData(emailText, passwordText)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this, SIgn_Up_Faculty::class.java)
            startActivity(intent)
        }
    }

    private fun readData(email: String, password: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(email.replace(".", ",")).get().addOnSuccessListener {
            if (it.exists() && it.child("password").value == password) {
                val name = it.child("name").value.toString()
                val phone = it.child("phone").value.toString()

                val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                val intentWelcome = Intent(this, MainActivity::class.java)
                intentWelcome.putExtra(KEY1, name)
                intentWelcome.putExtra(KEY2, password)
                intentWelcome.putExtra(KEY3, phone)
                intentWelcome.putExtra(KEY4, email)
                startActivity(intentWelcome)
                finish()
            } else {
                Toast.makeText(this, "USER DOES NOT EXIST OR INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "TRY AGAIN AFTER SOMETIME", Toast.LENGTH_SHORT).show()
        }
    }
}
