package com.seoultech.livingtogether_android.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityEditProfileBinding
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 100
    }
    private lateinit var vm: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar, "사용자 정보 등록")

        vm = viewModelProvider.get(ProfileViewModel::class.java)

        binding.run {
            viewModel = vm
        }

        vm.finishHandler.observe(this, finishObserver)

        edit_address_profile.setOnClickListener {
            startActivityForResult(Intent(this@EditProfileActivity, WebViewActivity::class.java), SEARCH_ADDRESS_ACTIVITY)
        }
    }

    //Todo: 그냥 뒤로가기 버튼을 누르면 저장되지 않았다는 다이얼로그 띄우기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                val mData = data?.getStringExtra("data")
                if (data != null) edit_address_profile.setText(mData)
            }
        }
    }
}
