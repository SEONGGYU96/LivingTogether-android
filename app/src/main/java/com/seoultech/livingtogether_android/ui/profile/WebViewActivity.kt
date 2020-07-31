package com.seoultech.livingtogether_android.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityWebViewBinding
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : BaseActivity<ActivityWebViewBinding>(R.layout.activity_web_view) {

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView_postalcode.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    this@run.loadUrl("javascript:daumPostalCode();")
                }
            }

            loadUrl("file:///android_asset/KakaoPostalCode.html");
        }
    }
}