package com.seoultech.livingtogether_android.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityProfileBinding
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel

class EditProfileActivity : BaseProfileActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNew = intent.getBooleanExtra("isNew", false)

        binding.run {
            viewModel = profileViewModel.apply {
                isWrongData.observe(this@EditProfileActivity, Observer {
                    showInitialDialog()
                })

                saveProfileEvent.observe(this@EditProfileActivity, Observer {
                    val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                    intent.putExtra("isNew", isNew)
                    startActivity(intent)
                    finish()
                })
            }

            if (isNew) {
                textviewProfileNew.visibility = View.VISIBLE
                buttonProfileEdit.text = "등록하기"
            } else {
                lttoolbarProfile.setBackButton()
            }

            groupProfileNormalmode.visibility = View.INVISIBLE
            groupProfileEditmode.visibility = View.VISIBLE

            buttonProfileEdit.setOnClickListener {
                profileViewModel.saveProfile()
            }

            profileViewModel.getProfile(true)
        }
    }

    override fun onBackPressed() {
        if (!isNew) {
            super.onBackPressed()
        }
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
