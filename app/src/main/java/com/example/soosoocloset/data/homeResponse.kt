package com.example.soosoocloset.data

import com.google.gson.annotations.SerializedName

data class homeResponse(
    val data: List<homeData>, //json 결과 여러 개를 모두 받음
    val code: String
)
{
    data class homeData (
        @SerializedName("id")
        val id: String,
        @SerializedName("likes")
        val likes: Int
    )
}