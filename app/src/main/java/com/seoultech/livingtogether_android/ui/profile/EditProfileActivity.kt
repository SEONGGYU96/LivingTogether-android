package com.seoultech.livingtogether_android.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityEditProfileBinding
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {

    private lateinit var vm: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar, "사용자 정보 등록")

        vm = viewModelProvider.get(ProfileViewModel::class.java)

        binding.run {
            viewModel = vm
        }

        vm.finishHandler.observe(this, finishObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            showDialog()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showDialog()
    }

    private fun showDialog() {
        if (!vm.isInitialized) {
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
}
