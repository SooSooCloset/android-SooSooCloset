package com.example.soosoocloset

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

//설명: 코디 만들기 화면
// author: Sumin
class AddCodiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)
    }

    //Drag and Drop
    fun onTouch(v: ImageView, event: MotionEvent): Boolean {
        val parentWidth = (v.parent as ViewGroup).width // 부모 View 의 Width
        val parentHeight = (v.parent as ViewGroup).height // 부모 View 의 Height
        if (event.action == MotionEvent.ACTION_MOVE) { //Drag
            v.x = v.x + event.x - v.width / 2
            v.y = v.y + event.y - v.height / 2
        } else if (event.action == MotionEvent.ACTION_UP) { //Drop
            if (v.x < 0) {
                v.x = 0f
            } else if (v.x + v.width > parentWidth) {
                v.x = parentWidth - v.width.toFloat()
            }
            if (v.y < 0) {
                v.y = 0f
            } else if (v.y + v.height > parentHeight) {
                v.y = parentHeight - v.height.toFloat()
            }
        }
        return true
    }

}