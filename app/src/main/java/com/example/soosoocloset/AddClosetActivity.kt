package com.example.soosoocloset

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.gabrielbb.cutout.CutOut
import com.google.android.material.floatingactionbutton.FloatingActionButton

// 설명: 옷장 화면 옷 추가 클릭시 -> 옷 등록 화면
// author: Soohyun, created: 21.05.21
class AddClosetActivity : AppCompatActivity() {
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_closet)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        imageView = findViewById(R.id.iv_closet)

        val imageIconUri: Uri = getUriFromDrawable(R.drawable.image_icon)
        imageView.setImageURI(imageIconUri)
        imageView.setTag(imageIconUri)

        val fab: FloatingActionButton = findViewById(R.id.btn_select_image)

        fab.setOnClickListener({ view ->
            val testImageUri: Uri = getUriFromDrawable(R.drawable.test_image)
            CutOut.activity()
                .src(testImageUri)
                .bordered()
                .noCrop()
                .start(this)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE.toInt()) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val imageUri = CutOut.getUri(data)
                    // Save the image using the returned Uri here
                    imageView.setImageURI(imageUri)
                    imageView.setTag(imageUri)
                }
                CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE.toInt() -> {
                    var ex : Exception = CutOut.getError(data)
                }
                else -> print("User cancelled the CutOut screen")
            }
        }
    }

    fun getUriFromDrawable(drawableId: Int): Uri {
        return Uri.parse("android.resource://$packageName/drawable/" + applicationContext.resources.getResourceEntryName(drawableId)
        )
    }
}