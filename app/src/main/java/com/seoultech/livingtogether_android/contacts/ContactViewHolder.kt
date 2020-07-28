package com.seoultech.livingtogether_android.contacts

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemContactListBinding
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.nok.repository.NOKRepository

class ContactViewHolder(parent: ViewGroup, val listener: ContactListAdapter.OnContactClickListener?) : BaseViewHolder<NOKEntity, ItemContactListBinding>(R.layout.item_contact_list, parent) {
    private val nokRepository : NOKRepository by lazy { NOKRepository() }

    override fun bind(data: NOKEntity) {
        binding.run {
            textviewContactitemName.text = data.name
            textviewContactitemPhone.text = data.phoneNum

            constraintLayoutContactitemRoot.setOnClickListener {
                nokRepository.insert(data)
                listener?.onClick()
            }
        }
    }
}