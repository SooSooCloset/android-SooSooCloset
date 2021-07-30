package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class mypageResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profile")
    val profile: List<Map<*, *>>
)