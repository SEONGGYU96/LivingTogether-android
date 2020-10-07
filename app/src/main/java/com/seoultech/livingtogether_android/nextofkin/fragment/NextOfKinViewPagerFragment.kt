package com.seoultech.livingtogether_android.nextofkin.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.MarginPageTransformer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseFragment
import com.seoultech.livingtogether_android.databinding.FragmentNextOfKinListBinding
import com.seoultech.livingtogether_android.nextofkin.BaseNextOfKinFragment
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinViewPagerAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.nok.NextOfKinListActivity
import com.seoultech.livingtogether_android.util.toPixel
import kotlinx.android.synthetic.main.fragment_next_of_kin_list.*

class NextOfKinViewPagerFragment: BaseNextOfKinFragment<FragmentNextOfKinListBinding>(R.layout.fragment_next_of_kin_list) {

    companion object {
        private const val ITEM_HEIGHT_WITH_MARGIN = 80
        private const val MARGIN_ITEM_VERTICAL = 16
        private const val ITEM_HEIGHT = 55
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.run {
            setBackButton()
            setRightTextButton(getString(R.string.toolbar_button_edit), View.OnClickListener {
                goToFragment(NextOfKinEditFragment::class.java, null)
            })
        }

        binding.run {
            viewModel = nextOfKinViewModel

            viewpager2Nextofkinlist.run {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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

    override fun onResume() {
        super.onResume()
        nextOfKinViewModel.onResume()
    }

    private fun initViewPager(height: Int) {
        var maxItem = height / ITEM_HEIGHT_WITH_MARGIN.toPixel()
        if (height % ITEM_HEIGHT_WITH_MARGIN.toPixel() >= ITEM_HEIGHT.toPixel()) {
            maxItem++
        }

        binding.viewpager2Nextofkinlist.run {
            (adapter as NextOfKinViewPagerAdapter).setMaxItems(maxItem)

            nextOfKinViewModel.items.observe(this@NextOfKinViewPagerFragment, Observer {
                (adapter as NextOfKinViewPagerAdapter).run {
                    setList(it)
                    if (itemCount == 1) {
                        circleindicator_nextofkinlist.visibility = View.INVISIBLE
                    } else {
                        circleindicator_nextofkinlist.visibility = View.VISIBLE
                    }
                }
            })
        }

        nextOfKinViewModel.start()
    }
}