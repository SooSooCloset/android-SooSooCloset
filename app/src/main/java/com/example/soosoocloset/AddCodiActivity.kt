package com.example.soosoocloset

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView


class AddCodiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_codi)

        val photoView = findViewById<PhotoView>(R.id.photoView)
        photoView.setImageResource(R.drawable.sample_cloth)
    }
}