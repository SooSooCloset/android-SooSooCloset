package com.example.soosoocloset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CodiAdapter (val codiList: ArrayList<Codi>) : RecyclerView.Adapter<CodiAdapter.CodiViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodiAdapter.CodiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_codi, parent, false)
        return CodiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return codiList.size
    }

    override fun onBindViewHolder(holder: CodiAdapter.CodiViewHolder, position: Int) {
        holder.codi_img.setImageResource(codiList.get(position).codi_img)
    }

    class CodiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codi_img = itemView.findViewById<ImageView>(R.id.image_view_codi)
    }
}