package com.seoultech.livingtogether_android.ui.nok

import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityAddNokBinding
import com.seoultech.livingtogether_android.nok.viewmodel.NOKViewModel

class AddNOKActivity : BaseActivity<ActivityAddNokBinding>(R.layout.activity_add_nok) {
    private lateinit var vm: NOKViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar,"보호자 등록하기")

        vm = viewModelProvider.get(NOKViewModel::class.java)

        binding.run {
            viewModel = vm
        }

        vm.finishHandler.observe(this, finishObserver)
    }
}
