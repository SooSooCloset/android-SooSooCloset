package com.example.soosoocloset.activity

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.codiResponse
import kotlinx.android.synthetic.main.activity_mycodi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//설명: 나의 코디 상세 화면
// Author : Sumin, Last Modified : 2021.07.30
class MycodiActivity : AppCompatActivity() {
    var update_codi = false // 코디 수정 선택 여부를 저장하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycodi)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // 커스텀 툴바로 설정
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기존 상단바 타이틀 없애기
        et_codi_description.visibility = View.INVISIBLE

        //CodiFragment 데이터 받아오기
        val codi_img = intent.getParcelableExtra<Uri>("codi_img")
        val codi_description = intent.getStringExtra("codi_description")
        val likes = intent.getDoubleExtra("likes", 0.0).toInt().toString()
        val isChecked = intent.getStringExtra("isChecked")
        val codi_date = intent.getStringExtra("codi_date")

        //나의 코디 화면 값들 재설정
        Glide.with(applicationContext).load(codi_img).into(iv_mycodi)
        tv_codi_description.setText(codi_description)
        tv_likes_num.setText(likes)
        tv_codi_date.setText(codi_date)

        if(isChecked.equals("true"))
            mycodi_cb_like.isChecked = true
        else if(isChecked.equals("false"))
            mycodi_cb_like.isChecked = false
    }

    // 상단바와 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(update_codi) { // 코디 수정 선택한 경우
            menuInflater.inflate(R.menu.cloth_mycodi_menu, menu)
        } else { // 코디 수정 선택하지 않은 경우
            menuInflater.inflate(R.menu.mycodi_menu, menu)
        }
        return true
    }

    // 상단바의 메뉴 클릭시 호출되는 메소드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_delete_codi -> { // 코디 삭제 클릭
                // 다이얼로그 생성
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("코디를 삭제하시겠습니까?") // 타이틀 설정
                    .setPositiveButton("예") { dialog, which -> // 오른쪽 버튼 설정
                        deleteCodi() //코디 삭제 메소드 호출
                    }
                    .setNegativeButton("아니오", null) // 왼쪽 버튼 설정 - 취소시 아무 것도 하지 않음
                    .create()

                alertDialog.setOnShowListener {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FCCACA"))
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#696969"))
                }

                alertDialog.show() // 다이얼로그를 보여줌
                return true
            }
            R.id.item_update_codi -> { // 코디 수정 클릭
                et_codi_description.visibility = View.VISIBLE // 코디 설명 입력창 보이게
                tv_codi_description.visibility = View.INVISIBLE // 코디 설명 안보이게
                et_codi_description.setText(tv_codi_description.text)

                update_codi = true
                invalidateOptionsMenu() // 메뉴 갱신

                return true
            }
            R.id.item_update_description -> { // 체크버튼 클릭
                updateCodiDescription() //코디 설명 수정 메소드 호출
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    //코디 삭제 메소드
    fun deleteCodi() {
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장 장소
        val user_id = prefs.getString("id", null)!! // 사용자 아이디
        val codi_id = intent.getDoubleExtra("codi_id", 0.0).toInt() //삭제할 코디의 아이디

        //코디 삭제 서버와 통신
        RetrofitClient.api.deleteCodiRequest(user_id, codi_id).enqueue(object : Callback<codiResponse> {
            override fun onFailure(call: Call<codiResponse>, t: Throwable) {
                Toast.makeText(this@MycodiActivity, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<codiResponse>, response: Response<codiResponse>) {
                if(response.isSuccessful) {
                    val result: codiResponse = response.body()!! // 응답 결과
                    if(result.code.equals("400")) // 에러 발생 시
                        Toast.makeText(this@MycodiActivity,"Error", Toast.LENGTH_SHORT).show()
                    else if(result.code.equals("200")) {
                        Toast.makeText(this@MycodiActivity, "Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        })
    }

    //코디 설명 메소드
    fun updateCodiDescription() {
        val codi_id = intent.getDoubleExtra("codi_id", 0.0).toInt() //코디 아이디
        val codi_description = et_codi_description.text.toString().trim()

        RetrofitClient.api.updateCodiRequest(codi_id, codi_description).enqueue(object : Callback<codiResponse> {
            override fun onFailure(call: Call<codiResponse>, t: Throwable) {
                Toast.makeText(this@MycodiActivity, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<codiResponse>, response: Response<codiResponse>) {
                if(response.isSuccessful) {
                    val result: codiResponse = response.body()!! // 응답 결과
                    if(result.code.equals("400")) // 에러 발생 시
                        Toast.makeText(this@MycodiActivity,"Error", Toast.LENGTH_SHORT).show()
                    else if(result.code.equals("200")) {
                        et_codi_description.visibility = View.INVISIBLE // 옷 설명 입력창 안보이게
                        tv_codi_description.visibility = View.VISIBLE // 옷 설명 보이게

                        update_codi = false
                        invalidateOptionsMenu() // 메뉴 갱신
                        tv_codi_description.text = codi_description

                    }
                }
            }
        })
    }
}