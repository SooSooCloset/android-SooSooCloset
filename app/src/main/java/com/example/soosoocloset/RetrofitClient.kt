package com.example.soosoocloset

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    val api: RetrofitApi = initApi()

    private fun initApi(): RetrofitApi =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000") // localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
}