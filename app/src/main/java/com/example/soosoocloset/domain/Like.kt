package com.example.soosoocloset.domain

import android.graphics.Bitmap

// author: Sumin, created: 21.05.19
// author: Soohyun, last modified: 21.07.31
class Like (
    val codi_id : Double, // 코디 아이디
    val image : Bitmap, // 코디 이미지
    val nickname: String, // 코디 작성자 닉네임
    var likeCount: Double, // 좋아요 수
    var isChecked: String, // 좋아요 여부
    val date : String, // 코디 작성 날짜
    val description : String // 코디 설명
)
