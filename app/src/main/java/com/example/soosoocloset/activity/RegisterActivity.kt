package com.example.soosoocloset.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.signupResponse
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 회원가입 화면
// author: Soohyun, created: 21.07.11
class RegisterActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var gender: String = "" // 선택한 성별

        // 성별 선택 버튼 클릭시
        rGroup_gender.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.rButton_men -> gender = "남성"
                R.id.rButton_women -> gender = "여성"
            }
        }

        // 가입하기 버튼 클릭시
        btn_register.setOnClickListener{
            // 입력된 정보 가져오는 부분
            var user_name = et_name.text.toString().trim()
            var nickname = et_nickname.text.toString().trim()
            var birth = et_birth.text.toString().trim()
            var user_id = et_id.text.toString().trim()
            var user_pw = et_password.text.toString().trim()
            var check_pw = et_password_check.text.toString().trim()

            // 입력 양식 확인 후 조치, 입력 양식 맞으면 서버와 통신
            if(user_name.equals("")) {
                Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(nickname.equals("")) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(birth.equals("")) {
                Toast.makeText(this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else if(user_id.equals("")) {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(user_pw.equals("")) {
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if(!user_pw.equals(check_pw)) {
                Toast.makeText(this, "비밀번호와 비밀번호 확인이 맞지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 회원가입 서버와 네트워크 통신하는 부분
                RetrofitClient.api.signupRequest(user_name, nickname, gender, birth, user_id, user_pw).enqueue(object : Callback<signupResponse> {
                    // 네트워크 통신 성공한 경우
                    override fun onResponse(call: Call<signupResponse>, response: Response<signupResponse>) {

                        if(response.isSuccessful) {
                            var result: signupResponse = response.body()!! // 응답 결과

                            if(result.code.equals("400")) {
                                Toast.makeText(this@RegisterActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("204")) {
                                Toast.makeText(this@RegisterActivity, "아이디가 중복입니다.", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) {
                                Toast.makeText(this@RegisterActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                    // 네트워크 통신 실패한 경우
                    override fun onFailure(call: Call<signupResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }
}

