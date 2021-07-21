package com.example.soosoocloset

import com.example.soosoocloset.data.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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

    // 로그인 API
    @FormUrlEncoded
    @POST("/user/login")
    fun loginRequest(
        @Field("user_id") id: String,
        @Field("user_pw") pw: String
    ) : Call<loginResponse>

    // 아이디 찾기 API
    @FormUrlEncoded
    @POST("/user/findid")
    fun findidRequest(
        @Field("user_name") name: String,
        @Field("birth") birth: String
    ) : Call<findidResponse>

    // 홈 화면 API
    //@FormUrlEncoded
    @POST("/user/home")
    fun homeRequest(
    ) : Call<homeResponse>

}