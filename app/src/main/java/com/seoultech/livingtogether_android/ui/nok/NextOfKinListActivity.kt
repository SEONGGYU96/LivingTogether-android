package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNextOfKinListBinding
import com.seoultech.livingtogether_android.library.LTDialogBuilder
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinViewPagerAdapter
import com.seoultech.livingtogether_android.nextofkin.fragment.NextOfKinEditFragment
import com.seoultech.livingtogether_android.nextofkin.fragment.NextOfKinViewPagerFragment
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.contacts.ContactListActivity
import com.seoultech.livingtogether_android.util.toPixel
import kotlinx.android.synthetic.main.activity_next_of_kin_list.*
import kotlinx.android.synthetic.main.activity_next_of_kin_list.view.*


class NextOfKinListActivity : BaseActivity<ActivityNextOfKinListBinding>(R.layout.activity_next_of_kin_list) {

    private lateinit var nextOfKinViewModel: NextOfKinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextOfKinViewModel = obtainViewModel().apply {
            newNextOfKinEvent.observe(this@NextOfKinListActivity, Observer {
                this@NextOfKinListActivity.addNewNextOfKin(null)
            })

            emptyListEvent.observe(this@NextOfKinListActivity, Observer {
                if (it) {
                    finish()
                }
            })
        }

        binding.run {
            viewModel = nextOfKinViewModel
            lttoolbarNextofkinlist.setBackButton()
        }

        goToFragment(NextOfKinViewPagerFragment::class.java, null)
    }

    override fun initFragmentId(): Int? {
        return R.id.framelayout_nextofkin_fragment
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0] is NextOfKinViewPagerFragment) {
            super.onBackPressed()
        } else {
            nextOfKinViewModel.deleteCache.clear()
            goToFragment(NextOfKinViewPagerFragment::class.java, null)
        }
    }

    private fun <B: ViewDataBinding> addNewNextOfKin(binding: B?) {
        LTDialogBuilder<B, Any>()
            .addVerticalButton("직접 추가하기") { dialog, _ ->
                startActivity(Intent(this, AddNextOfKinActivity::class.java))
                dialog.dismiss()
            }
            .addVerticalButton("연락처에서 추가하기") {dialog, _ ->
                startActivity(Intent(this, ContactListActivity::class.java))
                dialog.dismiss()
            }
            .build()
            .show(supportFragmentManager, "add_new_next_of_kin")
    }
    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}
