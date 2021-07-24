package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class getclothResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("outer")
    val outer: List<Map<*, *>>,
    @SerializedName("top")
    val top: List<Map<*, *>>,
    @SerializedName("bottom")
    val bottom: List<Map<*, *>>,
    @SerializedName("onepiece")
    val onepiece: List<Map<*, *>>,
    @SerializedName("shoes")
    val shoes: List<Map<*, *>>,
    @SerializedName("accessary")
    val accessary: List<Map<*, *>>
)