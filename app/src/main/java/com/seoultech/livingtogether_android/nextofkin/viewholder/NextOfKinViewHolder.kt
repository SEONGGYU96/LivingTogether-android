package com.seoultech.livingtogether_android.nextofkin.viewholder

import android.view.View
import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemNextOfKinBinding
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.util.toPixel

class NextOfKinViewHolder(parent: ViewGroup, private val isList: Boolean, private val isEdit: Boolean,
    private val deleteListener: ((Int) -> Unit)?) :
    BaseViewHolder<NextOfKin, ItemNextOfKinBinding>(R.layout.item_next_of_kin, parent) {

    override fun bind(data: NextOfKin) {
        binding.run {
            nextOfKin = data

            if (isList) {
                cardviewNextofkinitemRoot.setCardBackgroundColor(itemView.context.getColor(R.color.colorWhite))
            }

            if (isEdit) {
                imagebuttonNextofkinitemDelete.run {
                    visibility = View.VISIBLE
                    deleteListener?.let {
                        setOnClickListener {
                            it(adapterPosition)
                        }
                    }
                }
            } else {
                val padding = itemView.context.resources
                    .getDimension(R.dimen.padding_item_next_of_kin).toInt()

                constraintlayoutNextofkinitemRoot.setPadding(padding, 0, 0, 0)
            }
        }
    }
}