package com.example.barcodescanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Geri butonunu etkinleştirme
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // URL'yi yükleme
        String url = getIntent().getStringExtra("url");
        myWebView.loadUrl(url);
    }

    // Geri butonuna tıklanınca WebView sayfasını kapatma işlemi
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Activity'yi kapat
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
