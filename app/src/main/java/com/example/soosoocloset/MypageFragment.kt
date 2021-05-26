package com.example.soosoocloset

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter

// 설명: 메인 화면 하단바의 마이페이지 클릭 -> 마이페이지 화면
// author: Soohyun, created: 21.05.05
class MypageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_mypage, container, false)

        val lv_mypage : ListView = view.findViewById(R.id.lv_mypage) // 마이페이지의 리스트뷰
        val list = arrayOf("내 정보 수정", "로그아웃", "회원탈퇴") // 마이페이지의 리스트뷰에 들어갈 내용
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, list) // 리스트뷰의 어댑터

        lv_mypage.adapter = adapter // 리스트뷰와 어댑터 연결

        // 내 정보 수정 클릭시
        lv_mypage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            startActivity(Intent(context, MyinfoActivity::class.java))
        }

        return view
    }
}