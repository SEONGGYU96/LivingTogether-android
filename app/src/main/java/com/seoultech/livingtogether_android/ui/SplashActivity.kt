package com.seoultech.livingtogether_android.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.seoultech.livingtogether_android.ui.main.MainActivity


class SplashActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //권한이 없다면
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            //권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

        } else { //권한이 있다면
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // 요청이 취소되면 빈 Results 배열이 전달됨.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    //거부가 눌렸을 때 1회 호출되는 부분
                    //Todo: 권한이 없으면 앱을 사용할 수 없다는 다이얼로그 띄우기
                }
                return
            }
        }
    }
}