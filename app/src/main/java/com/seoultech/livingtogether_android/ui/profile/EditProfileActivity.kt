package com.seoultech.livingtogether_android.ui.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityEditProfileBinding
import com.seoultech.livingtogether_android.viewmodel.ProfileViewModel

class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {
    private lateinit var vm: ProfileViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.run {
            lifecycleOwner = this@EditProfileActivity
            viewModel = vm

            //Todo: 새로운 객체 user를 만들고 이런식으로 값을 매핑하는 방법 말고 데이터바인딩 선에서 해결할 수 있는 방법 찾기
            buttonEditProfile.setOnClickListener {
                vm.user.name = editNameProfile.text.toString()
                vm.user.phoneNum = editPhoneProfile.text.toString()
                vm.user.address = editAddressProfile.text.toString()

                vm.update(vm.user)

                finish()
            }
        }

        setSupportActionBar(binding.toolbarEditProfile)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_32dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
