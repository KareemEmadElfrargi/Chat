package com.example.chat.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.chat.R
import com.example.chat.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private lateinit var authentication: FirebaseAuth
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startUp()
        var delayMillis:Long = 0
        delayMillis = if (authentication.currentUser != null){2700}else{2700}
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            checkStatusOfUser()
        },delayMillis)
    }

    private fun checkStatusOfUser() {
        if (authentication.currentUser != null) {
            startActivity(Intent(this@Splash,MainActivity::class.java))
        }else{
            startActivity(Intent(this@Splash,Login::class.java))
        }
    }

    private fun startUp() {
        authentication = FirebaseAuth.getInstance()
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}