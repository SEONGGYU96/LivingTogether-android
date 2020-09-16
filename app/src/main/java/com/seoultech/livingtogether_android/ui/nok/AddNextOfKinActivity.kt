package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityInsertNextOfKinBinding
import com.seoultech.livingtogether_android.nextofkin.viewmodel.AddNextOfKinViewModel
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel

class AddNextOfKinActivity : BaseActivity<ActivityInsertNextOfKinBinding>(R.layout.activity_insert_next_of_kin) {
    private lateinit var nextOfKinViewModel: AddNextOfKinViewModel

    private var nameIsNotEmpty = false
    private var phoneNumberIsNotEmpty = false
    private var isButtonEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextOfKinViewModel = obtainViewModel().apply {
            nextOfKinUpdated.observe(this@AddNextOfKinActivity, Observer {
                startActivity(Intent(this@AddNextOfKinActivity, NextOfKinListActivity::class.java))
                finish()
            })

            isNameNotEmpty.observe(this@AddNextOfKinActivity, Observer {
                nameIsNotEmpty = it
                setButtonEnabled()
            })

            isPhoneNumberNotEmpty.observe(this@AddNextOfKinActivity, Observer {
                phoneNumberIsNotEmpty = it
                setButtonEnabled()
            })
        }

        binding.run {
            viewModel = nextOfKinViewModel
        }
    }

    private fun setButtonEnabled() {
        binding.buttonInsertnextofkinInsert.run {
            if (nameIsNotEmpty && phoneNumberIsNotEmpty) {
                if (!isButtonEnabled) {
                    backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorMainGreen))
                    isEnabled = true
                    isButtonEnabled = true
                }
            } else {
                if (isButtonEnabled) {
                    backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorRegisterButtonGray))
                    isEnabled = false
                    isButtonEnabled = false
                }
            }
        }
    }

    private fun obtainViewModel() = obtainViewModel(AddNextOfKinViewModel::class.java)
}
