package com.example.soosoocloset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.domain.Like
import com.example.soosoocloset.R

class LikeAdapter (val likeList: ArrayList<Like>) : RecyclerView.Adapter<Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 데이터 삽입
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return likeList.size
    }
}

class LikeHolder(view : View) : RecyclerView.ViewHolder(view) {
    var codi = view.findViewById<ImageView>(R.id.like_item_codi)
}