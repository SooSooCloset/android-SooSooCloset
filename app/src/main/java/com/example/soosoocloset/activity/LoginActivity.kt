package com.example.soosoocloset.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.loginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 로그인 화면
// author: Sumin, created: 21.07.11
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_login = btn_login

        btn_login.setOnClickListener{
            var id = et_id.text.toString()
            var pw = et_password.text.toString()

            RetrofitClient.api.loginRequest(id, pw).enqueue(object : Callback<loginResponse> {
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    if(response.isSuccessful){
                        var result: loginResponse = response.body()!! // 응답 결과
                        if(result.code.equals("404")) { // 에러 발생 시
                            Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                        } else if(result.code.equals("204") or result.code.equals("208")) { // Id나 PW를 잘못 입력한 경우
                            Toast.makeText(this@LoginActivity, "Id or password is not correct.", Toast.LENGTH_SHORT).show()
                        } else if(result.code.equals("200")) { // 로그인 성공
                            //자동 로그인
                            val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE)
                            val editor : SharedPreferences.Editor = prefs.edit()
                            editor.putString("id", id)
                            editor.commit()

                            Toast.makeText(this@LoginActivity, "Welcome", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            })
        }

        // 아이디 찾기 클릭시
        tv_find_id.setOnClickListener {
            startActivity(Intent(this, FindIdActivity::class.java))
        }

        // 비밀번호 찾기 클릭시
        tv_find_password.setOnClickListener {
            startActivity(Intent(this, FindPwActivity::class.java))
        }

        // 회원가입 클릭시
        tv_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}