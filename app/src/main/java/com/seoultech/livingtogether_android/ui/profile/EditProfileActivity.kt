package com.seoultech.livingtogether_android.ui.profile

import android.app.AlertDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityProfileBinding
import com.seoultech.livingtogether_android.ui.main.MainActivity

class EditProfileActivity : BaseProfileActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 100
    }

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
                    if (isNew) {
                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                })

                searchAddressEvent.observe(this@EditProfileActivity, Observer {
                    startActivityForResult(
                        Intent(this@EditProfileActivity, WebViewActivity::class.java),
                        SEARCH_ADDRESS_ACTIVITY
                    )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val city = data?.getStringExtra("city")
                val fullAddress = data?.getStringExtra("fullAddress")
                if (city == null || fullAddress == null) {
                    return
                }
                profileViewModel.setAddress(city, fullAddress)
            }
        }
    }
}
