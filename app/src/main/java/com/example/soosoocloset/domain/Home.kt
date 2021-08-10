package com.example.soosoocloset.domain

import android.graphics.Bitmap

// author: Sumin, created: 21.05.19
class Home(
    val codi_id: Double,
    val nickname: String,
    val image: Bitmap,
    val codi_description: String,
    var likes: Double,
    //var isChecked: String,
    val codi_date: String
)