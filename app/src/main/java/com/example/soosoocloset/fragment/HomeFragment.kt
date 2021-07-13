package com.example.soosoocloset.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.activity.CodiActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.adapter.HomeAdapter
import com.example.soosoocloset.domain.Home

// author: Sumin, created: 21.05.19
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var homeList = arrayListOf<Home>(Home("nickname1", "100"), Home("nickname2", "200"), Home("nickname3", "300"))
        val rv_home : RecyclerView = view.findViewById(R.id.rv_home)
        val homeAdapter = HomeAdapter(homeList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_home.layoutManager = layoutManager
        rv_home.adapter = homeAdapter

        // 홈 화면의 코디리스트 아이템 클릭시
        homeAdapter.setItemClickListener(object : HomeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                startActivity(Intent(context, CodiActivity::class.java))
            }
        })

        return view
    }

}