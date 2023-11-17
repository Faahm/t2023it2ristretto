package com.example.ristretto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val userCollectionName = "users"
    private val logo = "https://firebasestorage.googleapis.com/v0/b/t2023it2-ristretto.appspot.com/o/ristretto_logo_edit.png?alt=media&token=703f74df-e651-4f92-9116-3cc138faeae1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerAndLoginButton = findViewById<Button>(R.id.btnRegisterAndLogin)
        auth = Firebase.auth
        val db = Firebase.firestore
        val emailET = findViewById<EditText>(R.id.etRegisterEmail)
        val passwordET = findViewById<EditText>(R.id.etRegisterPassword)
        val displayNameET = findViewById<EditText>(R.id.etRegisterDisplayName)
        val photoUrlET = findViewById<EditText>(R.id.etRegisterPhotoUrl)
        val registerLogo = findViewById<ImageView>(R.id.registerLogo)
        val registerBack = findViewById<Button>(R.id.btnRegister)

        Glide.with(this).load(logo).into(registerLogo)

        registerBack.setOnClickListener {
            finish()
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }

        registerAndLoginButton.setOnClickListener {
            val email = emailET.text.toString()
            val password = passwordET.text.toString()
            val displayName = displayNameET.text.toString()
            val photoUrl = photoUrlET.text.toString()

            if (email != "" && password != "" && displayName != ""){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val data = hashMapOf(
                                "email" to email,
                                "displayName" to displayName,
                                "photoUrl" to photoUrl
                            )

                            db.collection(userCollectionName).document()
                                .set(data)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        baseContext,
                                        "Registered successfully!",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        baseContext,
                                        "Registration failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }

                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Registration failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }
}