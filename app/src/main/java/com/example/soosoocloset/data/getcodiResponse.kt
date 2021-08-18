package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class getcodiResponse(
    @SerializedName("codi")
    val codi: List<Map<*, *>>,
    @SerializedName("likes")
    val likes: List<String>,
    @SerializedName("code")
    val code: String
)