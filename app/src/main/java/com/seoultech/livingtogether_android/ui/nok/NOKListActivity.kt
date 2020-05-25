package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNokListBinding
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration

class NOKListActivity : BaseActivity<ActivityNokListBinding>(R.layout.activity_nok_list) {
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            buttonAddNokList.setOnClickListener {
                //Todo: 입력하여 추가하기 vs 주소록에서 추가하기 다이얼로그 띄우기
                startActivity(Intent(this@NOKListActivity, AddNOKActivity::class.java))

                recyclerNokList.layoutManager = LinearLayoutManager(baseContext)
                recyclerNokList.adapter = nokAdapter
                recyclerNokList.addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.VERTICAL))
            }
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
