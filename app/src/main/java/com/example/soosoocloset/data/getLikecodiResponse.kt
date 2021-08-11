package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class getLikecodiResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("like")
    val like: List<Map<*, *>>
)