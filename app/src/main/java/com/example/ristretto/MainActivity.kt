package com.example.ristretto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton = findViewById(R.id.mainLogoutButton)
        auth = FirebaseAuth.getInstance()

        logoutButton.setOnClickListener {
            signOutAndRedirectToLogin()
        }
    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    private fun signOutAndRedirectToLogin() {
        auth.signOut()
        redirectToLoginActivity()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLoginActivity()
        }
    }

    private fun redirectToLoginActivity() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }
}
