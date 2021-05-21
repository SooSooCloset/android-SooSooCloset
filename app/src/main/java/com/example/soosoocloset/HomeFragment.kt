package com.example.soosoocloset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.CodiAdapter
import com.example.soosoocloset.adapter.HomeAdapter
import com.example.soosoocloset.domain.Codi
import com.example.soosoocloset.domain.Home
import kotlinx.android.synthetic.main.fragment_closet.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var homeList = arrayListOf<Home>(Home(), Home(), Home())
        val rv_home : RecyclerView = view.findViewById(R.id.rv_home)
        val homeAdapater = HomeAdapter(homeList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_home.adapter = homeAdapater
        rv_home.layoutManager = layoutManager

        return view
    }

}