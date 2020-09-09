package com.seoultech.livingtogether_android.ui.nok

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityNokListBinding
import com.seoultech.livingtogether_android.nextofkin.adapter.NOKAdapter
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.contacts.ContactListActivity
import com.seoultech.livingtogether_android.util.MarginDecoration
import java.lang.StringBuilder


class NextOfKinListActivity : BaseActivity<ActivityNokListBinding>(R.layout.activity_nok_list) {
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }

    private lateinit var nextOfKinViewModel: NextOfKinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar, "보호자")

        nextOfKinViewModel = obtainViewModel()

        binding.run {
            viewModel = nextOfKinViewModel

            recyclerNokList.layoutManager = LinearLayoutManager(baseContext)
            recyclerNokList.adapter = nokAdapter
            recyclerNokList.addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.VERTICAL))
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
                
                AlertDialog.Builder(this@NextOfKinListActivity)
                    .setTitle("보호자 정보 삭제")
                    .setMessage(message)
                    .setPositiveButton("삭제") { dialog, _ ->
                        nextOfKinViewModel.deleteNextOfKin(item.phoneNumber)
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
                startActivity(Intent(this@NextOfKinListActivity, AddNOKActivity::class.java))
                return true
            }

            R.id.item_noklist_addfromcontacts -> {
                startActivity(Intent(this@NextOfKinListActivity, ContactListActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
}
