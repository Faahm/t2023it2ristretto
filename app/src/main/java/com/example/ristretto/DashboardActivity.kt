package com.example.ristretto

import android.app.ActionBar
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val itemCollection = "items"
    private val logo = "https://firebasestorage.googleapis.com/v0/b/t2023it2-ristretto.appspot.com/o/ristretto_logo_edit.png?alt=media&token=703f74df-e651-4f92-9116-3cc138faeae1"

    private fun redirectToLoginActivity() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
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

    private fun createCardView(): CardView {
        val cv = CardView(this)
        val cvParams = ActionBar.LayoutParams(
            resources.getDimensionPixelOffset(R.dimen.card_width), ActionBar.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.card_margin_bottom))
        }
        cv.layoutParams = cvParams
        return cv
    }

    private fun createLinearLayoutForCard(): LinearLayout {
        val llCard = LinearLayout(this)
        val llCardParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
        )
        llCard.gravity = Gravity.CENTER_HORIZONTAL
        llCard.orientation = LinearLayout.VERTICAL
        llCard.setPadding(
            resources.getDimensionPixelOffset(R.dimen.llCard_padding_x),
            resources.getDimensionPixelOffset(R.dimen.llCard_padding_y),
            resources.getDimensionPixelOffset(R.dimen.llCard_padding_x),
            resources.getDimensionPixelOffset(R.dimen.llCard_padding_y)
        )
        llCard.layoutParams = llCardParams
        return llCard
    }

    private fun createImageView(photoUrl: String): ImageView {
        val iv = ImageView(this)
        val ivParams = LinearLayout.LayoutParams(
            resources.getDimensionPixelOffset(R.dimen.iv_size),
            resources.getDimensionPixelOffset(R.dimen.iv_size)
        )
        iv.layoutParams = ivParams
        Glide.with(this).load(photoUrl).into(iv)
        return iv
    }

    private fun createNameTextView(name: String): TextView {
        val tvName = TextView(this)
        tvName.layoutParams = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        ).apply{
            topMargin = resources.getDimensionPixelOffset(R.dimen.tvTitle_margin_top)
        }
        tvName.text = (name)
        tvName.textSize = 18F
        tvName.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        tvName.setTypeface(null, Typeface.BOLD)
        return tvName
    }

    private fun createDescriptionTextView(description: String): TextView {
        val tvDescription = TextView(this)
        tvDescription.layoutParams = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        ).apply{
            topMargin = resources.getDimensionPixelOffset(R.dimen.tvTitle_margin_top)
        }
        tvDescription.text = (description)
        tvDescription.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
        return tvDescription
    }

    private fun createPriceTextViewLinearLayout(): LinearLayout {
        val lltvPricebtnView = LinearLayout(this)
        val lltvPricebtnViewParams = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
        ).apply{
            topMargin = resources.getDimensionPixelOffset(R.dimen.tvTitle_margin_top)
        }
        lltvPricebtnView.orientation = LinearLayout.HORIZONTAL
        lltvPricebtnView.layoutParams = lltvPricebtnViewParams

        return lltvPricebtnView
    }

    private fun createPriceTextView(price: String): TextView {
        val tvPrice = TextView(this)
        val priceTvParams = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            1F
        )
        tvPrice.layoutParams = priceTvParams

        tvPrice.text = price
        tvPrice.textSize = 18F
        tvPrice.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
        tvPrice.setTypeface(null, Typeface.BOLD)
        return tvPrice
    }

    private fun createViewButtonTextView(photoUrl: String, name: String, description: String, price: String): TextView {
        val btnView = TextView(this)
        btnView.layoutParams = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            1F
        )
        btnView.text = getString(R.string.view)
        btnView.textSize = 18F
        btnView.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
        btnView.setTypeface(null, Typeface.BOLD)

        btnView.setOnClickListener {
            val itemDetailsActivity = Intent(this, ItemDetailsActivity::class.java)
            itemDetailsActivity.putExtra("photoUrl", photoUrl)
            itemDetailsActivity.putExtra("name", name)
            itemDetailsActivity.putExtra("description", description)
            itemDetailsActivity.putExtra("price", price)
            startActivity(itemDetailsActivity)
        }
        return btnView
    }

    private fun addViews(
        cv: CardView,
        llCard: LinearLayout,
        iv: ImageView,
        tvName: TextView,
        tvDescription: TextView,
        lltvPricebtnView: LinearLayout,
        tvPrice: TextView,
        btnView: TextView
    ) {
        val layoutLinear = findViewById<LinearLayout>(R.id.layoutLinear)
        llCard.addView(iv)
        llCard.addView(tvName)
        llCard.addView(tvDescription)
        lltvPricebtnView.addView(tvPrice)
        lltvPricebtnView.addView(btnView)
        llCard.addView(lltvPricebtnView)
        cv.addView(llCard)
        layoutLinear.addView(cv)
    }

    private fun loadItems() {
        val db = Firebase.firestore

        db.collection(itemCollection)
            .get()
            .addOnSuccessListener { query ->
                for (document in query) {
                    val photoUrl = document.data["photoUrl"].toString()
                    val name = document.data["name"].toString()
                    val description = document.data["description"].toString()
                    val price = String.format("Php. %.2f", document.data["price"].toString().toFloat())

                    val cv = createCardView()
                    val llCard = createLinearLayoutForCard()
                    val iv = createImageView(photoUrl)
                    val tvName = createNameTextView(name)
                    val tvDescription = createDescriptionTextView(description)
                    val lltvPricebtnView = createPriceTextViewLinearLayout()
                    val tvPrice = createPriceTextView(price)
                    val btnView = createViewButtonTextView(photoUrl, name, description, price)

                    btnView.setOnClickListener {
                        val productViewActivity = Intent(this, ItemDetailsActivity::class.java)
                        productViewActivity.putExtra("photoUrl", photoUrl)
                        productViewActivity.putExtra("name", name)
                        productViewActivity.putExtra("description", description)
                        productViewActivity.putExtra("price", price)
                        startActivity(productViewActivity)
                    }

                    addViews(cv, llCard, iv, tvName, tvDescription, lltvPricebtnView, tvPrice, btnView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("zzz", "Failed to fetch documents.", exception)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnAdd = findViewById<Button>(R.id.dashboardAddButton)
        val btnLogout: Button = findViewById(R.id.dashboardLogoutButton)
        val dashboardLogo = findViewById<ImageView>(R.id.dashboardLogo)
        Glide.with(this).load(logo).into(dashboardLogo)
        firebaseAuth = FirebaseAuth.getInstance()

        btnAdd.setOnClickListener {
            val itemAddActivity = Intent(this, AddItemActivity::class.java)
            startActivity(itemAddActivity)
            finish()
        }

        btnLogout.setOnClickListener {
            signOutAndRedirectToLogin()
        }
        loadItems()
    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }
}
