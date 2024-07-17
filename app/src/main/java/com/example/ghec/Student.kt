package com.example.ghec

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class Student : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student, container, false)


        val btnAttendence = view.findViewById<CardView>(R.id.attendenceWeb)
        btnAttendence.setOnClickListener {
            val url = "https://ghec.ac.in/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        val btnResult = view.findViewById<CardView>(R.id.result)
        btnResult.setOnClickListener {
            val url = "https://himachal-pradesh.indiaresults.com/himtu/default.aspx"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        val btnAdmit = view.findViewById<CardView>(R.id.admitCard)
        btnAdmit.setOnClickListener {
            val url = "https://hptuexam.com/site/userlogin"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        val btnExamForms = view.findViewById<CardView>(R.id.examForms)
        btnExamForms.setOnClickListener {
            val url = "https://hptuexam.com/site/userlogin"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        val logoutButton = view.findViewById<Button>(R.id.btnLogout)
        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Redirect to SigninActivity
        val intent = Intent(activity, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish() // Close the current activity

    }
}