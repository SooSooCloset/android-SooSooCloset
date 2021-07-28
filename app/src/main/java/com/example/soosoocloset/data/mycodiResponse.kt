package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class mycodiResponse(
    @SerializedName("mycodi")
    val mycodi: List<Map<*, *>>,
    @SerializedName("code")
    val code: String
)