package com.example.ristretto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logoutButton = findViewById<Button>(R.id.mainLogoutButton)

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }

    public override fun onStart() {
        super.onStart()
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }
}