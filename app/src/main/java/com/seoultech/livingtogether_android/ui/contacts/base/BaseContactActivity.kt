package com.seoultech.livingtogether_android.ui.contacts.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.contacts.ContactListAdapter
import com.seoultech.livingtogether_android.contacts.ContactViewModel
import kotlinx.android.synthetic.main.activity_contact_list.*


open class BaseContactActivity<B: ViewDataBinding>(@LayoutRes layoutResId: Int) : BaseActivity<B>(layoutResId) {
    private val contactListAdapter : ContactListAdapter by lazy { ContactListAdapter() }
    protected lateinit var vm: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = viewModelProvider.get(ContactViewModel::class.java)

        binding.lifecycleOwner = this

        contactListAdapter.setOnContactClickListener(object: ContactListAdapter.OnContactClickListener {
            override fun onClick() {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        recycler_contact.run {
            layoutManager = LinearLayoutManager(this@BaseContactActivity, RecyclerView.VERTICAL, false)
            adapter = contactListAdapter
        }
    }

}