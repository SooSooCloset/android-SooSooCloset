package com.example.soosoocloset.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.data.addcodiResponse
import com.example.soosoocloset.data.getclothResponse
import com.example.soosoocloset.domain.Cloth
import com.google.gson.internal.LinkedTreeMap
import com.outsbook.libs.canvaseditor.CanvasEditorView
import kotlinx.android.synthetic.main.activity_add_codi.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

//설명: 코디 만들기 화면
// author: Sumin
// author: Soohyun, created: 21.08.14
class AddCodiActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var canvasEditor: CanvasEditorView // 코디 편집 뷰
    lateinit var capture_target: View
    lateinit var selectImage: Bitmap // 선택된 이미지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        canvasEditor = findViewById(R.id.canvasEditor)
        canvasEditor.setPaintColor(0) // 브러쉬 색상 투명으로 설정
        capture_target = findViewById<View>(R.id.canvasEditor) // 캡쳐할 영역

        val toolbar: Toolbar = findViewById(R.id.toolbar) // 상단바
        setSupportActionBar(toolbar) // 상단바를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) // 액션바의 타이틀을 숨김

        // 카테고리 버튼과 클릭 리스너 연결
        btn_outer.setOnClickListener(this)
        btn_top.setOnClickListener(this)
        btn_bottom.setOnClickListener(this)
        btn_onepiece.setOnClickListener(this)
        btn_shoes.setOnClickListener(this)
        btn_accessary.setOnClickListener(this)
    }

    // 상단바와 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_codi_menu, menu)
        return true
    }

    // 상단바의 메뉴 클릭시 호출되는 메소드
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_finishCodi -> {
                //캡쳐
                capture_target.buildDrawingCache();
                val captureView : Bitmap = capture_target.getDrawingCache();

                //저장
                try {
                    val fos = FileOutputStream("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/codi_img.png") //캡쳐 이미지 저장 경로
                    captureView.compress(Bitmap.CompressFormat.PNG, 100, fos) //캡쳐 이미지 Bitmap에서 png형식으로 변환

                    //캡쳐 이미지를 코디 이미지 파일로 변환
                    val file = File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/codi_img.png")
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val codi_img = MultipartBody.Part.createFormData("codi_img", file.name, requestFile)

                    val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) //자동로그인 정보 저장 장소
                    val user_id = RequestBody.create(MediaType.parse("text/plain"), prefs.getString("id", null)!!) //사용자 아이디
                    val codi_description = RequestBody.create(MediaType.parse("text/plain"), et_codi_description.text.toString().trim()) //코디 설명
                    val codi_date = RequestBody.create(MediaType.parse("text/plain"), LocalDate.now().toString()) //코디 생성 날짜

                    //코디 추가 서버와 통신
                    RetrofitClient.api.addcodiRequest(user_id, codi_img, codi_description, codi_date).enqueue(object : Callback<addcodiResponse> {
                        override fun onFailure(call: Call<addcodiResponse>, t: Throwable) {
                            Toast.makeText(this@AddCodiActivity, "Network error", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<addcodiResponse>, response: Response<addcodiResponse>) {
                            var result: addcodiResponse = response.body()!! // 응답 결과
                            if(response.isSuccessful) {
                                if(result.code.equals("400")) { // 에러 발생 시
                                    Toast.makeText(this@AddCodiActivity ,"Error", Toast.LENGTH_SHORT).show()
                                } else if(result.code.equals("200")) { //코디 추가 성공 시
                                    Toast.makeText(this@AddCodiActivity ,"Addcodi Success", Toast.LENGTH_SHORT).show()
                                    finish() //activity 종료
                                }
                            }
                        }
                    })
                } catch (e : Exception) {
                    e.printStackTrace();
                }

                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    // 클릭 이벤트 처리 메소드
    override fun onClick(v: View?) {
        val prefs : SharedPreferences = this.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        val user_id = prefs.getString("id", null)!! // 사용자 아이디

        when(v?.id) {
            R.id.btn_outer -> {
                getCloth(user_id, "outer")
            }
            R.id.btn_top -> {
                getCloth(user_id, "top")
            }
            R.id.btn_bottom -> {
                getCloth(user_id, "bottom")
            }
            R.id.btn_onepiece -> {
                getCloth(user_id, "onepiece")
            }
            R.id.btn_shoes -> {
                getCloth(user_id, "shoes")
            }
            R.id.btn_accessary -> {
                getCloth(user_id, "accessary")
            }

        }
    }

    // 서버에서 옷 가져와 팝업창으로 띄우는 메서드
    private fun getCloth(user_id: String, category: String){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // 뷰를 팝업창으로 띄울 수 있게 해주는 역할
        val view = inflater.inflate(R.layout.activity_add_codi_popup, null) // 팝업창을 띄울 뷰

        var clothList = arrayListOf<Cloth>() // 옷 리스트
        val rv_cloth: RecyclerView = view.findViewById(R.id.rv_cloth) // 팝업창의 옷 리사이클러뷰
        val clothAdapter = ClothAdapter(this, clothList) // 팝업창의 옷 리사이클러뷰의 어댑터
        val layoutManager: GridLayoutManager = GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

        rv_cloth.adapter = clothAdapter // 리사이클러뷰와 어댑터 연결
        rv_cloth.layoutManager = layoutManager // 리사이클러뷰와 레이아웃 매니저 연결

        // 옷 가져오기 서버와 네트워크 통신하는 부분
        RetrofitClient.api.getclothRequest(user_id, category).enqueue(object : Callback<getclothResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<getclothResponse>, response: Response<getclothResponse>) {
                if(response.isSuccessful) {
                    var result: getclothResponse = response.body()!! // 응답 결과

                    if(result.code.equals("400")) { // 에러 발생 시
                        Toast.makeText(this@AddCodiActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 옷 가져오기 조회 성공시

                        var imageList = getImg(result.cloth) // 서버로부터 받은 데이터를 비트맵으로 변환해 리스트로 저장

                        clothList.clear() // 옷 이미지 리스트 초기화
                        // 옷 관련 리스트에 값을 채우는 부분
                        for(i in imageList.indices) {
                            var obj = result.cloth[i]
                            if(result.cloth[i]["description"] == null) {
                                clothList.add(Cloth(obj["cloth_id"] as Double, imageList[i], ""))
                            } else {
                                clothList.add(Cloth(obj["cloth_id"] as Double, imageList[i], obj["description"] as String))
                            }
                        }

                        clothAdapter.notifyDataSetChanged() // 리사이클러뷰 갱신
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<getclothResponse>, t: Throwable) {
                Toast.makeText(this@AddCodiActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                System.out.println(t)
            }
        })

        // 옷 리스트의 아이템 클릭시
        clothAdapter.setItemClickListener(object : ClothAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                selectImage = clothList[position].image // 선택된 이미지 명 저장
            }
        })

        // 다이얼로그 생성
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("추가할 옷을 선택해주세요.") // 타이틀 설정
            .setPositiveButton("선택") { dialog, which -> // 오른쪽 버튼 설정
                val resources: Resources = this.resources
                val cloth = BitmapDrawable(resources, selectImage) // 선택된 이미지를 drawable로 변환
                cloth?.let {
                    canvasEditor.addDrawableSticker(cloth)
                }
            }
            .setNegativeButton("취소", null) // 왼쪽 버튼 설정 - 취소시 아무 것도 하지 않음
            .create()

        alertDialog.setView(view) // 다이얼로그에 뷰 배치

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FCCACA"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#696969"))
        }

        alertDialog.show() // 다이얼로그를 보여줌
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
