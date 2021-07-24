package com.example.soosoocloset.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.activity.ClothActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.AddClosetActivity
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.data.getclothResponse
import com.example.soosoocloset.domain.Cloth
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.fragment_closet.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 메인 화면 하단바의 옷장 클릭 -> 옷장 화면
// author: Soohyun, created: 21.07.24
class ClosetFragment : Fragment() {
    var outerList = arrayListOf<Bitmap>() // 아우터 이미지 리스트
    var topList = arrayListOf<Bitmap>() // 상의 이미지 리스트
    var bottomList = arrayListOf<Bitmap>() // 하의 이미지 리스트
    var onepieceList = arrayListOf<Bitmap>() // 원피스 이미지 리스트
    var shoesList = arrayListOf<Bitmap>() // 신발 이미지 리스트
    var accessaryList = arrayListOf<Bitmap>() // 악세서리 이미지 리스트

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar) // 상단바
        // 상단바 메뉴 클릭시
        toolbar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.item_addCloth -> {
                    startActivity(Intent(context, AddClosetActivity::class.java)) // 옷 등록 화면으로 이동
                    true
                }
                else -> false
            }
        }

        val prefs : SharedPreferences = view.context.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        val user_id = prefs.getString("id", null)!! // 사용자 아이디

        var clothList = arrayListOf<Cloth>() // 옷 리스트
        val rv_closet : RecyclerView = view.findViewById(R.id.rv_closet) // 옷 리사이클러뷰
        val clothAdapter = ClothAdapter(context!!, clothList) // 옷 리사이클러뷰의 어댑터
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

        rv_closet.adapter = clothAdapter // 리사이클러뷰와 어댑터 연결
        rv_closet.layoutManager = layoutManager // 리사이클러뷰와 레이아웃 매니저 연결

        // 옷 가져오기 서버와 네트워크 통신하는 부분
        RetrofitClient.api.getclothRequest(user_id).enqueue(object : Callback<getclothResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<getclothResponse>, response: Response<getclothResponse>) {
                if(response.isSuccessful) {
                    var result: getclothResponse = response.body()!! // 응답 결과

                    if(result.code.equals("404")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 옷 가져오기 조회 성공시

                        // 서버로부터 받은 데이터를 비트맵으로 변환해 리스트로 저장
                        outerList = getImg(result.outer)
                        topList = getImg(result.top)
                        bottomList = getImg(result.bottom)
                        onepieceList = getImg(result.onepiece)
                        shoesList = getImg(result.shoes)
                        accessaryList = getImg(result.accessary)

                        // 프래그먼트의 첫 화면 리스트 초기화하는 부분
                        clothList.clear()
                        for(i in outerList.indices) {
                            clothList.add(Cloth(outerList[i]))
                        }
                        clothAdapter.notifyDataSetChanged()
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<getclothResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                System.out.println(t)
            }
        })

        // 옷 리스트의 아이템 클릭시
        clothAdapter.setItemClickListener(object : ClothAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                startActivity(Intent(context, ClothActivity::class.java))
            }
        })

        // 아우터 버튼 클릭시
        view.btn_outer.setOnClickListener{
            clothList.clear()
            for(i in outerList.indices) {
                clothList.add(Cloth(outerList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        // 상의 버튼 클릭시
        view.btn_top.setOnClickListener {
            clothList.clear()
            for(i in topList.indices) {
                clothList.add(Cloth(topList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        // 하의 버튼 클릭시
        view.btn_bottom.setOnClickListener{
            clothList.clear()
            for(i in bottomList.indices) {
                clothList.add(Cloth(bottomList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        // 원피스 클릭시
        view.btn_onepiece.setOnClickListener{
            clothList.clear()
            for(i in onepieceList.indices) {
                clothList.add(Cloth(onepieceList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        // 신발 버튼 클릭시
        view.btn_shoes.setOnClickListener{
            clothList.clear()
            for(i in shoesList.indices) {
                clothList.add(Cloth(shoesList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        // 악세서리 버튼 클릭시
        view.btn_accessary.setOnClickListener{
            clothList.clear()
            for(i in accessaryList.indices) {
                clothList.add(Cloth(accessaryList[i]))
            }
            clothAdapter.notifyDataSetChanged()
        }

        return view
    }

    // 이미지를 가져오는 메서드
    private fun getImg(input: List<Map<*, *>>): ArrayList<Bitmap> {
        var output = arrayListOf<Bitmap>()

        for(i in input.indices) {
            var data = (input[i])["cloth_img"] as LinkedTreeMap<*, *>
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