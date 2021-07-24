package com.example.soosoocloset.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import kotlinx.android.synthetic.main.activity_cloth.*

class ClothActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

        // closetFragment 로부터 받은 데이터 가져오기
        val cloth_id = intent.getDoubleExtra("cloth_id", 0.0)
        val cloth_img = intent.getParcelableExtra<Uri>("cloth_img")
        val description = intent.getStringExtra("description")

        // 옷 이미지와 설명 세팅
        Glide.with(applicationContext).load(cloth_img).into(iv_cloth)
        tv_cloth_description.setText(description)
    }
}