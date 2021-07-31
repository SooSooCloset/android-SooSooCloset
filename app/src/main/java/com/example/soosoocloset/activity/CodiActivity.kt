package com.example.soosoocloset.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import kotlinx.android.synthetic.main.activity_codi.*

class CodiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codi)

        //HomeFragment에서 데이터 받아오기
        val nickname = intent.getStringExtra("nickname")
        val codi_img = intent.getParcelableExtra<Uri>("codi_img")
        val codi_description = intent.getStringExtra("codi_description")
        val likes = intent.getDoubleExtra("likes", 0.0).toInt().toString()
        val codi_date = intent.getStringExtra("codi_date")

        //홈 코디 상세 화면 값들 재설정
        tv_nickname.setText(nickname)
        Glide.with(applicationContext).load(codi_img).into(iv_codi)
        tv_codi_description.setText(codi_description)
        tv_likes_num.setText(likes)
        tv_codi_date.setText(codi_date)
    }
}