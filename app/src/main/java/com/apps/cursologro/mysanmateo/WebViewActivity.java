package com.apps.cursologro.mysanmateo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView myWebView = (WebView) this.findViewById(R.id.webView);
        String url = Listado.listado.getLinkWebView();
        myWebView.loadUrl(url);
        //myWebView.loadUrl("https://google.es");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("Estamos en onBackPressed de WebViewActivty");
        this.finish();
    }
}
