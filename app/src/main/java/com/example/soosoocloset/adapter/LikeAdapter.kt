package com.example.soosoocloset.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soosoocloset.domain.Like
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.likeResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// author: Sumin, created: 21.05.19
class LikeAdapter (val context: Context, var likeList: ArrayList<Like>) : RecyclerView.Adapter<LikeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        // 레이아웃 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_item, parent, false)
        return LikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        holder.nickname.text = likeList[position].nickname
        Glide.with(context).load(likeList[position].image).into(holder.codi)
        holder.btn_like.isChecked = true
        holder.likeCount.text = likeList[position].likeCount.toInt().toString()

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }


        holder.btn_like.setOnClickListener {
            val prefs : SharedPreferences = context!!.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
            val user_id = prefs.getString("id", null)!! // 사용자 아이디

            if(holder.btn_like.isChecked) { // 좋아요가 눌러진 경우
                // 좋아요 추가 서버와 네트워크 통신하는 부분
                RetrofitClient.api.addLikeRequest(user_id, likeList[position].codi_id.toInt(), (likeList[position].likeCount + 1).toInt()).enqueue(object : Callback<likeResponse> {
                    // 네트워크 통신 성공한 경우
                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful) {
                            var result: likeResponse = response.body()!! // 응답 결과

                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            } else if(result.code.equals("200")) { // 회원탈퇴 성공시
                                likeList[position].likeCount += 1 // 좋아요 수 변경
                                holder.likeCount.text = likeList[position].likeCount.toInt().toString() // 좋아요 수 텍스트 변경
                            }
                        }
                    }

                    // 네트워크 통신 실패한 경우
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            } else { // 좋아요가 눌러지지 않은 경우
                // 좋아요 삭제 서버와 네트워크 통신하는 부분
                RetrofitClient.api.deleteLikeRequest(user_id, likeList[position].codi_id.toInt(), (likeList[position].likeCount - 1).toInt()).enqueue(object : Callback<likeResponse> {
                    // 네트워크 통신 성공한 경우
                    override fun onResponse(call: Call<likeResponse>, response: Response<likeResponse>) {
                        if(response.isSuccessful) {
                            var result: likeResponse = response.body()!! // 응답 결과

                            if(result.code.equals("400")) { // 에러 발생 시
                                Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            }  else if(result.code.equals("200")) { // 회원탈퇴 성공시
                                likeList[position].likeCount -= 1 // 좋아요 수 변경
                                holder.likeCount.text = likeList[position].likeCount.toInt().toString() // 좋아요 수 텍스트 변경
                            }
                        }
                    }

                    // 네트워크 통신 실패한 경우
                    override fun onFailure(call: Call<likeResponse>, t: Throwable) {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
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
    var btn_like = view.findViewById<CheckBox>(R.id.like_item_btn_like)
    var likeCount = view.findViewById<TextView>(R.id.like_item_likeCount)
}