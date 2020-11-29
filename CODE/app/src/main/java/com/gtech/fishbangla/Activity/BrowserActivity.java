package com.gtech.fishbangla.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;

public class BrowserActivity extends AppCompatActivity {
    Utility utility = new Utility(this);
    ImageView ivBack;
    TextView tvTitle;
    WebView wvAll;
    String url;
    String tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        url = getIntent().getExtras().getString("url");
        if (getIntent().hasExtra("tag")) {
            tag = getIntent().getExtras().getString("tag");
        }
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        wvAll = (WebView) findViewById(R.id.wv_all);

        WebSettings webSettings = wvAll.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        wvAll.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvAll.loadUrl(url);
        wvAll.setWebViewClient(new MyWebViewClient());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wvAll.addJavascriptInterface(new WebAppInterface(this), "Android");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!tag.equals("ONLINE")) {
                utility.showProgress(true, "");
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!tag.equals("ONLINE")) {
                utility.hideProgress();
            }
        }
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void fishbanglaPaymentStatus(String message) {
            if (message.equalsIgnoreCase("200")) {
                Toast.makeText(mContext, "Thanks for your payment", Toast.LENGTH_SHORT).show();
                //send to cart activity
                sendBroadcast(new Intent("online.payment.check"));
                finish();
            } else {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvAll.canGoBack()) {
            wvAll.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
