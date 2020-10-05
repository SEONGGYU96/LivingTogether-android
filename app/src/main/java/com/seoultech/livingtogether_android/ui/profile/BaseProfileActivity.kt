package com.seoultech.livingtogether_android.ui.profile

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel

open class BaseProfileActivity<B : ViewDataBinding>(@LayoutRes layoutRes: Int) : BaseActivity<B>(layoutRes) {

    protected lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = obtainViewModel()
    }

    private fun obtainViewModel() = obtainViewModel(ProfileViewModel::class.java)
}