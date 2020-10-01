package com.seoultech.livingtogether_android.ui.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityContactListBinding
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity
import kotlinx.android.synthetic.main.activity_contact_list.*

class ContactListActivity :
    BaseContactActivity<ActivityContactListBinding>(R.layout.activity_contact_list) {

    companion object {
        private const val REQUEST_SEARCH = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.imagebuttonContactlistSearch.setOnClickListener {
            startActivityForResult(Intent(this@ContactListActivity, SearchContactActivity::class.java), REQUEST_SEARCH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onInitRecyclerView(): RecyclerView {
        return recyclerview_contactlist
    }
}