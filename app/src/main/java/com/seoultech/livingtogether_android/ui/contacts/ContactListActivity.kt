package com.seoultech.livingtogether_android.ui.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityContactListBinding
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity

class ContactListActivity : BaseContactActivity<ActivityContactListBinding>(R.layout.activity_contact_list) {

    companion object {
        private const val REQUEST_SEARCH = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("연락처")

        binding.viewModel = vm
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact_list, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_contactlistmenu_search)  {
                startActivityForResult(Intent(this@ContactListActivity, SearchContactActivity::class.java), REQUEST_SEARCH)
                return true
        }

        return super.onOptionsItemSelected(item)
    }
}