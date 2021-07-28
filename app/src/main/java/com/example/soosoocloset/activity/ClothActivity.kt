package com.example.soosoocloset.activity

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.addclothResponse
import kotlinx.android.synthetic.main.activity_add_closet.*
import kotlinx.android.synthetic.main.activity_cloth.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClothActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

        // closetFragment 로부터 받은 데이터 가져오기
        val cloth_id = intent.getDoubleExtra("cloth_id", 0.0)
        val cloth_img = intent.getParcelableExtra<Uri>("cloth_img")
        val description = intent.getStringExtra("description")

        // 옷 이미지와 설명 세팅
        Glide.with(applicationContext).load(cloth_img).into(iv_cloth)
        tv_cloth_description.setText(description)
    }

    // 상단바와 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cloth_menu, menu)
        return true
    }

    // 상단바의 메뉴 클릭시 호출되는 메소드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_delete_cloth -> {
                return true
            }
            R.id.item_update_cloth -> {
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}