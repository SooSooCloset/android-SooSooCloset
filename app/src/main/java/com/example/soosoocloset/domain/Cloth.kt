package com.example.soosoocloset.domain

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

// 설명: 옷장 화면의 옷 리스트 아이템의 객체
// author: Soohyun, created: 21.07.31
class Cloth(
    val cloth_id: Double, // 옷 아이디
    val image: Bitmap, // 옷 이미지
    val description: String // 옷 설명
)