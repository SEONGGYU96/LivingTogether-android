package com.seoultech.livingtogether_android.nextofkin.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseFragment
import com.seoultech.livingtogether_android.databinding.FragmentNextOfKinEditBinding
import com.seoultech.livingtogether_android.generated.callback.OnClickListener
import com.seoultech.livingtogether_android.nextofkin.BaseNextOfKinFragment
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinEditAdapter
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinListAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.nok.NextOfKinListActivity
import com.seoultech.livingtogether_android.util.MarginDecoration
import kotlinx.android.synthetic.main.activity_next_of_kin_list.*

class NextOfKinEditFragment: BaseNextOfKinFragment<FragmentNextOfKinEditBinding>(R.layout.fragment_next_of_kin_edit) {

    private lateinit var nextOfKinEditAdapter: NextOfKinEditAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.run {
            clearButton()
            setBackButton(View.OnClickListener {
                nextOfKinViewModel.deleteCache.clear()
                goToFragment(NextOfKinViewPagerFragment::class.java, null)
            })
            setRightTextButton("확인", View.OnClickListener {
                nextOfKinViewModel.deleteNextOfKinInCache()
                goToFragment(NextOfKinViewPagerFragment::class.java, null)
            })
        }

        nextOfKinEditAdapter = NextOfKinEditAdapter {
            nextOfKinViewModel.deleteCache.add(it)
        }

        nextOfKinViewModel.loadNextOfKin { nextOfKinEditAdapter.setList(it) }

        binding.run {
            viewModel = nextOfKinViewModel

            recyclerviewNextofkinedit.run {
                adapter = nextOfKinEditAdapter
                addItemDecoration(MarginDecoration(context, 25, RecyclerView.VERTICAL))
            }
        }
    }
}