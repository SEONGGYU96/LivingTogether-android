package com.seoultech.livingtogether_android.contacts

import android.view.ViewGroup
import com.seoultech.livingtogether_android.Injection
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemContactListBinding
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository

class ContactViewHolder(parent: ViewGroup, val listener: ContactListAdapter.OnContactClickListener?)
    : BaseViewHolder<NextOfKin, ItemContactListBinding>(R.layout.item_contact_list, parent) {

    private val nextOfKinRepository : NextOfKinRepository
            by lazy { Injection.provideNextOfKinRepository(parent.context.applicationContext) }

    override fun bind(data: NextOfKin) {
        binding.run {
            viewModel = data

            constraintLayoutContactitemRoot.setOnClickListener {
                nextOfKinRepository.saveNextOfKin(data)
                listener?.onClick()
            }
        }
    }
}