package com.example.soosoocloset.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.soosoocloset.R

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        Handler().postDelayed({
            val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
            val id = prefs.getString("id", null) // 아이디 값 가져오기

            if(id == null) {
                startActivity(Intent(this, LoginActivity::class.java)) // 자동 로그인 정보 없으면 로그인 화면으로
            } else {
                startActivity(Intent(this, MainActivity::class.java)) // 자동 로그인 정보 있으면 메인 화면으로
            }

            finish()
        }, 3000)
    }
}