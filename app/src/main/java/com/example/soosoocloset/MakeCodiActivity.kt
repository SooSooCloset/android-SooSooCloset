package com.example.soosoocloset

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MakeCodiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_codi)

        val photoView = findViewById<PhotoView>(R.id.photoView)
        photoView.setImageResource(R.drawable.sample_cloth)
    }
}