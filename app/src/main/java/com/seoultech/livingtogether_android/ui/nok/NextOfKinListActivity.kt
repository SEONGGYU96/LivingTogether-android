package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_next_of_kin_list.view.*


class NextOfKinListActivity : BaseActivity<ActivityNextOfKinListBinding>(R.layout.activity_next_of_kin_list) {
    companion object {
        private const val ITEM_HEIGHT_WITH_MARGIN = 80
        private const val MARGIN_ITEM_VERTICAL = 16
        private const val ITEM_HEIGHT = 55
    }

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

            lttoolbarNextofkinlist.setBackButton()

            viewpager2Nextofkinlist.run {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewpager2Nextofkinlist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        initViewPager(viewpager2Nextofkinlist.height)
                    }
                })
                adapter = NextOfKinViewPagerAdapter()
                offscreenPageLimit = 3
                setPageTransformer(MarginPageTransformer(MARGIN_ITEM_VERTICAL.toPixel()))
            }

            circleindicatorNextofkinlist.setViewPager(viewpager2Nextofkinlist)
            (viewpager2Nextofkinlist.adapter as NextOfKinViewPagerAdapter)
                .registerAdapterDataObserver(circleindicatorNextofkinlist.adapterDataObserver)
        }
    }

    private fun initViewPager(height: Int) {
        var maxItem = height / ITEM_HEIGHT_WITH_MARGIN.toPixel()
        if (height % ITEM_HEIGHT_WITH_MARGIN.toPixel() >= ITEM_HEIGHT.toPixel()) {
            maxItem++
        }

        binding.viewpager2Nextofkinlist.run {
            (adapter as NextOfKinViewPagerAdapter).setMaxItems(maxItem)

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
