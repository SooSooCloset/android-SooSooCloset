package com.example.soosoocloset.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.activity.CodiActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.adapter.HomeAdapter
import com.example.soosoocloset.domain.Home
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.homeResponse
import com.example.soosoocloset.domain.Codi
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// author: Sumin, created: 21.05.19, last modified : 21.07.13
class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var homeList = arrayListOf<Home>()
        val rv_home : RecyclerView = view.findViewById(R.id.rv_home)
        val homeAdapter = HomeAdapter(context!!, homeList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_home.layoutManager = layoutManager
        rv_home.adapter = homeAdapter

        //홈화면 서버와 통신
        RetrofitClient.api.homeRequest().enqueue(object : Callback<homeResponse> {
            override fun onFailure(call: Call<homeResponse>, t: Throwable) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<homeResponse>, response: Response<homeResponse>) {
                var result: homeResponse = response.body()!! // 응답 결과
                if(response.isSuccessful) {
                    if(result.code.equals("400")) { // 에러 발생 시
                        Toast.makeText(context ,"Error", Toast.LENGTH_SHORT).show()
                    } else if(result.code.equals("200")) { // 홈화면 코디들 조회 성공
                        var imageList = getImg(result.codi) //서버에서 받아온 이미지들을 Bitmap으로 변환하여 리스트에 저장
                        homeList.clear() //초기화
                        for(i in result.codi.indices) {
                            //서버에서 데이터 받아오기
                            var nickname = (result.codi[i])["user_id"].toString() //사용자 닉네임
                            var likes = (result.codi[i])["likes"].toString() //좋아요 수

                            homeList.add(Home(imageList[i], nickname, likes)) //변환된 Bitmap 이미지들을 리사이클러뷰 코디 아이템에 저장
                        }
                        homeAdapter.notifyDataSetChanged()
                    }
                }
            }
        })


        // 홈 화면의 코디리스트 아이템 클릭시
        homeAdapter.setItemClickListener(object : HomeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                startActivity(Intent(context, CodiActivity::class.java))

            }
        })
        return view
    }

    //이미지를 가져오는 메서드
    private fun getImg(input: List<Map<*, *>>): ArrayList<Bitmap>{
        var output = arrayListOf<Bitmap>()
        for(i in input.indices) {
            var data = (input[i])["codi_img"] as LinkedTreeMap<*, *>
            var array = data["data"] as ArrayList<*>
            var bitmap: Bitmap = convertBitmap(array as ArrayList<Double>)
            output.add(bitmap)
        }
        return output
    }

    //ByteArray 생성 메서드
    fun exByte(list: ArrayList<Double>): ByteArray {
        var list2: MutableList<Byte> = mutableListOf()
        for (i in 0..list.size - 1) {
            list2.add(list[i].toInt().toByte())
        }
        var arr = list2.toByteArray()
        return arr
    }

    //ByteArray를 Bitmap으로 변환하는 메서드
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