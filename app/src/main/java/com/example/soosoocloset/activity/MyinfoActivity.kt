package com.example.soosoocloset.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.myinfoResponse
import kotlinx.android.synthetic.main.activity_myinfo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)

        //et_nickname.setText(intent.getStringExtra("nickname"))

        btn_update.setOnClickListener {
            val nickname = et_nickname.text.toString().trim()
            val user_pw = et_pw.text.toString().trim()
            val check_pw = et_check_pw.text.toString().trim()
            val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
            val user_id = prefs.getString("id", null)!! // 아이디 값 가져오기

            // 입력 양식 맞으면 서버와 통신, 틀리면 메세지 띄움
            if(nickname.equals("")) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(user_pw.equals("")) {
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(!user_pw.equals(check_pw)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                //내정보 수정 서버와 통신
                RetrofitClient.api.myinfoRequest(nickname, user_pw, user_id).enqueue(object : Callback<myinfoResponse> {
                    override fun onFailure(call: Call<myinfoResponse>, t: Throwable) {
                        Toast.makeText(this@MyinfoActivity, "Network error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<myinfoResponse>, response: Response<myinfoResponse>) {
                        if(response.isSuccessful) {
                            val result: myinfoResponse = response.body()!! // 응답 결과
                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(this@MyinfoActivity ,"Error", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) { // 내정보 수정 성공 시
                                Toast.makeText(this@MyinfoActivity ,"Update Success", Toast.LENGTH_SHORT).show()
                                finish() //activity 종료
                            }
                        }
                    }
                })
            }
        }
    }
}