package com.seoultech.livingtogether_android.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.ViewModelFactory

abstract class BaseFragment<B: ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected lateinit var binding: B private set
    protected var TAG = javaClass.simpleName
    protected var mActivity: BaseActivity<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as BaseActivity<*>

        if (mActivity == null) {
            Log.e(TAG, "mActivity is null.")
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity = null
    }

    protected fun <T : ViewModel> obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(ApplicationImpl.getInstance())).get(viewModelClass)

    fun goToFragment(cls: Class<*>, args: Bundle?) {
        mActivity?.goToFragment(cls, args)
    }

    open fun onExitRound() {
        Log.d(TAG, "onExitRound()")
    }
}