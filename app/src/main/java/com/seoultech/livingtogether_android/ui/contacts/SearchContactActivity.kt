package com.seoultech.livingtogether_android.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivitySearchContactBinding
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity

class SearchContactActivity : BaseContactActivity<ActivitySearchContactBinding>(R.layout.activity_search_contact) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            viewModel = vm

            vm.initContactResult("")

            imagebuttonSearchcontactBack.setOnClickListener {
                finish()
            }

            imagebuttonSearchcontactClear.setOnClickListener {
                edittextSearchcontactSearch.text = null
            }

            edittextSearchcontactSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    vm.initContactResult(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}