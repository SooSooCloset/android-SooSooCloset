package com.example.soosoocloset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.domain.Home
import com.example.soosoocloset.R

class HomeAdapter (val homeList: ArrayList<Home>) : RecyclerView.Adapter<Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 데이터 삽입
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return homeList.size
    }
}

class HomeHolder(view : View) : RecyclerView.ViewHolder(view) {
    var cloth = view.findViewById<ImageView>(R.id.home_item_cloth)
}