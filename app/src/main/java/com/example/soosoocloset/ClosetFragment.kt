package com.example.soosoocloset

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.domain.Cloth

// 설명: 메인 화면 하단바의 옷장 클릭 -> 옷장 화면
// author: Soohyun, created: 21.05.22
class ClosetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar) // 상단바
        // 상단바 메뉴 클릭시
        toolbar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.item_addCloth -> {
                    startActivity(Intent(context, AddClosetActivity::class.java)) // 옷 등록 화면으로 이동
                    true
                }
                else -> false
            }
        }

        var clothList = arrayListOf<Cloth>(Cloth(), Cloth(), Cloth(), Cloth(), Cloth()) // 테스트용 데이터
        val rv_closet : RecyclerView = view.findViewById(R.id.rv_closet) // 옷 리사이클러뷰
        val clothAdpater = ClothAdapter(clothList) // 옷 리사이클러뷰의 어댑터
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

        rv_closet.adapter = clothAdpater // 리사이클러뷰와 어댑터 연결
        rv_closet.layoutManager = layoutManager // 리사이클러뷰와 레이아웃 매니저 연결

        return view
    }

}