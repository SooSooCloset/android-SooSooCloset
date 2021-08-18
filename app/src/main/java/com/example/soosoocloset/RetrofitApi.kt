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
    fun signupRequest(
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
    fun findpwRequest(
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
    @FormUrlEncoded
    @POST("/user/home")
    fun homeRequest(
        @Field("user_id") id: String
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

    // 옷 추가 API
    @Multipart
    @POST("/cloth/addcloth")
    fun addclothRequest(
        @Part("user_id") id: RequestBody,
        @Part("category") category: RequestBody,
        @Part cloth_img: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<clothResponse>

    // 옷 가져오기 API
    @FormUrlEncoded
    @POST("/cloth/getcloth")
    fun getclothRequest(
        @Field("user_id") id: String,
        @Field("category") category: String
    ) : Call<getclothResponse>

    // 옷 삭제하기 API
    @FormUrlEncoded
    @POST("/cloth/delete")
    fun deleteClothequest(
        @Field("cloth_id") id: Int
    ) : Call<clothResponse>

    // 옷 수정하기 API
    @FormUrlEncoded
    @POST("/cloth/update")
    fun updateClothRequest(
        @Field("cloth_id") id: Int,
        @Field("description") description: String
    ) : Call<clothResponse>

    //내정보 가져오기 API
    @FormUrlEncoded
    @POST("user/myinfo")
    fun myinfoRequest(
        @Field("nickname") nickname: String,
        @Field("user_pw") pw: String,
        @Field("user_id") id: String
    ) : Call<myinfoResponse>

    // 좋아요한 코디 가져오기 API
    @FormUrlEncoded
    @POST("/codi/getLikecodi")
    fun getLikecodiRequest(
        @Field("user_id") id: String
    ) : Call<getLikecodiResponse>

    // 마이페이지 화면 API
    @FormUrlEncoded
    @POST("/user/mypage")
    fun mypageRequest(
        @Field("user_id") id: String
    ) : Call<mypageResponse>

    // 프로필 사진 변경 API
    @Multipart
    @POST("/user/changeProfile")
    fun changeProfileRequest(
        @Part("user_id") id: RequestBody,
        @Part profile_img: MultipartBody.Part
    ) : Call<changeProfileResponse>

    // 회원탈퇴 API
    @FormUrlEncoded
    @POST("/user/deleteUser")
    fun deleteUserRequest(
        @Field("user_id") id: String
    ) : Call<deleteUserResponse>

    // 코디 삭제 API
    @FormUrlEncoded
    @POST("/codi/deleteCodi")
    fun deleteCodiRequest(
        @Field("user_id") id: String,
        @Field("codi_id") codi_id: Int
    ) : Call<deleteCodiResponse>

    // 좋아요 추가 API
    @FormUrlEncoded
    @POST("/codi/addLike")
    fun addLikeRequest(
        @Field("user_id") id: String,
        @Field("codi_id") codi_id: Int,
        @Field("likes") likes: Int
    ) : Call<likeResponse>

    // 좋아요 삭제 API
    @FormUrlEncoded
    @POST("/codi/deleteLike")
    fun deleteLikeRequest(
        @Field("user_id") id: String,
        @Field("codi_id") codi_id: Int,
        @Field("likes") likes: Int
    ) : Call<likeResponse>

    // 코디 설명 수정 API
    @FormUrlEncoded
    @POST("/codi/updateCodi")
    fun updateCodiRequest(
        @Field("codi_id") codi_id: Int,
        @Field("codi_description") codi_description: String
    ) : Call<updateCodiResponse>

}