package com.example.ghec

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class Home : Fragment() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val notificationWebView = view.findViewById<WebView>(R.id.notifyWeb)
        val webSettings = notificationWebView.settings
        webSettings.javaScriptEnabled = true
        notificationWebView.webViewClient = WebViewClient()
        notificationWebView.loadUrl("https://ghec.ac.in/index.php?/page/news")

        // Set up the Button to open the GHEC website
        val btnWeb = view.findViewById<Button>(R.id.btnWeb)
        btnWeb.setOnClickListener {
            val url = "https://www.ghec.ac.in/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        val openGalleryCard = view.findViewById<CardView>(R.id.gallery)
        openGalleryCard.setOnClickListener {
            val intent = Intent(activity, GalleryActivity::class.java)
            startActivity(intent)
        }
        val openDepartmentCard = view.findViewById<CardView>(R.id.courses)
        openDepartmentCard.setOnClickListener {
            val intent = Intent(activity, DepartmentActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}

