package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class getclothResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("cloth")
    val cloth: List<Map<*, *>>
    /*
    @SerializedName("cloth_id")
    val cloth_id: ArrayList<Int>,
    @SerializedName("cloth_img")
    val cloth_img: List<Map<*, *>>,
    @SerializedName("description")
    val description: ArrayList<String>*/
    /*
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
    val accessary: List<Map<*, *>>*/
)