package com.seoultech.livingtogether_android.ui.contacts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityContactListBinding
import com.seoultech.livingtogether_android.ui.SplashActivity
import com.seoultech.livingtogether_android.ui.contacts.base.BaseContactActivity
import com.seoultech.livingtogether_android.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_contact_list.*

class ContactListActivity : BaseContactActivity<ActivityContactListBinding>(R.layout.activity_contact_list) {

    companion object {
        private const val REQUEST_SEARCH = 1000
        private const val REQUEST_PERMISSION = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactViewModel.isPermissionGranted.observe(this@ContactListActivity, Observer {
            if (it) {
                Log.d(TAG, "requestPermission : permission is already granted.")
                contactViewModel.initContact()
            } else {
                requestPermission()
            }
        })

        binding.run {
            viewModel = contactViewModel
            contactViewModel.start()

            imagebuttonContactlistSearch.setOnClickListener {
                startActivityForResult(
                    Intent(this@ContactListActivity, SearchContactActivity::class.java), REQUEST_SEARCH)
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 요청이 취소되면 빈 Results 배열이 전달됨.
        if (!contactViewModel.checkPermissionResponse(grantResults)) {
            finish()
        }
    }

    override fun onInitRecyclerView(): RecyclerView {
        return recyclerview_contactlist
    }
}