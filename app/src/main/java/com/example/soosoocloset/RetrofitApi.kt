package com.example.soosoocloset

import com.example.soosoocloset.data.signupResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitApi {
    // 회원가입 API
    @FormUrlEncoded
    @POST("/user/signup")
    fun signupRequest(@Field("user_name") name: String,
                      @Field("nickname") nickname: String,
                      @Field("gender") gender: String,
                      @Field("birth") birth: String,
                      @Field("user_id") id: String,
                      @Field("user_pw") pw: String): Call<signupResponse>
}