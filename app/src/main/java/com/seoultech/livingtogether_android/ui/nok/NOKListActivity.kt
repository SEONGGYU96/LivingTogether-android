package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNokListBinding
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration
import com.seoultech.livingtogether_android.viewmodel.NOKViewModel
import kotlinx.android.synthetic.main.activity_nok_list.*

class NOKListActivity : BaseActivity<ActivityNokListBinding>(R.layout.activity_nok_list) {
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }
    private val vm: NOKViewModel by lazy { NOKViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            lifecycleOwner = this@NOKListActivity

            viewModel = vm


            recyclerNokList.layoutManager = LinearLayoutManager(baseContext)
            recyclerNokList.adapter = nokAdapter
            recyclerNokList.addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.VERTICAL))

            buttonAddNokList.setOnClickListener {
                //Todo: 입력하여 추가하기 vs 주소록에서 추가하기 다이얼로그 띄우기
                startActivity(Intent(this@NOKListActivity, AddNOKActivity::class.java))
            }

            //NOKEntity 를 관찰하고 값이 비어있지 않다면 레이아웃을 변경
            vm.getAll().observe(this@NOKListActivity, Observer {
                if (it.isEmpty()) {
                    layoutNoNok.visibility = View.VISIBLE
                    layoutNoks.visibility = View.GONE
                } else {
                    layoutNoNok.visibility = View.GONE
                    layoutNoks.visibility = View.VISIBLE
                }
            })
        }

        setSupportActionBar(binding.toolbarNokList)

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
