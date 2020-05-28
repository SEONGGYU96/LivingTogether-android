package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var vm: NOKViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(NOKViewModel::class.java)

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

            //debug
            buttonTest.setOnClickListener {
                startActivity(Intent(this@NOKListActivity, AddNOKActivity::class.java))
            }

            //NOKEntity 를 관찰하고 값이 비어있지 않다면 레이아웃을 변경
            //Fixme: add nok activity에서 보호자 등록 후, 다시 해당 액티비티로 돌아와서 active 상태가 되어도 리사이클러뷰가 갱신되지 않음
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nok_list_acitvity_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_go_to_add_nok_activity -> {
                startActivity(Intent(this@NOKListActivity, AddNOKActivity::class.java))
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
