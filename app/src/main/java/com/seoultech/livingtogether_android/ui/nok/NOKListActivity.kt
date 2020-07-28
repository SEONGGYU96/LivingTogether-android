package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNokListBinding
import com.seoultech.livingtogether_android.nok.adapter.NOKAdapter
import com.seoultech.livingtogether_android.nok.viewmodel.NOKViewModel
import com.seoultech.livingtogether_android.ui.contacts.ContactListActivity
import com.seoultech.livingtogether_android.util.MarginDecoration
import java.lang.StringBuilder


class NOKListActivity : BaseActivity<ActivityNokListBinding>(R.layout.activity_nok_list) {
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }

    private lateinit var vm: NOKViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar, "보호자")

        vm = viewModelProvider.get(NOKViewModel::class.java)

        binding.run {
            viewModel = vm

            recyclerNokList.layoutManager = LinearLayoutManager(baseContext)
            recyclerNokList.adapter = nokAdapter
            recyclerNokList.addItemDecoration(
                MarginDecoration(
                    baseContext,
                    15,
                    RecyclerView.VERTICAL
                )
            )

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

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = nokAdapter.getItem(viewHolder.layoutPosition)
                val message = StringBuilder()
                    .append(item.name)
                    .append("님의 정보를 삭제하시겠습니까?")
                
                AlertDialog.Builder(this@NOKListActivity)
                    .setTitle("보호자 정보 삭제")
                    .setMessage(message)
                    .setPositiveButton("삭제") { dialog, _ ->
                        vm.removeItem(item.phoneNum)
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                        nokAdapter.notifyDataSetChanged()
                    }
                    .show()
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerNokList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_noklist_addnok -> {
                startActivity(Intent(this@NOKListActivity, AddNOKActivity::class.java))
                return true
            }

            R.id.item_noklist_addfromcontacts -> {
                startActivity(Intent(this@NOKListActivity, ContactListActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
