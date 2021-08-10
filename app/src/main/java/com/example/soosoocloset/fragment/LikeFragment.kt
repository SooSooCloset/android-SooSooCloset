package com.example.soosoocloset.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soosoocloset.activity.CodiActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.ClothActivity
import com.example.soosoocloset.adapter.LikeAdapter
import com.example.soosoocloset.data.changeProfileResponse
import com.example.soosoocloset.data.getLikecodiResponse
import com.example.soosoocloset.data.getclothResponse
import com.example.soosoocloset.data.likeResponse
import com.example.soosoocloset.domain.Cloth
import com.example.soosoocloset.domain.Like
import com.google.gson.internal.LinkedTreeMap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

// 설명: 메인 화면 하단바의 좋아요 클릭 -> 좋아요 화면
// author: Sumin, created: 21.05.19
// author: Soohyun, last modified: 21.07.31
class LikeFragment : Fragment() {
    var likeList = arrayListOf<Like>() // 좋아요한 코디 객체 리스트

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        val rv_like : RecyclerView = view.findViewById(R.id.rv_like)
        val likeAdapater = LikeAdapter(context!!, likeList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_like.adapter = likeAdapater
        rv_like.layoutManager = layoutManager

        val prefs : SharedPreferences = view.context.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        val user_id = prefs.getString("id", null)!! // 사용자 아이디

        // 좋아요한 코디 가져오기 서버와 네트워크 통신하는 부분
        RetrofitClient.api.getLikecodiRequest(user_id).enqueue(object : Callback<getLikecodiResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<getLikecodiResponse>, response: Response<getLikecodiResponse>) {
                if(response.isSuccessful) {
                    var result: getLikecodiResponse = response.body()!! // 응답 결과

                    if(result.code.equals("400")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 좋아요한 코디 조회 성공시

                        var imageList = getImg(result.like) // 서버로부터 받은 데이터를 비트맵으로 변환해 리스트로 저장

                        likeList.clear() // 좋아요 리스트 초기화

                        // 좋아요한 코디 관련 리스트에 값을 채우는 부분
                        for(i in imageList.indices) {
                            var obj = result.like[i]
                            if(obj["description"] == null) {
                                likeList.add(Like(obj["codi_id"] as Double, imageList[i], obj["nickname"] as String, obj["likes"] as Double, obj["codi_date"] as String, ""))
                            } else {
                                likeList.add(Like(obj["codi_id"] as Double, imageList[i], obj["nickname"] as String, obj["likes"] as Double, obj["codi_date"] as String, obj["codi_description"] as String))
                            }
                        }

                        likeAdapater.notifyDataSetChanged() // 리사이클러뷰 갱신
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<getLikecodiResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })

        // 좋아요한 코디 아이템 클릭시
        likeAdapater.setItemClickListener(object : LikeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 비트맵 이미지를 Uri로 변환
                val stream = ByteArrayOutputStream();
                likeList[position].image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                val path: String = MediaStore.Images.Media.insertImage(context!!.getContentResolver(), likeList[position].image, "IMG_" + Calendar.getInstance().getTime(), null)
                val uri: Uri = Uri.parse(path);

                val intent = Intent(context, CodiActivity::class.java)
                //intent.putExtra("codi_id",likeList[position].codi_id) // 코디 아이디 값 넘기기
                intent.putExtra("codi_img", uri) // 이미지 Uri 값 넘기기
                intent.putExtra("nickname", likeList[position].nickname) // 닉네임 값 넘기기
                intent.putExtra("likes", likeList[position].likeCount) // 좋아요 수 값 넘기기
                intent.putExtra("codi_date", likeList[position].date) // 코디 작성 날짜 값 넘기기
                intent.putExtra("codi_description",likeList[position].description) // 코디 설명 값 넘기기
                startActivity(intent) // 액티비티 실행
            }
        })

        return view
    }

    // 이미지를 가져오는 메서드
    private fun getImg(input: List<Map<*, *>>): ArrayList<Bitmap> {
        var output = arrayListOf<Bitmap>()

        for(i in input.indices) {
            var data = (input[i])["codi_img"] as LinkedTreeMap<*, *>
            var array = data["data"] as ArrayList<Double>
            var bitmap: Bitmap = convertBitmap(array)

            output.add(bitmap)
        }

        return output
    }

    fun exByte(list: ArrayList<Double>): ByteArray {
        var list2: MutableList<Byte> = mutableListOf()
        for (i in 0..list.size - 1) {
            list2.add(list[i].toInt().toByte())
        }
        var arr = list2.toByteArray()
        return arr
    }

    // 이미지를 비트맵으로 변환하는 메서드
    fun convertBitmap(input: ArrayList<Double>): Bitmap {
        var arr = exByte(input)

        try {
            var bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.size)
            return bitmap
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}