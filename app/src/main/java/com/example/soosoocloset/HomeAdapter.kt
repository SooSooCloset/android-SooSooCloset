package com.example.soosoocloset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter (val homeList: ArrayList<Home>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_like, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        holder.home_img.setImageResource(homeList.get(position).home_img)
        holder.nickname.text = homeList.get(position).nickname
        holder.likes_num.text = homeList.get(position).likes_num.toString()
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val home_img = itemView.findViewById<ImageView>(R.id.image_view_home)
        val nickname = itemView.findViewById<TextView>(R.id.home_nickname)
        val likes_num = itemView.findViewById<TextView>(R.id.home_likes_num)
    }
}