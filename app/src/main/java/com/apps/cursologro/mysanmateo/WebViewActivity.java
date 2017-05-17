package com.apps.cursologro.mysanmateo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    Boolean back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate de WebView Activity");
        setContentView(R.layout.activity_web_view);

        //Utilizamos back para saber cuando estamos volviendo tras mostrar la url
        back = false;

        WebView myWebView = (WebView) this.findViewById(R.id.webView);
        String url = Listado.listado.getLinkWebView();
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);
        //myWebView.loadUrl("https://google.es");

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart de WebView Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        back = true;
        System.out.println("onStop de WebView Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy de WebView Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume de WebView Activity");
        if (back) {this.finish();}

    }
}
