package com.example.soosoocloset.domain

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

// author: Sumin, created: 21.05.19
class Home (
    @SerializedName("codi_img")
    val image: Bitmap,
    @SerializedName("user_id")
    val nickname: String,
    @SerializedName("likes")
    val likeCount: String
)