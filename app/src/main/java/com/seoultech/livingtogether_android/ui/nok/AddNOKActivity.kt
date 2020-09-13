package com.seoultech.livingtogether_android.ui.nok

import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityInsertNextOfKinBinding
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel

class AddNOKActivity : BaseActivity<ActivityInsertNextOfKinBinding>(R.layout.activity_insert_next_of_kin) {
    private lateinit var nextOfKinViewModel: NextOfKinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextOfKinViewModel = obtainViewModel()

        binding.run {
            viewModel = nextOfKinViewModel
        }
    }

    private fun obtainViewModel() = obtainViewModel(NextOfKinViewModel::class.java)
}
