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

class LoginActivity : AppCompatActivity() {
    private val logo = "https://firebasestorage.googleapis.com/v0/b/t2023it2-ristretto.appspot.com/o/ristretto_logo_edit.png?alt=media&token=703f74df-e651-4f92-9116-3cc138faeae1"

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val emailET = findViewById<EditText>(R.id.etLoginEmail)
        val passwordET = findViewById<EditText>(R.id.etLoginPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val loginLogo = findViewById<ImageView>(R.id.loginLogo)
        Glide.with(this).load(logo).into(loginLogo)

        loginButton.setOnClickListener {
            val email = emailET.text.toString()
            val password = passwordET.text.toString()
            if (email != "" && password != ""){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Success",
                                Toast.LENGTH_SHORT,
                            ).show()

                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Email or password is incorrect.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                }
            }else{
                Toast.makeText(
                    baseContext,
                    "Email or password is incorrect.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        registerButton.setOnClickListener {
            finish()
            val registerActivity = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivity)
        }

    }
}