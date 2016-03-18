package com.example.mrmean.hakomataaa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        WebView mapView = (WebView)findViewById(R.id.webView);
        mapView.setWebViewClient(new MyBrowser());
        mapView.loadUrl("http://innovatepp.com/hakomata/map2.php");

    }
}

 class MyBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}