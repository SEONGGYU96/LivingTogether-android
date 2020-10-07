package com.seoultech.livingtogether_android.nextofkin

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.seoultech.livingtogether_android.base.BaseFragment
import com.seoultech.livingtogether_android.library.LTToolbar
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.nok.NextOfKinListActivity
import kotlinx.android.synthetic.main.activity_next_of_kin_list.*

open class BaseNextOfKinFragment<B: ViewDataBinding>(@LayoutRes layoutRes: Int): BaseFragment<B>(layoutRes) {
    protected lateinit var nextOfKinViewModel: NextOfKinViewModel
    protected var toolbar: LTToolbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextOfKinViewModel = obtainViewModel()
        toolbar = (mActivity as NextOfKinListActivity).lttoolbar_nextofkinlist
    }

    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}