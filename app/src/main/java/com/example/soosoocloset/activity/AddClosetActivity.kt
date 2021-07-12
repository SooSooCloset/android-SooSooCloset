package com.example.soosoocloset.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.soosoocloset.R
import com.github.gabrielbb.cutout.CutOut
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_add_closet.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// 설명: 옷장 화면 옷 추가 클릭시 -> 옷 등록 화면
// author: Soohyun, created: 21.05.22
class AddClosetActivity : AppCompatActivity() {
    val REQUEST_CAMERA = 100 // 카메라 요청 코드
    val REQUEST_GALLERY = 101 // 갤러리 요청 코드
    lateinit var currentPhotoPath : String  // 카메라를 통해 가져온 이미지 경로

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_closet)

        val toolbar: Toolbar = findViewById(R.id.toolbar) // 상단바
        setSupportActionBar(toolbar) // 상단바를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) // 액션바의 타이틀을 숨김

        var category = arrayOf("아우터", "상의", "하의", "원피스", "신발", "악세서리") // 옷 종류 리스트
        var catogoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, category) // 스피너 어댑터 생성
        spinner_closet.adapter = catogoryAdapter // 스피너와 어댑터 연결

        // 스피너 선택 리스너
        spinner_closet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // 아무것도 선택하지 않은 경우
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            // 옷 종류를 선택한 경우
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@AddClosetActivity, category[position], Toast.LENGTH_SHORT).show()
            }

        }

        // 사진을 선택할 수 있는 버튼 클릭시
        btn_select_image.setOnClickListener{ view ->
            val items = arrayOf("카메라", "갤러리")
            // 사진을 가져올 방법을 선택하는 팝업창 생성 후 보여줌
            val builder = AlertDialog.Builder(this)
                    .setTitle("사진을 선택해주세요")
                    .setItems(items) { dialog, which -> settingPermission(which)} // 방법 선택시 권한을 설정하는 메소드 실행
                    .show()
        }
    }

    // 상단바와 메뉴를 연결하는 메소드
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_closet_menu, menu)
        return true
    }

    // 상단바의 메뉴 클릭시 호출되는 메소드
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_finishCloset -> {
                finish() // 액티비티 종료
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    // 권한을 설정하는 메소드
    fun settingPermission(select: Int){
        // 권한 객체 생성
        var permis = object: PermissionListener {
            // 권한을 허용한 경우
            override fun onPermissionGranted() {
                if(select == 0) { // 팝업창에서 카메라 선택시 -> 카메라 실행
                    startCamera()
                } else if(select == 1) { // 팝업창에서 갤러리 선택시 -> 갤러리 실행
                    startGallery()
                }
            }

            // 권한을 거부한 경우
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@AddClosetActivity, "권한 거부", Toast.LENGTH_SHORT).show()
                ActivityCompat.finishAffinity(this@AddClosetActivity) // 앱 종료
            }
        }

        // 권한이 허용되었는지 체크
        TedPermission.with(this)
                .setPermissionListener(permis)
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA) // 필요한 권한
                .check()
    }

    // 카메라로 사진을 촬영하는 메소드
    fun startCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{ createImageFile() }catch(ex: IOException){ null } // 촬영한 이미지 저장할 파일

                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(this, "com.example.soosoocloset.fileprovider", it) // 촬영한 이미지의 경로(Uri)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA) // 카메라 실행
                }
            }
        }
    }

    // 갤러리에서 사진을 선택할 수 있도록 하는 메소드
    fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_GALLERY) // 갤러리 열기
    }

    // 이미지 파일 이름 지정하는 메소드
    @Throws(IOException::class)
    private fun createImageFile() : File{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 오늘 날짜를 문자열로 가져옴
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES) // 이미지를 저장할 디렉토리

        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{ currentPhotoPath = absolutePath } // 지정된 이름으로 이미지를 저장할 파일 생성하여 리턴
    }

    // 다른 액티비티로 넘어갔다가 돌아왔을 때 실행되는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 카메라 실행 후 돌아온 경우
        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath) // 촬영한 이미지의 경로를 통해 이미지 파일 가져옴

            val image: Uri = Uri.fromFile(file) // 촬영한 이미지의 경로
            CutOut.activity().src(image).bordered().noCrop().start(this) // 촬영한 이미지 경로를 삽입하여 이미지 편집 액티비티 실행
        }

        // 갤러리 실행 후 돌아온 경우
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            val image = data?.data // 갤러리에서 선택한 이미지의 경로
            CutOut.activity().src(image).bordered().noCrop().start(this) // 갤러리에서 선택한 이미지 경로를 삽입하여 이미지 편집 액티비티 실행
        }

        // 이미지 편집 후 돌아온 경우
        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE.toInt()) {
            when (resultCode) {
                Activity.RESULT_OK -> { // 액티비티 결과가 OK인 경우
                    val imageUri = CutOut.getUri(data) // 편집한 이미지의 경로 가져옴

                    iv_closet.setImageURI(imageUri) // 경로를 통해 이미지뷰에 이미지 보여줌
                    iv_closet.setTag(imageUri) // 이미지를 tag로 등록
                }
                CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE.toInt() -> { // 액티비티 결과가 에러인 경우
                    var ex : Exception = CutOut.getError(data)
                }
                else -> print("User cancelled the CutOut screen")
            }
        }
    }

}