package com.seoultech.livingtogether_android.ui.nok

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityAddNokBinding
import com.seoultech.livingtogether_android.viewmodel.NOKViewModel

class AddNOKActivity : BaseActivity<ActivityAddNokBinding>(R.layout.activity_add_nok) {
    private lateinit var vm: NOKViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(NOKViewModel::class.java)

        binding.run {
            viewModel = vm

            //Todo: 데이터바인딩으로 해결해보자. 지금은 너무 못났다.
            buttonAddNokList.setOnClickListener {
                vm.insert(vm.newNok)
                finish()
            }
        }

        setSupportActionBar(binding.toolbarAddNok)

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
