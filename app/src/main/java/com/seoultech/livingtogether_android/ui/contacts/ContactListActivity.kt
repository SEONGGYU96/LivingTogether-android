package com.seoultech.livingtogether_android.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityContactListBinding
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity

class ContactListActivity : BaseContactActivity<ActivityContactListBinding>(R.layout.activity_contact_list) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = vm

        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_32dp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact_list, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.item_contactlistmenu_search -> {
                startActivity(Intent(this@ContactListActivity, SearchContactActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}