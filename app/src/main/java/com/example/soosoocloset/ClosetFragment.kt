package com.example.soosoocloset

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.domain.Cloth

// 설명: 메인 화면 하단바의 옷장 클릭 -> 옷장 화면
// author: Soohyun, created: 21.05.08
class ClosetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var clothList = arrayListOf<Cloth>(Cloth(), Cloth(), Cloth(), Cloth(), Cloth())
        val rv_closet : RecyclerView = view.findViewById(R.id.rv_closet)
        val clothAdpater = ClothAdapter(clothList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 3)

        rv_closet.adapter = clothAdpater
        rv_closet.layoutManager = layoutManager

        return view
    }

    // 상단바의 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.closet_menu, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

}