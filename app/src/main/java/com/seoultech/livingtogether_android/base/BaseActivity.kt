package com.seoultech.livingtogether_android.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.ViewModelFactory
import kotlinx.android.synthetic.main.layout_toolbar.view.*


abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes var layoutResId : Int) : AppCompatActivity() {

    protected lateinit var binding : B private set

    protected val TAG = javaClass.simpleName

    private var fragmentId: Int? = null

    protected val finishObserver = Observer<Boolean> {
        if(it) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)

        fragmentId = initFragmentId()

        binding.lifecycleOwner = this
    }

    protected fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

    fun goToFragment(cls: Class<*>, args: Bundle?): Fragment? {
        try {
            val fragment = cls.newInstance() as Fragment
            fragment.arguments = args
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(fragmentId!!, fragment).commit()
            return fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    protected open fun initFragmentId(): Int? {
        return null
    }
}