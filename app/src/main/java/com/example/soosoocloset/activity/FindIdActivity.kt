package com.example.soosoocloset.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.findidResponse
import kotlinx.android.synthetic.main.activity_find_id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 로그인 화면에서 아이디 찾기 -> 아이디 찾기 화면
// author: Sumin, created: 21.07.13
class FindIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // toolbar를 액션바로 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 삭제
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon) // 뒤로가기 버튼 아이콘 변경

        btn_findid.setOnClickListener{
            var name = et_name.text.toString()
            var birth = et_birth.text.toString()

            //아이디찾기 서버와 통신
            RetrofitClient.api.findidRequest(name, birth).enqueue(object : Callback<findidResponse> {
                override fun onFailure(call: Call<findidResponse>, t: Throwable) {
                    tv_findid.setText("Network error")
                }

                override fun onResponse(call: Call<findidResponse>, response: Response<findidResponse>) {
                    if(response.isSuccessful){
                        var result: findidResponse = response.body()!! // 응답 결과
                        if(result.code.equals("404")) { // 에러 발생 시
                            tv_findid.setText("Error")
                        } else if(result.code.equals("204")) { // 가입하지 않은 사용자인 경우
                            tv_findid.setText("Never registered")
                        } else if(result.code.equals("200")) { // 가입한 사용자의 경우
                            tv_findid.setText(result.id)
                        }
                    }
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼 클릭한 경우
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}