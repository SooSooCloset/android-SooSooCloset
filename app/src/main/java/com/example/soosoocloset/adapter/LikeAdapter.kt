package com.example.soosoocloset.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soosoocloset.domain.Like
import com.example.soosoocloset.R
import org.w3c.dom.Text

// author: Sumin, created: 21.05.19
class LikeAdapter (val context: Context, val likeList: ArrayList<Like>) : RecyclerView.Adapter<LikeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_item, parent, false)
        return LikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        holder.nickname.text = likeList[position].nickname
        Glide.with(context).load(likeList[position].image).into(holder.codi)
        holder.likeCount.text = likeList[position].likeCount.toInt().toString()

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return likeList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

class LikeViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var codi = view.findViewById<ImageView>(R.id.like_item_codi)
    var nickname = view.findViewById<TextView>(R.id.like_item_nickname)
    var likeCount = view.findViewById<TextView>(R.id.like_item_likeCount)
}