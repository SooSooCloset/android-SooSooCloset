package com.example.soosoocloset.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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
import com.example.soosoocloset.data.deleteCodiResponse
import com.example.soosoocloset.data.updateCodiResponse
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
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this) //경고 팝업창
                dialog.setTitle("코디를 삭제 하시겠습니까?") //제목
                dialog.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                    deleteCodi() //코디 삭제
                })
                dialog.setNegativeButton("아니요", null)
                dialog.show()
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
                updateCodiDescription()

                et_codi_description.visibility = View.INVISIBLE // 옷 설명 입력창 안보이게
                tv_codi_description.visibility = View.VISIBLE // 옷 설명 보이게

                update_codi = false
                invalidateOptionsMenu() // 메뉴 갱신

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
        RetrofitClient.api.deleteCodiRequest(user_id, codi_id).enqueue(object : Callback<deleteCodiResponse> {
            override fun onFailure(call: Call<deleteCodiResponse>, t: Throwable) {
                Toast.makeText(this@MycodiActivity, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<deleteCodiResponse>, response: Response<deleteCodiResponse>) {
                if(response.isSuccessful) {
                    val result: deleteCodiResponse = response.body()!! // 응답 결과
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

        RetrofitClient.api.updateCodiRequest(codi_id, codi_description).enqueue(object : Callback<updateCodiResponse> {
            override fun onFailure(call: Call<updateCodiResponse>, t: Throwable) {
                Toast.makeText(this@MycodiActivity, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<updateCodiResponse>, response: Response<updateCodiResponse>) {
                if(response.isSuccessful) {
                    val result: updateCodiResponse = response.body()!! // 응답 결과
                    if(result.code.equals("400")) // 에러 발생 시
                        Toast.makeText(this@MycodiActivity,"Error", Toast.LENGTH_SHORT).show()
                    else if(result.code.equals("200")) {
                        Toast.makeText(this@MycodiActivity, "updateCodi Success", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}