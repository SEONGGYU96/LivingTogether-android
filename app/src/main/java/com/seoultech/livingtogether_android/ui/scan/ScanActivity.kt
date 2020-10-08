package com.seoultech.livingtogether_android.ui.scan

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.bluetooth.viewmodel.ScanViewModel
import com.seoultech.livingtogether_android.databinding.ActivityScanBinding

class ScanActivity : BaseActivity<ActivityScanBinding>(R.layout.activity_scan) {
    private lateinit var scanViewModel: ScanViewModel

    private companion object {
        const val POPPOP_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        const val SCAN_PERIOD = 10000L
        const val REQUEST_ENABLE_BT = 1000
        const val FOUND_DELAY_TIME = 2000L
    }

    private var isFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scanViewModel = obtainViewModel().apply {
            duplicateEvent.observe(this@ScanActivity, Observer {
                if (it) {
                    Toast.makeText(application, getString(R.string.scan_duplicated_sensor), Toast.LENGTH_SHORT).show()
                    finish()
                }
            })

            foundSensorEvent.observe(this@ScanActivity, Observer {
                if (it) {
                    isFound = true
                    setFoundedView()
                    startActivityWithDelay()
                }
            })

            timeOutEvent.observe(this@ScanActivity, Observer {
                if (it) {
                    showScanFailDialog()
                }
            })

            backKeyEvent.observe(this@ScanActivity, Observer {
                if (it) {
                    showBackDialog()
                }
            })

            bluetoothIsNotAvailableEvent.observe(this@ScanActivity, Observer {
                if (it) {
                    Toast.makeText(application, getString(R.string.scan_bluetooth_is_not_available), Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }

        scanViewModel.checkBluetoothAvailable()

        binding.run {
            viewModel = scanViewModel
            setAnimation(R.raw.findsensor, true)
        }
    }

    override fun onResume() {
        super.onResume()
        //블루투스가 활성화 되어있는지 확인. 비활성화 되어 있으면 블루투스를 켜는 화면으로 이동
        if (scanViewModel.isBluetoothOn()) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)

        } else {
            //블루투스가 켜져있다면 스캔 시작
            scanViewModel.startScan(SCAN_PERIOD)
        }
    }

    private fun showScanFailDialog() {
        AlertDialog.Builder(this@ScanActivity)
            .setTitle("스캔 실패")
            .setMessage("센서를 찾지 못하였습니다.")
            .setPositiveButton("재시도") { dialog, _ ->
                scanViewModel.startScan(SCAN_PERIOD)
                dialog.dismiss()
            }
            .setNegativeButton("종료") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    private fun setAnimation(rawRes: Int, isRepeat: Boolean) {
        var a = Glide.with(this@ScanActivity).asGif()
        if (!isRepeat) {
            a = a.listener(object:
                RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?,
                    target: com.bumptech.glide.request.target.Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
                override fun onResourceReady(resource: GifDrawable, model: Any?,
                    target: com.bumptech.glide.request.target.Target<GifDrawable>, dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {
                    resource.run {
                        setLoopCount(1)
                    }
                    return false
                }
            })
        }
        a.load(rawRes).into(binding.imageviewScanAnimation)
    }

    private fun setFoundedView() {
        setAnimation(R.raw.findsensor_found, false)
        binding.run {
            textviewScanContent.text = getString(R.string.sensor_find_success)
            imagebuttonScanBack.visibility = View.GONE
        }
    }

    private fun startActivityWithDelay() {
        Handler().postDelayed({
            startActivity(Intent(this@ScanActivity, InsertLocationActivity::class.java))
            finish()
        }, FOUND_DELAY_TIME)
    }

    override fun onBackPressed() {
        if (!isFound) {
            showBackDialog()
        }
    }

    private fun showBackDialog() {
        AlertDialog.Builder(this)
            .setTitle("스캔 종료")
            .setMessage("아직 센서를 찾지 못했습니다.\n스캔을 종료하시겠습니까?")
            .setPositiveButton("종료") { dialog: DialogInterface, _: Int ->
                if (isFound) {
                    dialog.dismiss()
                } else {
                    scanViewModel.stopScan()
                    finish()
                }
            }
            .setNegativeButton("취소") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                scanViewModel.startScan(SCAN_PERIOD)
            }
            .show()
    }

    private fun obtainViewModel(): ScanViewModel = obtainViewModel(ScanViewModel::class.java)
}
