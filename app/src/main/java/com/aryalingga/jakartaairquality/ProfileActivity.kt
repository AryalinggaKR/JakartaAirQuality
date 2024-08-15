package com.aryalingga.jakartaairquality

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        imgProfile = findViewById(R.id.imgProfile)
        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)

        // Get the current user
        val user = FirebaseAuth.getInstance().currentUser

        // Update UI with user information
        updateUI(user)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Set username and email
            tvUserName.text = user.displayName
            tvUserEmail.text = user.email

            // Load profile picture using Glide
            Glide.with(this)
                .load(user.photoUrl)
                .circleCrop()
                .into(imgProfile)
        } else {
            // If user is not logged in, show default values
            tvUserName.text = "Guest"
            tvUserEmail.text = "guest@example.com"
            imgProfile.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}
