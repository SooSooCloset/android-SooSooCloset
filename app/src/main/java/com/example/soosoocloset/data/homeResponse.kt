package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class homeResponse(
    @SerializedName("resultArray")
    val resultArray: JSONArray,
    @SerializedName("code")
    val code: String
)