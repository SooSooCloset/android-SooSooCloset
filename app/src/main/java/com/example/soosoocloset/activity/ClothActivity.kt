package com.example.soosoocloset.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.clothResponse
import kotlinx.android.synthetic.main.activity_cloth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClothActivity : AppCompatActivity() {
    var update_cloth = false // 옷 수정 선택 여부를 저장하는 변수
    var cloth_id: Int = 0 // 옷 아이디

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

        val toolbar: Toolbar = findViewById(R.id.toolbar) // 상단바
        setSupportActionBar(toolbar) // 상단바를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) // 액션바의 타이틀을 숨김
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon) // 뒤로가기 버튼 아이콘 변경
        et_cloth_description.visibility = View.INVISIBLE

        // closetFragment 로부터 받은 데이터 가져오기
        cloth_id = intent.getDoubleExtra("cloth_id", 0.0).toInt()
        val cloth_img = intent.getParcelableExtra<Uri>("cloth_img")
        val description = intent.getStringExtra("description")

        // 옷 이미지와 설명 세팅
        Glide.with(applicationContext).load(cloth_img).into(iv_cloth)
        tv_cloth_description.setText(description)
    }

    // 상단바와 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(update_cloth) { // 옷 수정 선택한 경우
            menuInflater.inflate(R.menu.cloth_mycodi_menu, menu)
        } else { // 옷 수정 선택하지 않은 경우
            menuInflater.inflate(R.menu.cloth_menu, menu)
        }
        return true
    }

    // 상단바의 메뉴 클릭시 호출되는 메소드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> { // 뒤로가기 버튼 클릭한 경우
                finish()
                return true
            }
            R.id.item_delete_cloth -> { // 옷 삭제 클릭
                deleteCloth() // 옷 삭제
                return true
            }
            R.id.item_update_cloth -> { // 옷 수정 클릭
                et_cloth_description.visibility = View.VISIBLE // 옷 설명 입력창 보이게
                tv_cloth_description.visibility = View.INVISIBLE // 옷 설명 안보이게
                et_cloth_description.setText(tv_cloth_description.text)

                update_cloth = true
                invalidateOptionsMenu() // 메뉴 갱신

                return true
            }
            R.id.item_update_description -> { // 체크버튼 클릭
                updateCloth() // 옷 수정
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    // 옷 삭제 메서드
    private fun deleteCloth() {
        // 옷 삭제 서버와 네트워크 통신하는 부분
        RetrofitClient.api.deleteClothequest(cloth_id).enqueue(object : Callback<clothResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<clothResponse>, response: Response<clothResponse>) {

                if(response.isSuccessful) {
                    var result: clothResponse = response.body()!! // 응답 결과

                    if(result.code.equals("400")) {
                        Toast.makeText(this@ClothActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    } else if(result.code.equals("200")) {
                        finish()
                    }
                }

            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<clothResponse>, t: Throwable) {
                Toast.makeText(this@ClothActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 옷 수정 메서드
    private fun updateCloth() {
        var description = et_cloth_description.text.toString().trim() // 수정된 옷 설명

        // 옷 수정 서버와 네트워크 통신하는 부분
        RetrofitClient.api.updateClothRequest(cloth_id, description).enqueue(object : Callback<clothResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<clothResponse>, response: Response<clothResponse>) {

                if(response.isSuccessful) {
                    var result: clothResponse = response.body()!! // 응답 결과

                    if(result.code.equals("400")) {
                        Toast.makeText(this@ClothActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    } else if(result.code.equals("200")) {
                        et_cloth_description.visibility = View.INVISIBLE // 옷 설명 입력창 안보이게
                        tv_cloth_description.visibility = View.VISIBLE // 옷 설명 보이게

                        update_cloth = false
                        invalidateOptionsMenu() // 메뉴 갱신
                        tv_cloth_description.text = description // 옷 설명 갱신
                    }
                }

            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<clothResponse>, t: Throwable) {
                Toast.makeText(this@ClothActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}