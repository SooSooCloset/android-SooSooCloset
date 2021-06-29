package com.example.soosoocloset

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.domain.Cloth
import kotlin.math.max
import kotlin.math.min

//설명: 코디 만들기 화면
// author: Sumin
// author: Soohyun, created: 21.06.29

class AddCodiActivity : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    lateinit var imageView: ImageView // 기본 세팅 이미지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        val layout_add_codi = findViewById<ConstraintLayout>(R.id.layout_add_codi) // 코디 추가 화면의 레이아웃
        val btn_outer = findViewById<Button>(R.id.btn_outer) // 아우터 버튼
        val btn_top = findViewById<Button>(R.id.btn_top) // 상의 버튼
        val btn_bottom = findViewById<Button>(R.id.btn_bottom) // 하의 버튼
        val btn_onepiece = findViewById<Button>(R.id.btn_onepiece) // 원피스 버튼
        val btn_shoes = findViewById<Button>(R.id.btn_shoes) // 신발 버튼
        val btn_accessary = findViewById<Button>(R.id.btn_accessary) // 악세서리 버튼
        imageView = findViewById<ImageView>(R.id.iv_codi_default)

        var selectImage: String = "" // 선택된 이미지 명

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // 아우터 버튼 클릭시
        btn_outer.setOnClickListener{
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater // 뷰를 팝업창으로 띄울 수 있게 해주는 역할
            val view = inflater.inflate(R.layout.activity_add_codi_popup, null) // 팝업창을 띄울 뷰

            var clothList = arrayListOf<Cloth>(Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth"), Cloth("sample_cloth")) // 테스트용 데이터
            val rv_cloth : RecyclerView = view.findViewById(R.id.rv_cloth) // 팝업창의 옷 리사이클러뷰
            val clothAdapter = ClothAdapter(this, clothList) // 팝업창의 옷 리사이클러뷰의 어댑터
            val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 3) // 그리드 레이아웃 매니저

            rv_cloth.adapter = clothAdapter // 리사이클러뷰와 어댑터 연결
            rv_cloth.layoutManager = layoutManager // 리사이클러뷰와 레이아웃 매니저 연결

            // 옷 리스트의 아이템 클릭시
            clothAdapter.setItemClickListener(object : ClothAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    selectImage = clothList[position].image // 선택된 이미지 명 저장
                }
            })

            // 다이얼로그 생성
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("추가할 옷을 선택해주세요.") // 타이틀 설정
                .setPositiveButton("선택") {dialog, which -> // 오른쪽 버튼 설정
                    val resourceId = this.resources.getIdentifier(selectImage, "drawable", this.packageName) // 선택된 이미지의 리소스 아이디를 가져옴
                    val cloth = ImageView(this) // 새로운 이미지뷰 생성

                    // 이미지뷰를 배치할 레이아웃에서 위치와 크기 설정
                    val layoutParams = ConstraintLayout.LayoutParams(400, 400)
                    layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
                    layoutParams.endToEnd = ConstraintSet.PARENT_ID
                    layoutParams.startToStart = ConstraintSet.PARENT_ID
                    layoutParams.topToTop = ConstraintSet.PARENT_ID

                    cloth.setLayoutParams(layoutParams)
                    cloth.setImageResource(resourceId) // 이미지뷰에 이미지를 넣어줌
                    layout_add_codi.addView(cloth) // 코디 추가 화면에 생성한 이미지뷰 동적 추가
                }
                .setNegativeButton("취소", null) // 왼쪽 버튼 설정 - 취소시 아무 것도 하지 않음
                .create()

            alertDialog.setView(view) // 다이얼로그에 뷰 배치
            alertDialog.show() // 다이얼로그를 보여줌
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

        // 이미지 이동
        if (motionEvent.action == MotionEvent.ACTION_MOVE) {

            imageView.y = motionEvent.rawY - imageView.height/2
            imageView.x = motionEvent.rawX - imageView.width/2
        }

        scaleGestureDetector.onTouchEvent(motionEvent) // 이미지 확대/축소

        return true
    }

    // 이미지 확대/축소 리스너
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }

}