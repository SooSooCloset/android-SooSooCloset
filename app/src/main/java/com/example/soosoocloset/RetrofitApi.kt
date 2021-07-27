package com.example.soosoocloset

import com.example.soosoocloset.data.findpwResponse
import com.example.soosoocloset.data.findidResponse
import com.example.soosoocloset.data.homeResponse
import com.example.soosoocloset.data.loginResponse
import com.example.soosoocloset.data.signupResponse
import com.example.soosoocloset.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {
    // 회원가입 API
    @FormUrlEncoded
    @POST("/user/signup")
    fun requestSignup(
        @Field("user_name") name: String,
        @Field("nickname") nickname: String,
        @Field("gender") gender: String,
        @Field("birth") birth: String,
        @Field("user_id") id: String,
        @Field("user_pw") pw: String
    ): Call<signupResponse>

    // 로그인 API
    @FormUrlEncoded
    @POST("/user/login")
    fun loginRequest(
        @Field("user_id") id: String,
        @Field("user_pw") pw: String
    ) : Call<loginResponse>

    // 비밀번호 찾기 API
    @FormUrlEncoded
    @POST("/user/findpw")
    fun requestFindpw(
        @Field("user_id") id:String,
        @Field("user_email") email:String
    ) : Call<findpwResponse>

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

    // 코디 화면 API
    @FormUrlEncoded
    @POST("/codi/getcodi")
    fun getcodiRequest(
        @Field("user_id") id: String
    ) : Call<getcodiResponse>

    // 코디 추가 API
    @Multipart
    @POST("/codi/addcodi")
    fun addcodiRequest(
        @Part("user_id") id: RequestBody,
        @Part codi_img: MultipartBody.Part,
        @Part ("codi_description") codi_description: RequestBody,
        @Part ("codi_date") codi_date: RequestBody
    ) : Call<addcodiResponse>

    // 나의 코디 화면 API
    @FormUrlEncoded
    @POST("/codi/mycodi")
    fun mycodiRequest(
        @Field("user_id") id: String
    ) : Call<mycodiResponse>
}