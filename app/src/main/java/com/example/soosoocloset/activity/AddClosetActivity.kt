package com.example.soosoocloset.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.data.addclothResponse
import com.github.gabrielbb.cutout.CutOut
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_closet.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// 설명: 옷장 화면 옷 추가 클릭시 -> 옷 등록 화면
// author: Soohyun, created: 21.05.22
class AddClosetActivity : AppCompatActivity() {
    val REQUEST_CAMERA = 100 // 카메라 요청 코드
    val REQUEST_GALLERY = 101 // 갤러리 요청 코드
    lateinit var currentPhotoPath : String  // 카메라/갤러리를 통해 가져온 이미지 경로
    var imageUri: Uri = Uri.EMPTY // 편집된 이미지 경로
    var selectCategory: Int = -1 // 선택된 카테고리 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_closet)

        val toolbar: Toolbar = findViewById(R.id.toolbar) // 상단바
        setSupportActionBar(toolbar) // 상단바를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false) // 액션바의 타이틀을 숨김
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon) // 뒤로가기 버튼 아이콘 변경

        var categoryArray = arrayOf("아우터", "상의", "하의", "원피스", "신발", "악세서리") // 옷 종류 리스트
        var catogoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryArray) // 스피너 어댑터 생성
        spinner_closet.adapter = catogoryAdapter // 스피너와 어댑터 연결

        // 스피너 선택 리스너
        spinner_closet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            // 아무것도 선택하지 않은 경우
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            // 옷 종류를 선택한 경우
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectCategory = position
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
            android.R.id.home -> { // 뒤로가기 버튼 클릭한 경우
                finish()
                return true
            }
            R.id.item_finishCloset -> {
                // 입력 양식 확인 후 조치, 입력 양식 맞으면 서버와 통신
                if(selectCategory == -1) {
                    Toast.makeText(this@AddClosetActivity,"옷 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else if (Uri.EMPTY.equals(imageUri)){
                    Toast.makeText(this@AddClosetActivity,"사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    var categoryArray = arrayOf("outer", "top", "bottom", "onepiece", "shoes", "acccesary")
                    val prefs : SharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳

                    // 입력된 정보 가져오는 부분
                    val user_id = RequestBody.create(MediaType.parse("text/plain"), prefs.getString("id", null)!!) // 사용자 아이디
                    val category = RequestBody.create(MediaType.parse("text/plain"), categoryArray[selectCategory]) // 옷 카테고리
                    val description = RequestBody.create(MediaType.parse("text/plain"), et_content.text.toString().trim()) // 옷 설명

                    // 옷 이미지 파일로 변환
                    var file = File(imageUri.path)
                    var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    var cloth_img = MultipartBody.Part.createFormData("cloth_img", file.name, requestFile)

                    // 옷 추가 서버와 네트워크 통신하는 부분
                    RetrofitClient.api.addclothRequest(user_id, category, cloth_img, description).enqueue(object : Callback<addclothResponse> {
                        // 네트워크 통신 성공한 경우
                        override fun onResponse(call: Call<addclothResponse>, response: Response<addclothResponse>) {

                            if(response.isSuccessful) {
                                var result: addclothResponse = response.body()!! // 응답 결과

                                if(result.code.equals("400")) {
                                    Toast.makeText(this@AddClosetActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                } else if(result.code.equals("200")) {
                                    Toast.makeText(this@AddClosetActivity, "옷이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                    finish() // 액티비티 종료
                                }
                            }

                        }

                        // 네트워크 통신 실패한 경우
                        override fun onFailure(call: Call<addclothResponse>, t: Throwable) {
                            Toast.makeText(this@AddClosetActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
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
                    imageUri = CutOut.getUri(data) // 편집한 이미지의 경로 가져옴

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