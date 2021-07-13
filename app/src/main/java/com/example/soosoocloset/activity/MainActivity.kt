package com.example.soosoocloset.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.soosoocloset.R
import com.example.soosoocloset.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

// 설명: 메인 화면 - 하단바를 통해 여러 화면으로 이동
// author: Soohyun, created: 21.05.05
class MainActivity : AppCompatActivity() {
    val homeFragment : HomeFragment = HomeFragment() // 홈 화면 프래그먼트
    val closetFragment : ClosetFragment = ClosetFragment() // 옷장 화면 프래그먼트
    val codiFragment : CodiFragment = CodiFragment() // 코디 화면 프래그먼트
    val likeFragment : LikeFragment = LikeFragment() // 좋아요 화면 프래그먼트
    val mypageFragment : MypageFragment = MypageFragment() // 마이페이지 화면 프래그먼트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom_navi : BottomNavigationView = findViewById(R.id.bottom_navi) // 메인 화면의 하단바

        // 첫 화면은 홈 화면으로 보이게 설정
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction() // 화면 교체를 도와주는 트랜잭션 시작
        transaction.replace(R.id.main_frameLayout, homeFragment) // 트랜잭션 설정(홈 화면으로 교체)
        transaction.commit() // 트랜잭션 마무리

        // 하단바 선택시 수행할 작업을 정의한 부분
        bottom_navi.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // 홈 화면
                R.id.item_home -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction() // 화면 교체를 도와주는 트랜잭션 시작
                    transaction.replace(R.id.main_frameLayout, homeFragment) // 트랜잭션 설정(홈 화면으로 교체)
                    transaction.commit() // 트랜잭션 마무리
                    return@setOnNavigationItemSelectedListener true
                }
                // 옷장 화면
                R.id.item_closet -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frameLayout, closetFragment)
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                // 코디 화면
                R.id.item_codi -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frameLayout, codiFragment)
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                // 좋아요 화면
                R.id.item_like -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frameLayout, likeFragment)
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                // 마이페이지 화면
                R.id.item_mypage -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frameLayout, mypageFragment)
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
}