package com.example.soosoocloset.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.R
import com.example.soosoocloset.domain.Codi
import com.bumptech.glide.Glide

// author: Sumin, created: 21.05.19
class CodiAdapter(val context: Context, val codiList: ArrayList<Codi>) : RecyclerView.Adapter<CodiViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodiViewHolder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.codi_item, parent, false)
        return CodiViewHolder(view)
    }

    override fun onBindViewHolder(holder: CodiViewHolder, position: Int) {
        Glide.with(context).load(codiList[position].image).into(holder.codi)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        // 리스트 개수 리턴
        return codiList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

class CodiViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var codi = view.findViewById<ImageView>(R.id.codi_item_codi)
}
