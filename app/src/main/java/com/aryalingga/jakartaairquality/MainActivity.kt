package com.aryalingga.jakartaairquality

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val aqiService by lazy { RetrofitClient.aqiService }
    private val token = "08a2573a82f676630e8c2f83bfa6655a5e77b83c"
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvAQIDescription: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var tvCity: TextView
    private lateinit var tvAQI: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout
    private lateinit var imgShowPopup: ImageView
    private lateinit var tvAQIPenjelasan: TextView
    private lateinit var tvAQIDampak: TextView

    private lateinit var imgProfile: ImageView
    private lateinit var tvUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        progressBar = findViewById(R.id.progressBar)
        contentLayout = findViewById(R.id.layout_content)
        tvAQIDescription = findViewById(R.id.tvAQIDescription)
        tvCity = findViewById(R.id.tvCity)
        tvAQI = findViewById(R.id.tvAQI)
        imgShowPopup = findViewById(R.id.imgShowPopup)
        tvAQIPenjelasan = findViewById(R.id.tvAQIPenjelasan)
        tvAQIDampak = findViewById(R.id.tvAQIDampak)

        // Initialize header views
        val headerView = navigationView.getHeaderView(0)
        imgProfile = headerView.findViewById(R.id.imgProfile)
        tvUserName = headerView.findViewById(R.id.tvUserName)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        getCityAQI("jakarta")

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set ImageView click listener to show PopupWindow
        imgShowPopup.setOnClickListener {
            showPopupWindow()
        }

        val user = auth.currentUser
        updateUI(user)
        updateMenu()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_tips -> {
                val intent = Intent(this, TipsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_login -> {
                val user = auth.currentUser
                if (user == null) {
                    signIn()
                } else {
                    auth.signOut()
                    googleSignInClient.signOut().addOnCompleteListener {
                        updateUI(null)
                        Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                        updateMenu()
                    }
                }
            }
            R.id.nav_logout -> {
                auth.signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    updateUI(null)
                    Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                    updateMenu()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateMenu() {
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val menu: Menu = navigationView.menu
        val user = auth.currentUser

        menu.findItem(R.id.nav_login).isVisible = user == null // Hide login menu item if user is logged in
        menu.findItem(R.id.nav_logout).isVisible = user != null // Show logout menu item only if logged in
        menu.findItem(R.id.nav_profile).isVisible = user != null // Show profile menu item only if logged in
    }

    private fun getCityAQI(city: String) {
        progressBar.visibility = View.VISIBLE
        contentLayout.visibility = View.GONE
        tvCity.visibility = View.GONE
        tvAQI.visibility = View.GONE
        tvAQIDescription.visibility = View.GONE
        tvAQIPenjelasan.visibility = View.GONE
        tvAQIDampak.visibility = View.GONE

        aqiService.getCityAQI(city, token).enqueue(object : Callback<AQIResponse> {
            override fun onResponse(call: Call<AQIResponse>, response: Response<AQIResponse>) {
                progressBar.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        val aqi = it.data.aqi
                        tvCity.text = it.data.city.name
                        tvAQI.text = aqi.toString()

                        setAQIColor(tvAQI, aqi)
                        setAQIDescription(tvAQIDescription, aqi)
                        setAQIPenjelasan(tvAQIPenjelasan, aqi)
                        setAQIDampak(tvAQIDampak, aqi)

                        tvCity.visibility = View.VISIBLE
                        tvAQI.visibility = View.VISIBLE
                        tvAQIDescription.visibility = View.VISIBLE
                        tvAQIPenjelasan.visibility = View.VISIBLE
                        tvAQIDampak.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AQIResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, "Failed to load data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAQIColor(tvAQI: TextView, aqi: Int) {
        when (aqi) {
            in 0..50 -> tvAQI.setTextColor(Color.GREEN)
            in 51..100 -> tvAQI.setTextColor(Color.YELLOW)
            in 101..150 -> tvAQI.setTextColor(Color.parseColor("#FFA500")) // Orange
            in 151..200 -> tvAQI.setTextColor(Color.RED)
            in 201..300 -> tvAQI.setTextColor(Color.parseColor("#800080")) // Ungu
            else -> tvAQI.setTextColor(Color.parseColor("#800000")) // Merah Maroon
        }
    }

    private fun setAQIDescription(tvAQIDescription: TextView, aqi: Int) {
        val (description, color) = when (aqi) {
            in 0..50 -> "Good" to Color.GREEN
            in 51..100 -> "Moderate" to Color.YELLOW
            in 101..150 -> "Unhealthy for Sensitive Groups" to Color.parseColor("#FFA500")
            in 151..200 -> "Unhealthy" to Color.RED
            in 201..300 -> "Very Unhealthy" to Color.parseColor("#800080")
            else -> "Hazardous" to Color.parseColor("#800000")
        }
        tvAQIDescription.text = description
        tvAQIDescription.setTextColor(color)
    }

    private fun setAQIPenjelasan(tvAQIPenjelasan: TextView, aqi: Int) {
        val penjelasan = when (aqi) {
            in 0..50 -> "Kualitas udara dianggap memuaskan, dan polusi udara hanya menimbulkan sedikit risiko atau bahkan tidak sama sekali\""
            in 51..100 -> "Anak-anak dan orang dewasa yang aktif, serta penderita penyakit pernapasan, seperti asma, harus membatasi aktivitas di luar ruangan dalam waktu lama."
            in 101..150 -> "Anak-anak dan orang dewasa yang aktif, serta penderita penyakit pernapasan, seperti asma, harus membatasi aktivitas di luar ruangan dalam waktu lama."
            in 151..200 -> "Anak-anak dan orang dewasa yang aktif, serta penderita penyakit pernapasan, seperti asma, harus menghindari aktivitas di luar ruangan dalam waktu lama. Semua orang, terutama anak-anak, harus membatasi aktivitas di luar ruangan dalam waktu lama."
            in 201..300 -> "Anak-anak dan orang dewasa yang aktif, serta penderita penyakit pernapasan, seperti asma, harus menghindari aktivitas di luar ruangan; semua orang, terutama anak-anak, harus membatasi aktivitas di luar ruangan."
            else -> "Setiap orang harus menghindari semua aktivitas di luar ruangan."
        }
        tvAQIPenjelasan.text = penjelasan
    }

    private fun setAQIDampak(tvAQIDampak: TextView, aqi: Int) {
        val dampak = when (aqi) {
            in 0..50 -> "Tidak ada dampak kesehatan."
            in 51..100 -> "Kualitas udara dapat diterima namun, untuk beberapa polutan mungkin terdapat masalah kesehatan yang moderat bagi sejumlah kecil orang yang sangat sensitif terhadap polusi udara."
            in 101..150 -> "Anggota kelompok sensitif mungkin mengalami dampak kesehatan. Masyarakat umum kemungkinan besar tidak akan terpengaruh."
            in 151..200 -> "Setiap orang mungkin mulai mengalami dampak kesehatan, anggota kelompok sensitif mungkin mengalami dampak kesehatan yang lebih serius"
            in 201..300 -> "Peringatan kesehatan tentang kondisi darurat. Seluruh populasi lebih mungkin terkena dampaknya."
            else -> "Peringatan kesehatan: setiap orang mungkin mengalami dampak kesehatan yang lebih serius"
        }
        tvAQIDampak.text = "Dampak :\n$dampak"
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    updateMenu()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    updateMenu()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            //Kondisi Sudah Login
            tvUserName.text = user.displayName
            tvUserName.visibility = View.VISIBLE
            imgProfile.visibility = View.VISIBLE

            Glide.with(this)
                .load(user.photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(imgProfile)
        } else {
            //Kondisi Belum Login
            tvUserName.text = "Guest"
            tvUserName.visibility = View.VISIBLE
            imgProfile.visibility = View.GONE
        }
    }

    private fun showPopupWindow() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_keterangan, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(imgShowPopup, 0, 0)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val RC_SIGN_IN = 9001
    }
}
