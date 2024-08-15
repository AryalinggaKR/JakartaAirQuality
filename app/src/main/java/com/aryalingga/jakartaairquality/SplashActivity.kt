package com.aryalingga.jakartaairquality

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.ImageButton


class SplashActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // This removes SplashActivity from the back stack
        }, 10000)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Set up the ViewPager with an adapter
        val images = listOf(
            R.drawable.splash_image1,
            R.drawable.splash_image2,
            R.drawable.splash_image3
        )
        viewPager.adapter = SplashPagerAdapter(images)

        // Set up TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Set up navigation buttons
        val btnPrevious: ImageButton = findViewById(R.id.btn_previous)
        val btnNext: ImageButton = findViewById(R.id.btn_next)

        btnPrevious.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
            }
        }

        btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < images.size - 1) {
                viewPager.currentItem = currentItem + 1
            } else {
                // Finish the splash screens
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        // Initial button visibility
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                btnPrevious.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                btnNext.setImageResource(
                    if (position == images.size - 1) R.drawable.baseline_arrow_forward_ios_24 else R.drawable.baseline_arrow_forward_ios_24
                )
            }
        })
    }
}

