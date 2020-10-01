package com.seoultech.livingtogether_android.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityProfileBinding
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel

class EditProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = obtainViewModel()

        binding.run {
            viewModel = profileViewModel
        }

        profileViewModel.finishHandler.observe(this, finishObserver)
    }

    override fun onBackPressed() {
        showDialog()
    }

    private fun showDialog() {
        if (!profileViewModel.isInitialized) {
            showInitialDialog()
        } else {
            showWarningDialog()
        }
    }

    private fun showWarningDialog() {
        AlertDialog.Builder(this)
            .setTitle("사용자 설정")
            .setMessage("사용자 설정이 저장되지 않았습니다. 작성한 데이터는 반영되지 않습니다.")
            .setPositiveButton("나가기") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("계속 작성") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showInitialDialog() {
        AlertDialog.Builder(this)
            .setTitle("사용자 설정")
            .setMessage("사용자 설정이 완료되지 않았습니다. 항목을 모두 입력해주세요.")
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun obtainViewModel() = obtainViewModel(ProfileViewModel::class.java)
}
