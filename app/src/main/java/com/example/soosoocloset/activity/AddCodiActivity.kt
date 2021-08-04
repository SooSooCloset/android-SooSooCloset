package com.example.soosoocloset.activity

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.data.addcodiResponse
import com.example.soosoocloset.domain.Cloth
import com.outsbook.libs.canvaseditor.CanvasEditorView
import kotlinx.android.synthetic.main.activity_add_codi.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

//설명: 코디 만들기 화면
// author: Sumin
// author: Soohyun, created: 21.06.13
class AddCodiActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var canvasEditor: CanvasEditorView // 코디 편집 뷰
    lateinit var capture_target: View
    var selectImage: String = "" // 선택된 이미지 명

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
        when(v?.id) {
            R.id.btn_outer,
            R.id.btn_top,
            R.id.btn_bottom,
            R.id.btn_onepiece,
            R.id.btn_shoes,
            R.id.btn_accessary -> {
                /*
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // 뷰를 팝업창으로 띄울 수 있게 해주는 역할
                val view = inflater.inflate(R.layout.activity_add_codi_popup, null) // 팝업창을 띄울 뷰

                var clothList = arrayListOf<Cloth>(Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth")) // 테스트용 데이터
                val rv_cloth: RecyclerView = view.findViewById(R.id.rv_cloth) // 팝업창의 옷 리사이클러뷰
                val clothAdapter = ClothAdapter(this, clothList) // 팝업창의 옷 리사이클러뷰의 어댑터
                val layoutManager: GridLayoutManager =
                    GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

                rv_cloth.adapter = clothAdapter // 리사이클러뷰와 어댑터 연결
                rv_cloth.layoutManager = layoutManager // 리사이클러뷰와 레이아웃 매니저 연결

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
                        val resourceId = this.resources.getIdentifier(
                            selectImage,
                            "drawable",
                            this.packageName
                        ) // 선택된 이미지의 리소스 아이디를 가져옴
                        val cloth = ContextCompat.getDrawable(this, resourceId)
                        cloth?.let {
                            canvasEditor.addDrawableSticker(cloth)
                        }
                    }
                    .setNegativeButton("취소", null) // 왼쪽 버튼 설정 - 취소시 아무 것도 하지 않음
                    .create()

                alertDialog.setView(view) // 다이얼로그에 뷰 배치
                alertDialog.show() // 다이얼로그를 보여줌*/
            }

        }
    }
}
