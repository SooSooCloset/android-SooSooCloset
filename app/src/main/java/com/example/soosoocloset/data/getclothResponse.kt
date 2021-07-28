package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class getclothResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("cloth")
    val cloth: List<Map<*, *>>
)