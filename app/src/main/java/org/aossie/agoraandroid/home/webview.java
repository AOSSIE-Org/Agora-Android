package org.aossie.agoraandroid.home;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.aossie.agoraandroid.R;

public class webview extends AppCompatActivity {
  WebView webview;
  boolean b;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);

    b = true;
    webview = (WebView) findViewById(R.id.webview);

    if (ContactUsFragment.opt == 1) {
      webview.setWebViewClient(new WebViewClient());
      webview.loadUrl("https://gitter.im/aossie/home");
      webview.getSettings().setJavaScriptEnabled(b);
    } else if (ReportBugFragment.opt == 3 && ContactUsFragment.opt == 0) {
      webview.setWebViewClient(new WebViewClient());
      webview.loadUrl("https://gitlab.com/aossie/agora-android/issues/new");
      webview.getSettings().setJavaScriptEnabled(b);

    } else if (ContactUsFragment.opt == 2) {
      webview.setWebViewClient(new WebViewClient());
      webview.loadUrl("https://gitlab.com/aossie");
      webview.getSettings().setJavaScriptEnabled(b);

    }
  }

  @Override
  public void onBackPressed() {
    if (webview.canGoBack()) {
      webview.goBack();
    } else {
      super.onBackPressed();
    }
    ContactUsFragment.opt = 0;
  }
}