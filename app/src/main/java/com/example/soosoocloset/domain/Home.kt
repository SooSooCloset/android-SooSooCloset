package com.example.soosoocloset.domain

import com.google.gson.annotations.SerializedName

// author: Sumin, created: 21.05.19
class Home(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("likeCount")
    val likeCount: String
    //@SerializedName("codi_img")
    //val codi_img: String
)