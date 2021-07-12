package com.example.soosoocloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        Handler().postDelayed({
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }, 3000)
    }
}