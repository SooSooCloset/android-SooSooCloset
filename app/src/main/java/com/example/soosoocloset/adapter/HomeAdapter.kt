package com.example.soosoocloset.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soosoocloset.domain.Home
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.likeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        holder.likes.text = homeList[position].likes.toInt().toString()
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        if(homeList[position].isChecked == "true")
            holder.cb_like.isChecked = true
        else if(homeList[position].isChecked == "false")
            holder.cb_like.isChecked = false

        val prefs : SharedPreferences = context!!.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장 장소
        val user_id = prefs.getString("id", null)!! // 사용자 아이디

        //좋아요 체크박스 이벤트 처리
        holder.cb_like.setOnClickListener {
            if(holder.cb_like.isChecked) { //좋아요 체크박스 채우는 이벤트 발생 시
                //좋아요 추가 서버와 통신
                RetrofitClient.api.addLikeRequest(user_id, homeList[position].codi_id.toInt(), (homeList[position].likes+1).toInt()).enqueue(object : Callback<likeResponse> {
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful){
                            val result: likeResponse = response.body()!! // 응답 결과
                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) {
                                homeList[position].likes += 1
                                holder.likes.text = homeList[position].likes.toInt().toString()
                                Toast.makeText(context, "addLike Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
            else { //좋아요 체크박스 비우는 이벤트 발생 시
                //좋아요 삭제 서버와 통신
                RetrofitClient.api.deleteLikeRequest(user_id, homeList[position].codi_id.toInt(), (homeList[position].likes-1).toInt()).enqueue(object : Callback<likeResponse> {
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful){
                            var result: likeResponse = response.body()!! // 응답 결과
                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) { // 통신 성공 시
                                homeList[position].likes -= 1
                                holder.likes.text = homeList[position].likes.toInt().toString()
                                Toast.makeText(context, "addLike Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
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
    var likes = view.findViewById<TextView>(R.id.home_item_likes)
    var cb_like = view.findViewById<CheckBox>(R.id.home_item_cb_like)
}