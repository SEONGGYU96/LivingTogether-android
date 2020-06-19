package com.seoultech.livingtogether_android.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes var layoutResId : Int) : AppCompatActivity() {

    protected lateinit var binding : B private set

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    protected lateinit var viewModelProvider: ViewModelProvider

    protected val TAG = javaClass.simpleName

    protected val finishObserver = Observer<Boolean> {
        if(it) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)

        viewModelProvider = ViewModelProvider(this, viewModelFactory)

        binding = DataBindingUtil.setContentView(this, layoutResId)

        binding.lifecycleOwner = this
    }
}