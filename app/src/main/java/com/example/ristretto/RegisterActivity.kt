package com.example.ristretto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val userCollection = "users"
    private val logo = "https://firebasestorage.googleapis.com/v0/b/t2023it2-ristretto.appspot.com/o/ristretto_logo_edit.png?alt=media&token=703f74df-e651-4f92-9116-3cc138faeae1"

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestoreDB: FirebaseFirestore = FirebaseFirestore.getInstance()

        val registerLogo = findViewById<ImageView>(R.id.registerLogo)
        val etPhotoUrl = findViewById<EditText>(R.id.etRegisterPhotoUrl)
        val etDisplayName = findViewById<EditText>(R.id.etRegisterDisplayName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val btnRegisterAndLogin = findViewById<Button>(R.id.btnRegisterAndLogin)
        val btnBack = findViewById<Button>(R.id.btnBack)

        Glide.with(this).load(logo).into(registerLogo)

        btnRegisterAndLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val displayName = etDisplayName.text.toString()
            val photoUrl = etPhotoUrl.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userId = user?.uid ?: ""

                            val userData = hashMapOf(
                                "email" to email,
                                "displayName" to displayName,
                                "photoUrl" to photoUrl
                            )

                            firestoreDB.collection(userCollection).document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    showToast("Registered successfully!")
                                    val mainActivity = Intent(this, MainActivity::class.java)
                                    startActivity(mainActivity)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    showToast("Registration failed: ${e.message}")
                                }
                        } else {
                            showToast("Registration failed.")
                        }
                    }
            } else {
                showToast("Please fill in all the fields.")
            }
        }

        btnBack.setOnClickListener {
            finish()
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }
}
