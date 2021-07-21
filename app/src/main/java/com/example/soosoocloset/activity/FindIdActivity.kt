package com.example.soosoocloset.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.findidResponse
import kotlinx.android.synthetic.main.activity_find_id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)

        val btn_findid = btn_findid

        btn_findid.setOnClickListener{
            var name = et_name.text.toString()
            var birth = et_birth.text.toString()

            //아이디찾기 서버와 통신
            RetrofitClient.api.findidRequest(name, birth).enqueue(object : Callback<findidResponse> {
                override fun onFailure(call: Call<findidResponse>, t: Throwable) {
                    tv_findid.setText("Network error")
                }

                override fun onResponse(call: Call<findidResponse>, response: Response<findidResponse>) {
                    var result: findidResponse = response.body()!! // 응답 결과
                    if(result.code.equals("404")) { // 에러 발생 시
                        tv_findid.setText("Error")
                    } else if(result.code.equals("204")) { // 가입하지 않은 사용자인 경우
                        tv_findid.setText("Never registered")
                    } else if(result.code.equals("200")) { // 가입한 사용자의 경우
                        tv_findid.setText(result.id)
                    }
                }
            })
        }
    }
}