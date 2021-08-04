package com.example.soosoocloset.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.activity.MycodiActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.AddCodiActivity
import com.example.soosoocloset.adapter.CodiAdapter
import com.example.soosoocloset.data.getcodiResponse
import com.example.soosoocloset.domain.Codi
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

// author: Sumin, created: 21.05.19, last modified 21.07.26
class CodiFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_codi, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var codiList = arrayListOf<Codi>()
        val rv_codi : RecyclerView = view.findViewById(R.id.rv_codi)
        val codiAdapater = CodiAdapter(context!!, codiList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_codi.adapter = codiAdapater
        rv_codi.layoutManager = layoutManager

        //코디화면 서버와 통신
        val prefs : SharedPreferences = view.context.getSharedPreferences("User", Context.MODE_PRIVATE) //자동로그인 정보 저장 장소
        val user_id = prefs.getString("id", null)!! //사용자 아이디

        RetrofitClient.api.getcodiRequest(user_id).enqueue(object : Callback<getcodiResponse> {
            override fun onFailure(call: Call<getcodiResponse>, t: Throwable) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<getcodiResponse>, response: Response<getcodiResponse>) {
                if(response.isSuccessful) {
                    var result: getcodiResponse = response.body()!! // 응답 결과
                    if(result.code.equals("400")) { // 에러 발생 시
                        Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
                    } else if(result.code.equals("200")) { // 코디화면: 내코디 목록 조회 성공
                        var imageList = getImg(result.codi) //서버에서 받아온 이미지들을 비트맵으로 변환하여 리스트에 저장
                        codiList.clear() //초기화
                        for (i in result.codi.indices)
                            codiList.add(Codi((result.codi[i])["codi_id"] as Double, imageList[i],
                                (result.codi[i])["codi_description"] as String,
                                (result.codi[i])["likes"] as Double, (result.codi[i])["codi_date"] as String))
                        codiAdapater.notifyDataSetChanged() //리사이클러뷰 갱신
                    }
                }
            }
        })

        //화면전환
        codiAdapater.setItemClickListener(object : CodiAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val intent = Intent(context, MycodiActivity::class.java)

                //코디 이미지
                val stream = ByteArrayOutputStream();
                codiList[position].image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                val path: String = MediaStore.Images.Media.insertImage(context!!.getContentResolver(), codiList[position].image, "Title", null)
                val uri: Uri = Uri.parse(path);

                val codi_id = codiList[position].codi_id //코디 아이디
                val codi_description = codiList[position].codi_description //코디 설명
                val likes = codiList[position].likes //좋아요 수
                val codi_date = codiList[position].codi_date //코디 생성 날짜

                //MycodiActivity로 데이터 전달
                intent.apply {
                    this.putExtra("codi_id", codi_id)
                    this.putExtra("codi_img", uri)
                    this.putExtra("codi_description", codi_description)
                    this.putExtra("likes", likes)
                    this.putExtra("codi_date", codi_date)
                    startActivity(intent)
                }
            }
        })

        val toolbar: Toolbar = view.findViewById(R.id.toolbar) // 상단바
        // 상단바 메뉴 클릭시
        toolbar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.item_addCodi -> {
                    startActivity(Intent(context, AddCodiActivity::class.java)) // 코디 추가 화면으로 이동
                    true
                }
                else -> false
            }
        }

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
        for (i in list.indices) {
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