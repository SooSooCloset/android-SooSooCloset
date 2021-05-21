package com.example.soosoocloset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*

class LikeAdapter (val likeList: ArrayList<Like>) : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeAdapter.LikeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_like, parent, false)
        return LikeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return likeList.size
    }

    override fun onBindViewHolder(holder: LikeAdapter.LikeViewHolder, position: Int) {
        holder.like_img.setImageResource(likeList.get(position).like_img)
        holder.nickname.text = likeList.get(position).nickname
        holder.likes_num.text = likeList.get(position).likes_num.toString()
    }

    class LikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val like_img = itemView.findViewById<ImageView>(R.id.image_view_like)
        val nickname = itemView.findViewById<TextView>(R.id.like_nickname)
        val likes_num = itemView.findViewById<TextView>(R.id.like_likes_num)
    }
}