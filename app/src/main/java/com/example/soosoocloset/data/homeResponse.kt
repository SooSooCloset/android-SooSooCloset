package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class homeResponse(
    @SerializedName("resultArray")
    val resultArray: List<JSONObject>,
    @SerializedName("code")
    val code: String
)