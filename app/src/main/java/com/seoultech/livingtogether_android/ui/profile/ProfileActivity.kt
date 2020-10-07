package com.seoultech.livingtogether_android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.databinding.ActivityProfileBinding
import com.seoultech.livingtogether_android.ui.main.MainActivity

class ProfileActivity : BaseProfileActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isNew = intent.getBooleanExtra("isNew", false)

        binding.run {
            viewModel = profileViewModel

            lttoolbarProfile.setBackButton(View.OnClickListener {
                if (isNew) {
                    startMainActivity()
                } else {
                    finish()
                }
            })

            groupProfileEditmode.visibility = View.GONE
            groupProfileNormalmode.visibility = View.VISIBLE

            buttonProfileEdit.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        if (isNew) {
            startMainActivity()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfile(false)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}