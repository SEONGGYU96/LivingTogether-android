package com.seoultech.livingtogether_android.ui.contacts.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.contacts.ContactListAdapter
import com.seoultech.livingtogether_android.contacts.ContactViewModel


abstract class BaseContactActivity<B: ViewDataBinding>(@LayoutRes layoutResId: Int) : BaseActivity<B>(layoutResId) {
    private val contactListAdapter : ContactListAdapter by lazy { ContactListAdapter() }
    protected lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = obtainViewModel()

        binding.lifecycleOwner = this

        contactListAdapter.setOnContactClickListener(object: ContactListAdapter.OnContactClickListener {
            override fun onClick() {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        onInitRecyclerView().adapter = contactListAdapter
    }

    abstract fun onInitRecyclerView() : RecyclerView

    private fun obtainViewModel() = obtainViewModel(ContactViewModel::class.java)
}