package com.seoultech.livingtogether_android.ui

import android.Manifest.permission
import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.seoultech.livingtogether_android.Injection
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.ui.main.MainActivity
import com.seoultech.livingtogether_android.ui.profile.EditProfileActivity
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import kotlinx.android.synthetic.main.activity_splash.*

//Todo: 로직들을 ViewModel로 옮길 순 없을까?
class SplashActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_PERMISSION: Int = 1000
    }

    private var isInitialOperation = true

    private var isAnimationTerminated = false

    private var isPermissionCheckFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startAnimation()

        Injection.provideProfileRepository(applicationContext)
            .getProfile(object : ProfileDataSource.GetProfileCallback {
                override fun onProfileLoaded(profile: Profile) {
                    isInitialOperation = false
                    start()
                }

                override fun onDataNotAvailable() {
                    isInitialOperation = true
                    start()
                }
            })
    }

    private fun start() {
        //권한이 없다면
        if (!checkPermission()) {
            //권한 요청
            requestPermission()

        } else { //권한이 있다면
            permissionCheckFinish()
        }
    }

    private fun permissionCheckFinish() {
        isPermissionCheckFinished = true
        startNextActivity()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(permission.ACCESS_FINE_LOCATION, permission.SEND_SMS),
            REQUEST_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 요청이 취소되면 빈 Results 배열이 전달됨.
        if (checkPermissionResponse(grantResults)) {

            permissionCheckFinish()
        } else {
            //거부가 되었을 때 다이얼로그 띄우기
            showDialog()
        }
    }

    private fun showDialog() {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setPositiveButton(getString(R.string.allow)) { _, _ ->
                    requestPermission()
                }
                setNegativeButton(getString(R.string.do_finish)) { _, _ ->
                    finish()
                }
            }
            builder.setMessage(getString(R.string.notice_permission_must_be_granted))
            builder.create()
        }
        alertDialog?.show()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionResponse(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    private fun startAnimation() {
        lottie_splash.run {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimationTerminated = true
                    startNextActivity()
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}
            })
            playAnimation()
        }
    }

    private fun startNextActivity() {
        if (!isAnimationTerminated || !isPermissionCheckFinished) {
            return
        }

        if (isInitialOperation) {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.getBooleanExtra("isNew", true)
            startActivity(intent)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
