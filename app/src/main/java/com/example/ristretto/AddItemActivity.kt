package com.example.ristretto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AddItemActivity : AppCompatActivity() {
    private val menuCollectionName = "items"
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        db = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etItemName)
        val etDescription = findViewById<EditText>(R.id.etItemDescription)
        val etPrice = findViewById<EditText>(R.id.etItemPrice)
        val etPhotoUrl = findViewById<EditText>(R.id.etItemPhotoUrl)
        val btnAddItem = findViewById<Button>(R.id.btnAdd)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        btnAddItem.setOnClickListener {
            val name = etName.text.toString()
            val description = etDescription.text.toString()
            val price = etPrice.text.toString()
            val photoUrl = etPhotoUrl.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && photoUrl.isNotEmpty()) {
                val priceDouble = String.format("%.2f", price.toDouble()).toDouble()

                val data = hashMapOf(
                    "name" to name,
                    "description" to description,
                    "price" to priceDouble,
                    "photoUrl" to photoUrl,
                    "dateCreated" to Timestamp(Date())
                )

                db.collection(menuCollectionName)
                    .add(data)
                    .addOnSuccessListener {
                        showToast("Added item.")
                        finish()
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }
                    .addOnFailureListener {
                        showToast("Item addition failed.")
                    }
            } else {
                showToast("Please input all fields.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}
