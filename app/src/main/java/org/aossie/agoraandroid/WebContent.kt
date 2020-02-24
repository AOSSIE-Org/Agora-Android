package org.aossie.agoraandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_content.*

class WebContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_content)

        webview1.loadUrl(getIntent().getStringExtra("url"))

        webview1.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val name = webview1.getTitle()
                title1.setText(name)
            }
        })

        back1.setOnClickListener {
            super.onBackPressed()
        }


    }
}
