package com.seoultech.livingtogether_android.ui.nok

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNextOfKinListBinding
import com.seoultech.livingtogether_android.nextofkin.adapter.NOKAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.util.MarginDecoration


class NextOfKinListActivity : BaseActivity<ActivityNextOfKinListBinding>(R.layout.activity_next_of_kin_list) {
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }

    private lateinit var nextOfKinViewModel: NextOfKinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextOfKinViewModel = obtainViewModel()

        binding.run {
            viewModel = nextOfKinViewModel

            recyclerviewNextofkinlist.run {
                adapter = nokAdapter
                addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.VERTICAL))
            }
        }
    }

    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}
