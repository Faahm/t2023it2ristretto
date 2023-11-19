package com.example.ristretto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogout: Button = findViewById(R.id.mainLogoutButton)
        firebaseAuth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            signOutAndRedirectToLogin()
        }
    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    private fun signOutAndRedirectToLogin() {
        firebaseAuth.signOut()
        redirectToLoginActivity()
    }

    private fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
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
