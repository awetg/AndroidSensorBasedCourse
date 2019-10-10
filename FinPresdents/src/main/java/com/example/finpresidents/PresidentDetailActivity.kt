package com.example.retrofit_adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_president_detail.*

class PresidentDetailActivity : Activity() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"
        const val WIKIPEDIA_URL = "https://fi.wikipedia.org?search="

        fun newIntent(context: Context, president: President): Intent {
            val detailIntent = Intent(context, PresidentDetailActivity::class.java)

            detailIntent.putExtra(EXTRA_TITLE, president.name)
            detailIntent.putExtra(EXTRA_URL, WIKIPEDIA_URL)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_president_detail)

        val title = intent.extras?.getString(EXTRA_TITLE)
        val url = intent.extras?.getString(EXTRA_URL)

        detail_web_view.loadUrl("$url$title")
        detail_web_view.settings.javaScriptEnabled = true
        detail_web_view.settings.allowFileAccess = true
        detail_web_view.webViewClient = WebViewClient()
    }
}
