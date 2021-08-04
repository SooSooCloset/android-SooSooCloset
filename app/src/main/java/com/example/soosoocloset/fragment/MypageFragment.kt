package com.example.soosoocloset.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.soosoocloset.activity.MyinfoActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.LoginActivity
import com.example.soosoocloset.data.deleteUserResponse
import com.example.soosoocloset.data.getclothResponse
import com.example.soosoocloset.data.mypageResponse
import com.example.soosoocloset.domain.Cloth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 설명: 메인 화면 하단바의 마이페이지 클릭 -> 마이페이지 화면
// author: Soohyun, created: 21.07.30
class MypageFragment : Fragment() {
    lateinit var prefs: SharedPreferences
    lateinit var user_id: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_mypage, container, false)

        val lv_mypage : ListView = view.findViewById(R.id.lv_mypage) // 마이페이지의 리스트뷰
        val list = arrayOf("내 정보 수정", "로그아웃", "회원탈퇴") // 마이페이지의 리스트뷰에 들어갈 내용
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, list) // 리스트뷰의 어댑터
        lv_mypage.adapter = adapter // 리스트뷰와 어댑터 연결

        prefs = view.context.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        user_id = prefs.getString("id", null)!! // 사용자 아이디
        var nickname = ""

        // 마이페이지 서버와 네트워크 통신하는 부분
        RetrofitClient.api.mypageRequest(user_id).enqueue(object : Callback<mypageResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<mypageResponse>, response: Response<mypageResponse>) {
                if(response.isSuccessful) {
                    var result: mypageResponse = response.body()!! // 응답 결과

                    if(result.code.equals("404")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 사용자 정보 조회 성공시
                        nickname = result.nickname
                        var profile = result.profile

                        tv_nickname.setText(nickname)

                        if(profile == null) {
                            iv_profile.setImageResource(R.drawable.user)
                        } 
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<mypageResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })

        // 마이페이지 리스트뷰의 클릭 리스너
        lv_mypage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position) {
                0 -> { // 내 정보 수정
                    val intent = Intent(context, MyinfoActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    startActivity(intent) // 내 정보 수정 화면으로 이동
                }
                1 -> { // 로그아웃
                    // 자동 로그인 정보 삭제
                    val editor : SharedPreferences.Editor = prefs.edit()
                    editor.remove("id")
                    editor.commit()

                    startActivity(Intent(context, LoginActivity::class.java)) // 로그인 화면을 이동
                    activity?.finish() // 현재 액티비티 종료
                }
                2 -> { // 회원 탈퇴
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(context) // 경고 팝업창
                    dialog.setTitle("정말 회원 탈퇴 하시겠습니까?") //제목
                    dialog.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                        deleteUser()
                    })
                    dialog.setNegativeButton("아니요", null)
                    dialog.show()
                }
            }
        }

        return view
    }

    // 회원 탈퇴 메서드
    fun deleteUser() {
        // 회원탈퇴 서버와 네트워크 통신하는 부분
        RetrofitClient.api.deleteUserRequest(user_id).enqueue(object : Callback<deleteUserResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<deleteUserResponse>, response: Response<deleteUserResponse>) {
                if(response.isSuccessful) {
                    var result: deleteUserResponse = response.body()!! // 응답 결과

                    if(result.code.equals("404")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 회원탈퇴 성공시
                        Toast.makeText(context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(context, LoginActivity::class.java)) // 로그인 화면을 이동
                        activity?.finish() // 현재 액티비티 종료
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<deleteUserResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}