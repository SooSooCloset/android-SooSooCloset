package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class mypageResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("info")
    val info: List<Map<*, *>>
)