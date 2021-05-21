package com.example.soosoocloset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.R
import com.example.soosoocloset.domain.Cloth

// 설명: 옷장 화면의 옷 리스트의 어댑터
// author: Soohyun, created: 21.05.08
class ClothAdapter(val clothList : ArrayList<Cloth>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.closet_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 데이터 삽입
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return clothList.size
    }

}

// 옷 아이템 뷰홀더
class Holder(view : View) : RecyclerView.ViewHolder(view) {
    var cloth = view.findViewById<ImageView>(R.id.closet_item_cloth)
}