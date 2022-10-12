package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_Constant;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_SharedPrefrencesApp;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;


public class DevSpy_PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    DevSpy_SharedPrefrencesApp preferences;
    private LinearLayout mAdView;
    private String mediation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.devspy_privacy_policy_activity);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        // finally change the color
        if (Build.VERSION.SDK_INT >= 23)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        mDeclaration();
    }

    private void mDeclaration() {

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Privacy Policy");
        preferences = new DevSpy_SharedPrefrencesApp(DevSpy_PrivacyPolicyActivity.this);
        preferences.setPreferences(preferences.Check_ID, "1");

//        AdView mAdViewFb1 = new AdView(this, getString(R.string.Fb_banner_Ad), AdSize.BANNER_HEIGHT_50);
//        // Find the Ad Container
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//        // Add the ad view to your activity layout
//        adContainer.addView(mAdViewFb1);
//        // Request an ad
//        mAdViewFb1.loadAd();


//       banner_ad = new Custom_Banner_Ad();
//        mAdView=(LinearLayout)findViewById(R.id.banner_container);
//        mediation = SharePrefUtils.getString(Constant_ad.MEDIATION, "0");
//        if (mediation.equals("0")) {
//            banner_ad.reload_admob_banner_Ad(this,mAdView);
////            load_admob_full_ad();
//        } else {
//            banner_ad.reload_fb_banner_Ad(this,mAdView);
////            load_fb_full_ad();
//        }


        url = DevSpy_Constant.PRIVACY_POLICY   ;

        webView = (WebView) findViewById(R.id.webView_privacy);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClientDemo());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        progressBar = (ProgressBar) findViewById(R.id.prgBar_privacy);

        callUrl();
    }

    private void callUrl() {

        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }

    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onBackPressed() {



        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        preferences.setPreferences(preferences.Check_ID, "0");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        preferences.setPreferences(preferences.Check_ID, "1");
    }

    @Override
    protected void onDestroy() {
        preferences.setPreferences(preferences.Check_ID, "2");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
