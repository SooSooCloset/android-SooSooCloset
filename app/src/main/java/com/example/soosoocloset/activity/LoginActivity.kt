package com.example.soosoocloset.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.loginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_login = findViewById<Button>(R.id.btn_login)

        btn_login.setOnClickListener{
            var id = et_id.text.toString()
            var pw = et_password.text.toString()

            RetrofitClient.api.requestLogin(id, pw).enqueue(object: Callback<loginResponse> {
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("error")
                    dialog.setMessage("호출 실패")
                    dialog.show()
                }

                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    var login = response.body()
                    Log.d("LOGIN","msg : "+login?.message)
                    Log.d("LOGIN","code : "+login?.code)
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle(login?.message)
                    dialog.setMessage(login?.code)
                    dialog.show()
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