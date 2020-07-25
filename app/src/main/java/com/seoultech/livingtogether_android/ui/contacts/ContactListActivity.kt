package com.seoultech.livingtogether_android.ui.contacts

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.contacts.ContactListAdapter
import com.seoultech.livingtogether_android.contacts.ContactViewModel
import com.seoultech.livingtogether_android.databinding.ActivityContactListBinding

class ContactListActivity : BaseActivity<ActivityContactListBinding>(R.layout.activity_contact_list) {

    private val contactListAdapter : ContactListAdapter by lazy { ContactListAdapter() }
    private lateinit var vm: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = viewModelProvider.get(ContactViewModel::class.java)

        binding.run {
            viewModel = vm

            recyclerContactContaclist.layoutManager = LinearLayoutManager(this@ContactListActivity, RecyclerView.VERTICAL, false)
            recyclerContactContaclist.adapter = contactListAdapter
        }
    }
}