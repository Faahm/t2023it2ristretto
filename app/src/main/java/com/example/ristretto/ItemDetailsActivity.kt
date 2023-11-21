package com.example.ristretto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ItemDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val name: String = intent.getStringExtra("name").toString()
        val description: String = intent.getStringExtra("description").toString()
        val photoUrl: String = intent.getStringExtra("photoUrl").toString()
        val price: String = intent.getStringExtra("price").toString()

        val viewItemPhoto = findViewById<ImageView>(R.id.ivItemPhoto)
        Glide.with(this)
            .load(photoUrl)
            .into(viewItemPhoto)

        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = name

        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvDescription.text = description

        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        tvPrice.text = price

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

    }
}