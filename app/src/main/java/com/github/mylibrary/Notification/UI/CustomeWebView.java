package com.github.mylibrary.Notification.UI;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Database.NoZoomControllWebView;


public class CustomeWebView extends AppCompatActivity{

	public NoZoomControllWebView webView;
	ProgressDialog mProgressDialog;

	ProgressDialog mSpinner;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
							View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
		setContentView(R.layout.z_notification_webview);

		mProgressDialog=new ProgressDialog(CustomeWebView.this);

		mProgressDialog.setMessage("Loading...");
		webView = new NoZoomControllWebView(this);
		webView = (NoZoomControllWebView) findViewById(R.id.webView1);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new CustomClient());

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCachePath("/data/data/"+ getPackageName() +"/cache");
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		JsHandler _jsHandler = new JsHandler(this, webView);
		webView.addJavascriptInterface(_jsHandler, "JsHandler");
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		String url;
		try{
		Bundle where = getIntent().getExtras();
		url = where.getString("link1");
		}catch (NullPointerException e) {
			url="";
		}
		int check_afiliate = 1;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (url.contains("flipkart")) {
			i.setPackage("com.flipkart.android");
		} else if (url.contains("amzn") || url.contains("amazon")) {
			i.setPackage("in.amazon.mShop.android.shopping");
		} else if (url.contains("snapdeal")) {
			i.setPackage("com.snapdeal.main");
		} else {
			webView.loadUrl(url);
			check_afiliate = 0;
		}
		if (check_afiliate == 1) {
			try {

				startActivity(i);
				finish();
			} catch (ActivityNotFoundException e) {
				// Chrome is probably not installed
				// Try with the default browser
				webView.loadUrl(url);
			}
		}
//		AdView adView = (AdView) findViewById(R.id.adView);
//		if (getResources().getString(R.string.ad_unit_id).length() == 0) {
//			adView.setVisibility(View.GONE);
//		} else {
//			adView.setVisibility(View.VISIBLE);
//			AdRequest adRequest = new AdRequest.Builder()
//					.addTestDevice("5AD47C03FEA90E3222051A8F076F8976")
//					.addTestDevice("3A56423DE328DF0B2B09E6B157C2CC32").build();
//			adView.loadAd(adRequest);
//		}

	}
	private class CustomClient extends WebViewClient {


		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// Log.d(TAG, "Loading URL: " + url);

			mProgressDialog.show();

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mProgressDialog.cancel();

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);

		}

		 @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if (Uri.parse(url).getScheme().equals("market")) {
		            try {
		                Intent intent = new Intent(Intent.ACTION_VIEW);
		                intent.setData(Uri.parse(url));
		                Activity host = (Activity) view.getContext();
		                host.startActivity(intent);
		                return true;
		            } catch (ActivityNotFoundException e) {
		                // Google Play app is not installed, you may want to open the app store link
		                Uri uri = Uri.parse(url);
		                view.loadUrl("http://play.google.com/store/apps/" + uri.getHost() + "?" + uri.getQuery());
		                return false;
		            }

		        }
		        return false;
		    }


}

	public void onBackPressed() {
		if (webView.isFocused() && webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.webview_action, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int i = item.getItemId();
		if (i == R.id.action_reload) {
			Toast.makeText(getApplicationContext(), "Reload page...",
					Toast.LENGTH_SHORT).show();
			webView.reload();

		} else if (i == R.id.action_exit) {
			finish();

		} else if (i == R.id.action_back) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	class JsHandler {
		Activity activity;
		String TAG = "JsHandler";
		WebView webView;

		public JsHandler(Activity _contxt, WebView _webView) {
			activity = _contxt;
			webView = _webView;
		}

		/**
		 * This function handles call from JS
		 */
		@JavascriptInterface
		public void initVideo() {
			webView.loadUrl("javascript:playVideo()");
		}

		public void initAudio() {
			webView.loadUrl("javascript:playAudio()");
		}

		/**
		 * This function handles call from Android-Java
		 */
		public void javaFnCall(String jsString) {

			final String webUrl = "javascript:diplayJavaMsg('" + jsString + "')";
			// Add this to avoid android.view.windowmanager$badtokenexception unable to add window
			if (!activity.isFinishing())
				// loadurl on UI main thread
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						webView.loadUrl(webUrl);
					}
				});
		}
	}
}
