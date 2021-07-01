package com.example.soosoocloset

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.CodiAdapter
import com.example.soosoocloset.adapter.HomeAdapter
import com.example.soosoocloset.adapter.LikeAdapter
import com.example.soosoocloset.domain.Home
import com.example.soosoocloset.domain.Like

// author: Sumin, created: 21.05.19
class LikeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var likeList = arrayListOf<Like>(Like(), Like(), Like())
        val rv_like : RecyclerView = view.findViewById(R.id.rv_like)
        val likeAdapater = LikeAdapter(likeList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_like.adapter = likeAdapater
        rv_like.layoutManager = layoutManager

        //화면전환
        likeAdapater.setItemClickListener(object : LikeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                startActivity(Intent(context, CodiActivity::class.java))
            }
        })

        return view
    }

}