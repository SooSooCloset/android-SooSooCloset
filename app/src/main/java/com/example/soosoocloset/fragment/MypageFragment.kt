package com.example.soosoocloset.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.soosoocloset.activity.MyinfoActivity
import com.example.soosoocloset.R
import com.example.soosoocloset.RetrofitClient
import com.example.soosoocloset.activity.LoginActivity
import com.example.soosoocloset.data.changeProfileResponse
import com.example.soosoocloset.data.deleteUserResponse
import com.example.soosoocloset.data.mypageResponse
import com.google.gson.internal.LinkedTreeMap
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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
import kotlin.collections.ArrayList

// 설명: 메인 화면 하단바의 마이페이지 클릭 -> 마이페이지 화면
// author: Soohyun, created: 21.07.30
class MypageFragment : Fragment() {
    lateinit var iv_profile : ImageView
    lateinit var prefs: SharedPreferences
    lateinit var user_id: String // 사용자 아이디
    val REQUEST_CAMERA = 100 // 카메라 요청 코드
    val REQUEST_GALLERY = 101 // 갤러리 요청 코드
    lateinit var currentPhotoPath : String  // 카메라/갤러리를 통해 가져온 이미지 경로
    var photoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_mypage, container, false)

        iv_profile = view.findViewById(R.id.iv_profile) // 사용자 프로필 사진 이미지뷰
        val lv_mypage : ListView = view.findViewById(R.id.lv_mypage) // 마이페이지의 리스트뷰
        val list = arrayOf("내 정보 수정", "로그아웃", "회원탈퇴") // 마이페이지의 리스트뷰에 들어갈 내용
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, list) // 리스트뷰의 어댑터
        lv_mypage.adapter = adapter // 리스트뷰와 어댑터 연결

        prefs = view.context.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        user_id = prefs.getString("id", null)!! // 사용자 아이디

        // 마이페이지 서버와 네트워크 통신하는 부분
        RetrofitClient.api.mypageRequest(user_id).enqueue(object : Callback<mypageResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<mypageResponse>, response: Response<mypageResponse>) {
                if(response.isSuccessful) {
                    var result: mypageResponse = response.body()!! // 응답 결과

                    if(result.code.equals("404")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 사용자 정보 조회 성공시
                        var nickname = result.info[0]["nickname"] as String
                        var profile = getImg(result.info) // 서버로부터 받은 프로필 이미지를 Bitmap 으로 변환

                        tv_nickname.setText(nickname) // 닉네임 세팅

                        // 프로필 사진 세팅
                        if(profile == null) {
                            iv_profile.setImageResource(R.drawable.user)
                        } else {
                            Glide.with(context!!).load(profile).circleCrop().into(iv_profile)
                        }
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<mypageResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })

        iv_profile.setOnClickListener{
            val items = arrayOf("카메라", "갤러리")
            // 사진을 가져올 방법을 선택하는 팝업창 생성 후 보여줌
            val builder = AlertDialog.Builder(context)
                .setTitle("사진을 선택해주세요")
                .setItems(items) { dialog, which -> settingPermission(which)} // 방법 선택시 권한을 설정하는 메소드 실행
                .show()
        }

        // 마이페이지 리스트뷰의 클릭 리스너
        lv_mypage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position) {
                0 -> { // 내 정보 수정
                    val intent = Intent(context, MyinfoActivity::class.java)
                    startActivity(intent) // 내 정보 수정 화면으로 이동
                }
                1 -> { // 로그아웃
                    // 자동 로그인 정보 삭제
                    val editor : SharedPreferences.Editor = prefs.edit()
                    editor.remove("id")
                    editor.commit()

                    startActivity(Intent(context, LoginActivity::class.java)) // 로그인 화면을 이동
                    activity?.finish() // 현재 액티비티 종료
                }
                2 -> { // 회원 탈퇴
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(context) // 경고 팝업창
                    dialog.setTitle("정말 회원 탈퇴 하시겠습니까?") //제목
                    dialog.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                        deleteUser()
                    })
                    dialog.setNegativeButton("아니요", null)
                    dialog.show()
                }
            }
        }

        return view
    }

    // 회원 탈퇴 메서드
    fun deleteUser() {
        // 회원탈퇴 서버와 네트워크 통신하는 부분
        RetrofitClient.api.deleteUserRequest(user_id).enqueue(object : Callback<deleteUserResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<deleteUserResponse>, response: Response<deleteUserResponse>) {
                if(response.isSuccessful) {
                    var result: deleteUserResponse = response.body()!! // 응답 결과

                    if(result.code.equals("404")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 회원탈퇴 성공시
                        Toast.makeText(context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(context, LoginActivity::class.java)) // 로그인 화면을 이동
                        activity?.finish() // 현재 액티비티 종료
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<deleteUserResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 이미지를 가져오는 메서드
    private fun getImg(input: List<Map<*, *>>): Bitmap {
        var data = (input[0])["user_profile"] as LinkedTreeMap<*, *>
        var array = data["data"] as ArrayList<Double>
        var bitmap: Bitmap = convertBitmap(array)

        return bitmap
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
                Toast.makeText(context, "권한 거부", Toast.LENGTH_SHORT).show()
                activity?.let { ActivityCompat.finishAffinity(it) } // 앱 종료
            }
        }

        // 권한이 허용되었는지 체크
        TedPermission.with(context)
            .setPermissionListener(permis)
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA) // 필요한 권한
            .check()
    }

    // 카메라로 사진을 촬영하는 메소드
    fun startCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                val photoFile: File? = try{ createImageFile() }catch(ex: IOException){ null } // 촬영한 이미지 저장할 파일

                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(context!!, "com.example.soosoocloset.fileprovider", it) // 촬영한 이미지의 경로(Uri)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.putExtra("crop", true)
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA) // 카메라 실행
                }
            }
        }
    }

    // 갤러리에서 사진을 선택할 수 있도록 하는 메소드
    fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.putExtra("crop", true)
        startActivityForResult(intent, REQUEST_GALLERY) // 갤러리 열기
    }

    // 이미지 파일 이름 지정하는 메소드
    @Throws(IOException::class)
    private fun createImageFile() : File{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) // 오늘 날짜를 문자열로 가져옴
        val storageDir : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) // 이미지를 저장할 디렉토리

        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{ currentPhotoPath = absolutePath } // 지정된 이름으로 이미지를 저장할 파일 생성하여 리턴
    }

    // 이미지 크롭 메서드
    fun cropImage(uri: Uri?){
        activity?.let {
            CropImage.activity(uri)
                .setCropShape(CropImageView.CropShape.RECTANGLE) // 원형으로 자르기
                .start(it, this@MypageFragment)
        }
    }

    // 다른 액티비티로 넘어갔다가 돌아왔을 때 실행되는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 카메라 실행 후 돌아온 경우
        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath) // 촬영한 이미지의 경로를 통해 이미지 파일 가져옴
            val selectedUri = Uri.fromFile(file) // 촬영한 이미지의 경로
            cropImage(selectedUri) // 크롭 액티비티 실행
        }

        // 갤러리 실행 후 돌아온 경우
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            data?.data?.let { uri ->
                cropImage(uri) // 크롭 액티비티 실행
            }
        }

        // 이미지 크롭 후 돌아온 경우
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data) // 크롭된 이미지

            if(resultCode == Activity.RESULT_OK){
                result.uri?.let {
                    changeProfile(result.uri) // 프로필 사진 변경 메서드 실행
                }
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 프로필 사진 변경 메서드
    fun changeProfile(uri: Uri) {
        val prefs : SharedPreferences = context!!.getSharedPreferences("User", Context.MODE_PRIVATE) // 자동로그인 정보 저장되어 있는 곳
        val user_id = RequestBody.create(MediaType.parse("text/plain"), prefs.getString("id", null)!!) // 사용자 아이디

        // 프로필 사진 파일로 변환
        var file = File(uri.path)
        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var profile_img = MultipartBody.Part.createFormData("profile_img", file.name, requestFile)

        // 프로필 사진 변경 서버와 네트워크 통신하는 부분
        RetrofitClient.api.changeProfileRequest(user_id, profile_img).enqueue(object : Callback<changeProfileResponse> {
            // 네트워크 통신 성공한 경우
            override fun onResponse(call: Call<changeProfileResponse>, response: Response<changeProfileResponse>) {
                if(response.isSuccessful) {
                    var result: changeProfileResponse = response.body()!! // 응답 결과

                    if(result.code.equals("400")) { // 에러 발생 시
                        Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }  else if(result.code.equals("200")) { // 회원탈퇴 성공시
                        Glide.with(context!!).load(uri).circleCrop().into(iv_profile) // 프로필 사진 변경
                    }
                }
            }

            // 네트워크 통신 실패한 경우
            override fun onFailure(call: Call<changeProfileResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}