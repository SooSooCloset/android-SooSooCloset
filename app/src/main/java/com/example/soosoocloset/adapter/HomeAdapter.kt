package com.example.soosoocloset.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soosoocloset.domain.Home
import com.example.soosoocloset.R

// author: Sumin, created: 21.05.19
class HomeAdapter (val context: Context, val homeList: ArrayList<Home>) : RecyclerView.Adapter<HomeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.nickname.text = homeList[position].nickname
        Glide.with(context).load(homeList[position].image).into(holder.codi)
        holder.likeCount.text = homeList[position].likeCount

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return homeList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

class HomeViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var codi = view.findViewById<ImageView>(R.id.home_item_codi)
    var nickname = view.findViewById<TextView>(R.id.home_item_nickname)
    var likeCount = view.findViewById<TextView>(R.id.home_item_likeCount)
}