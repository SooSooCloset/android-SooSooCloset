package com.example.soosoocloset.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.findpwResponse
import kotlinx.android.synthetic.main.activity_find_pw.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 로그인 화면에서 비밀번호 찾기 -> 비밀번호 찾기 화면
// author: Soohyun, created: 21.07.13
class FindPwActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_pw)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // toolbar를 액션바로 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 삭제
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon) // 뒤로가기 버튼 아이콘 변경

        btn_findpw.setOnClickListener{
            // 입력된 정보 가져오는 부분
            var user_id = et_id.text.toString().trim()
            var user_email = et_email.text.toString().trim()

            // 입력 양식 확인 후 조치, 입력 양식 맞으면 서버와 통신
            if(user_id.equals("")) {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(user_email.equals("")) {
                Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 회원가입 서버와 네트워크 통신하는 부분
                RetrofitClient.api.findpwRequest(user_id, user_email).enqueue(object : Callback<findpwResponse> {
                    // 네트워크 통신 성공한 경우
                    override fun onResponse(call: Call<findpwResponse>, response: Response<findpwResponse>) {

                        if(response.isSuccessful) {
                            var result: findpwResponse = response.body()!! // 응답 결과

                            if(result.code.equals("400")) {
                                Toast.makeText(this@FindPwActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("204")) {
                                Toast.makeText(this@FindPwActivity, "등록된 아이디가 없습니다.", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) {
                                Toast.makeText(this@FindPwActivity, "이메일로 임시 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                    // 네트워크 통신 실패한 경우
                    override fun onFailure(call: Call<findpwResponse>, t: Throwable) {
                        Toast.makeText(this@FindPwActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
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