package com.example.test_myrealtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_news_web_view.*
import kotlinx.android.synthetic.main.layout_item_list.*

class NewsWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)

        val link = intent.getStringExtra("link")
        val title = intent.getStringExtra("title")
        val keywords = intent.getStringArrayListExtra("keywords")

        news_title.text=title
        news_keyword1.text=keywords[0]
        news_keyword2.text=keywords[1]
        news_keyword3.text=keywords[2]

        webView.webViewClient = WebViewClient()

        with(webView.settings){
            javaScriptEnabled = true
            setSupportMultipleWindows(true)
            javaScriptCanOpenWindowsAutomatically = false
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls=false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            cacheMode=WebSettings.LOAD_NO_CACHE
            domStorageEnabled=true
        }

        webView.loadUrl(link)

    }
}
