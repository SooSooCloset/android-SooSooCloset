package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class findidResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("id")
    val id: String
)