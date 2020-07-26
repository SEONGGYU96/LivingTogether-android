package com.seoultech.livingtogether_android.ui.contacts

import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivitySearchContactBinding
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity

class SearchContactActivity : BaseContactActivity<ActivitySearchContactBinding>(R.layout.activity_search_contact) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            viewModel = vm

            imagebuttonSearchcontactBack.setOnClickListener {
                finish()
            }

            imagebuttonSearchcontactClear.setOnClickListener {
                edittextSearchcontactSearch.text = null
            }


        }
    }
}