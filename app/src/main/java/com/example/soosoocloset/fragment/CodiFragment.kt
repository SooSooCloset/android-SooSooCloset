package com.example.soosoocloset.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.activity.MycodiActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.AddCodiActivity
import com.example.soosoocloset.activity.LoginActivity
import com.example.soosoocloset.adapter.CodiAdapter
import com.example.soosoocloset.domain.Codi
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// author: Sumin, created: 21.05.19
class CodiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_codi, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var codiList = arrayListOf<Codi>(Codi(), Codi(), Codi())
        val rv_codi : RecyclerView = view.findViewById(R.id.rv_codi)
        val codiAdapater = CodiAdapter(codiList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_codi.adapter = codiAdapater
        rv_codi.layoutManager = layoutManager

        //코디화면 서버와 통신
        /*var id = et_id.text.toString()
        RetrofitClient.api.codiRequest(id).enqueue(object : Callback<codiResponse> {
            override fun onFailure(call: Call<codiResponse>, t: Throwable) {
                //Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<codiResponse>, response: Response<codiResponse>) {
                var result: codiResponse = response.body()!! // 응답 결과
                if(result.code.equals("404")) { // 에러 발생 시
                    //Toast.makeText(context ,"Error", Toast.LENGTH_SHORT).show()
                } else if(result.code.equals("200")) { // 코디화면: 내코디 목록 조회 성공
                    //homeAdapter.notifyDataSetChanged()
                }
            }
        })*/

        //화면전환
        codiAdapater.setItemClickListener(object : CodiAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                startActivity(Intent(context, MycodiActivity::class.java))
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

}