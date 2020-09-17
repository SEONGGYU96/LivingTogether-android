package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNextOfKinListBinding
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinViewPagerAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.util.toPixel


class NextOfKinListActivity : BaseActivity<ActivityNextOfKinListBinding>(R.layout.activity_next_of_kin_list) {
    private lateinit var nextOfKinViewModel: NextOfKinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextOfKinViewModel = obtainViewModel().apply {
            newNextOfKinEvent.observe(this@NextOfKinListActivity, Observer {
                this@NextOfKinListActivity.addNewNextOfKin()
            })
        }

        binding.run {
            viewModel = nextOfKinViewModel

            viewpager2Nextofkinlist.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewpager2Nextofkinlist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    initViewPager(viewpager2Nextofkinlist.height)
                }
            })
        }
    }

    private fun initViewPager(height: Int) {
        var maxItem = height / 80.toPixel()
        if (height % 80.toPixel() >= 55.toPixel()) {
            maxItem++
        }

        binding.viewpager2Nextofkinlist.run {
            adapter = NextOfKinViewPagerAdapter(maxItem)
            offscreenPageLimit = 3
            setPageTransformer(MarginPageTransformer(16.toPixel()))

            binding.tablayoutNextofkinlist.run {
                TabLayoutMediator(this, binding.viewpager2Nextofkinlist) { _, _ ->  }.attach()
            }

            nextOfKinViewModel.items.observe(this@NextOfKinListActivity, Observer {
                (adapter as NextOfKinViewPagerAdapter).setList(it)
            })
        }

        nextOfKinViewModel.start()
    }

    private fun addNewNextOfKin() {
        startActivity(Intent(this, AddNextOfKinActivity::class.java))
    }

    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}
