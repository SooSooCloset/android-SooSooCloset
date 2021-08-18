package com.example.soosoocloset.domain

import android.graphics.Bitmap

// author: Sumin, created: 21.05.19
class Codi (
    val codi_id: Double,
    val image : Bitmap,
    val codi_description : String,
    val likes : Double,
    var isChecked: String,
    val codi_date : String
)