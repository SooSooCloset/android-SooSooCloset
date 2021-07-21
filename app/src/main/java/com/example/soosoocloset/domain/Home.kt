package com.example.soosoocloset.domain

import com.google.gson.annotations.SerializedName

// author: Sumin, created: 21.05.19
class Home(
    @SerializedName("user_id")
    val nickname: String,
    @SerializedName("likes")
    val likeCount: String
)