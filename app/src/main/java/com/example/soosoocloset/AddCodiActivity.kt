package com.example.soosoocloset

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.domain.Cloth
import com.outsbook.libs.canvaseditor.CanvasEditorView

//설명: 코디 만들기 화면
// author: Sumin
// author: Soohyun, created: 21.06.29

class AddCodiActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var canvasEditor: CanvasEditorView // 코디 편집 뷰
    var selectImage: String = "" // 선택된 이미지 명

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        canvasEditor = findViewById(R.id.canvasEditor)
        canvasEditor.setPaintColor(0) // 브러쉬 색상 투명으로 설정

        // 테스트 이미지
        val drawable = ContextCompat.getDrawable(this, R.drawable.codi_default)
        drawable?.let{
            canvasEditor.addDrawableSticker(drawable)
        }

        val btn_outer = findViewById<Button>(R.id.btn_outer) // 아우터 버튼
        val btn_top = findViewById<Button>(R.id.btn_top) // 상의 버튼
        val btn_bottom = findViewById<Button>(R.id.btn_bottom) // 하의 버튼
        val btn_onepiece = findViewById<Button>(R.id.btn_onepiece) // 원피스 버튼
        val btn_shoes = findViewById<Button>(R.id.btn_shoes) // 신발 버튼
        val btn_accessary = findViewById<Button>(R.id.btn_accessary) // 악세서리 버튼

        // 카테고리 버튼과 클릭 리스너 연결
        btn_outer.setOnClickListener(this)
        btn_top.setOnClickListener(this)
        btn_bottom.setOnClickListener(this)
        btn_onepiece.setOnClickListener(this)
        btn_shoes.setOnClickListener(this)
        btn_accessary.setOnClickListener(this)

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
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // 뷰를 팝업창으로 띄울 수 있게 해주는 역할
                val view = inflater.inflate(R.layout.activity_add_codi_popup, null) // 팝업창을 띄울 뷰

                var clothList = arrayListOf<Cloth>( Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth")) // 테스트용 데이터
                val rv_cloth: RecyclerView = view.findViewById(R.id.rv_cloth) // 팝업창의 옷 리사이클러뷰
                val clothAdapter = ClothAdapter(this, clothList) // 팝업창의 옷 리사이클러뷰의 어댑터
                val layoutManager: GridLayoutManager = GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

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
                        val resourceId = this.resources.getIdentifier(selectImage, "drawable", this.packageName) // 선택된 이미지의 리소스 아이디를 가져옴
                        val cloth = ContextCompat.getDrawable(this, resourceId)
                        cloth?.let{
                            canvasEditor.addDrawableSticker(cloth)
                        }
                    }
                    .setNegativeButton("취소", null) // 왼쪽 버튼 설정 - 취소시 아무 것도 하지 않음
                    .create()

                alertDialog.setView(view) // 다이얼로그에 뷰 배치
                alertDialog.show() // 다이얼로그를 보여줌
            }
        }
    }

}
