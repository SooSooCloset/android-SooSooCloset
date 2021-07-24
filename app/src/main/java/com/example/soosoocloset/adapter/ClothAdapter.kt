package com.example.soosoocloset.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.R
import com.example.soosoocloset.domain.Cloth

// 설명: 옷장 화면의 옷 리스트의 어댑터
// author: Soohyun, created: 21.06.13
class ClothAdapter(val context: Context, val clothList : ArrayList<Cloth>) : RecyclerView.Adapter<ClothViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothViewHolder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.closet_item, parent, false)
        return ClothViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothViewHolder, position: Int) {
        //val resourceId = context.resources.getIdentifier(clothList[position].image, "drawable", context.packageName)
        //holder.cloth.setImageResource(resourceId)
        holder.cloth.setImageBitmap(clothList[position].image)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return clothList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

// 옷 아이템 뷰홀더
class ClothViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var cloth = view.findViewById<ImageView>(R.id.closet_item_cloth)
}