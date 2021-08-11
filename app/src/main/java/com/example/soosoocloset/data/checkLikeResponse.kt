package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class checkLikeResponse (
    @SerializedName("codi_id")
    val codi_id: List<Map<*, *>>,
    @SerializedName("code")
    val code: String
)