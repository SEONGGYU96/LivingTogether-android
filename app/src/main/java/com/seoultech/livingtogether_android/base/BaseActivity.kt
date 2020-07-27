package com.seoultech.livingtogether_android.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import kotlinx.android.synthetic.main.layout_toolbar.view.*


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

    protected fun setToolbar(toolbar: View, title: String) {
        setSupportActionBar(toolbar as Toolbar)
        toolbar.textview_toolbar_title.text = title

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_32dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}