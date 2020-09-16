package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNextOfKinListBinding
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinListAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.util.MarginDecoration


class NextOfKinListActivity : BaseActivity<ActivityNextOfKinListBinding>(R.layout.activity_next_of_kin_list) {
    private val nextOfKinMainAdapter: NextOfKinListAdapter by lazy { NextOfKinListAdapter() }

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

            recyclerviewNextofkinlist.run {
                adapter = nextOfKinMainAdapter
                addItemDecoration(MarginDecoration(baseContext, 25, RecyclerView.VERTICAL))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nextOfKinViewModel.start()
    }

    private fun addNewNextOfKin() {
        startActivity(Intent(this, AddNextOfKinActivity::class.java))
    }

    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}
