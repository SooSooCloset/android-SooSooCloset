package com.example.soosoocloset.activity

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.likeResponse
import kotlinx.android.synthetic.main.activity_codi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 코디 상세 화면(홈 아이템, 좋아요 아이템 클릭 후 화면)
// author: Sumin, last modified: 21.08.10
class CodiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codi)

        // HomeFragment, LikeFragment 에서 데이터 받아오기
        val codi_id = intent.getDoubleExtra("codi_id", 0.0).toInt()
        val nickname = intent.getStringExtra("nickname")
        val codi_img = intent.getParcelableExtra<Uri>("codi_img")
        val codi_description = intent.getStringExtra("codi_description")
        var likes = intent.getDoubleExtra("likes", -1.0).toInt()
        val codi_date = intent.getStringExtra("codi_date")

        //홈 코디 상세 화면 값들 재설정
        tv_nickname.text = nickname
        Glide.with(applicationContext).load(codi_img).into(iv_codi)
        tv_codi_description.text = codi_description
        tv_likes_num.text = likes.toString()
        tv_codi_date.text = codi_date

        /*if(isChecked.equals("true"))
        codi_cb_like.isChecked = true
        else if(isChecked.equals("false"))
            codi_cb_like.isChecked = false*/

        val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장 장소
        val user_id = prefs.getString("id", null)!! // 아이디 값 가져오기

        //좋아요 체크박스 클릭 이벤트 처리
        codi_cb_like.setOnClickListener {
            if(codi_cb_like.isChecked) { //좋아요 체크박스 채우는 이벤트 발생 시
                likes += 1 //좋아요 수 +1

                //좋아요 추가 서버와 통신
                RetrofitClient.api.addLikeRequest(user_id, codi_id, likes).enqueue(object : Callback<likeResponse> {
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(this@CodiActivity, "Network error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful){
                            var result: likeResponse = response.body()!! // 응답 결과
                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(this@CodiActivity ,"Error", Toast.LENGTH_SHORT).show()
                            }
                            else if(result.code.equals("200")) { // 통신 성공 시
                                tv_likes_num.text = likes.toString()
                                Toast.makeText(this@CodiActivity ,"addLike success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
            else { //좋아요 체크박스 비우는 이벤트 발생 시
                likes -= 1 //좋아요 수 -1

                //좋아요 삭제 서버와 통신
                RetrofitClient.api.deleteLikeRequest(user_id, codi_id, likes).enqueue(object : Callback<likeResponse> {
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(this@CodiActivity, "Network error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful){
                            var result: likeResponse = response.body()!! // 응답 결과
                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(this@CodiActivity ,"Error", Toast.LENGTH_SHORT).show()
                            }
                            else if(result.code.equals("200")) { // 통신 성공 시
                                tv_likes_num.text = likes.toString()
                                Toast.makeText(this@CodiActivity ,"deleteLike success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }
}