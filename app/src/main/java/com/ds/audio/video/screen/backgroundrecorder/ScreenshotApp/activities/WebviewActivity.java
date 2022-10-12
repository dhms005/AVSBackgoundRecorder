package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;

import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Screen_Shot_Tab;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.Constants;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static final int STORAGE_PERMISIION_CODE = 108;
    int endPos = 0;
    AppCompatAutoCompleteTextView et_search;
    String extension;
    Bitmap.CompressFormat format;

    public boolean isHasFocus = true;
    ImageView ivCloseRefresh;
    ProgressDialog mProgressDialog;
    String path;
    String[] permsa = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    ProgressBar progressBar;
    int startPos = 0;
    Button takeWVScreenshot;
    WebView webView;

    public void onPermissionsGranted(int i, List<String> list) {
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            DevSpy_Admob_Full_AD_New.getInstance().showInterBack(this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    finish();
                }
            });
        }
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        WebView.enableSlowWholeDocumentDraw();
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView((int) R.layout.activity_webview);
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        methodRequiresTwoPermission();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        WebView webView2 = (WebView) findViewById(R.id.webView);
        this.webView = webView2;
        webView2.setBackgroundColor(getResources().getColor(R.color.bg1));
        this.et_search = (AppCompatAutoCompleteTextView) findViewById(R.id.et_search);
        this.ivCloseRefresh = (ImageView) findViewById(R.id.iv_close_refresh);
        ((FrameLayout) findViewById(R.id.iv_close)).setOnClickListener(this);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.takeWVScreenshot = (Button) findViewById(R.id.takeWVScreenshot);
        this.et_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || i != 66) {
                    return false;
                }
                String trim = WebviewActivity.this.et_search.getText().toString().trim();
                WebviewActivity.this.webView.loadUrl(String.format(Constants.SEARCH_URL, new Object[]{trim}));
                WebviewActivity.this.webView.setBackgroundColor(WebviewActivity.this.getResources().getColor(R.color.white));
                WebviewActivity.this.et_search.setText(String.format(Constants.SEARCH_URL, new Object[]{trim}));
                WebviewActivity.this.focusSearchView(false);
                return true;
            }
        });
        this.et_search.setOnClickListener(this);
        this.et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                boolean unused = WebviewActivity.this.isHasFocus = z;
                if (WebviewActivity.this.isHasFocus) {
                    WebviewActivity.this.ivCloseRefresh.setImageResource(R.drawable.ic_close_gray_24dp);
                } else {
                    WebviewActivity.this.ivCloseRefresh.setImageResource(R.drawable.ic_refresh_gray_24dp);
                }
            }
        });
        webInit();
    }


    public void focusSearchView(boolean z) {
        this.et_search.setCursorVisible(z);
        this.et_search.setFocusable(z);
        this.et_search.setFocusableInTouchMode(z);
        if (!z) {
            try {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.et_search.getWindowToken(), 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void webInit() {
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setMixedContentMode(2);
        this.webView.setScrollBarStyle(33554432);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Log.e("shouldOverrid", "shouldOverrideUrlLoading: ");
                return WebviewActivity.this.m15a(webView, str);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                Log.e("shouldOverrid", "shouldOverrideUrlLoading: 1");
                return WebviewActivity.this.m15a(webView, webResourceRequest.getUrl().toString());
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                boolean unused = WebviewActivity.this.isHasFocus = false;
                WebviewActivity.this.ivCloseRefresh.setImageResource(R.drawable.ic_refresh_gray_24dp);
                WebviewActivity.this.et_search.setText(webView.getUrl());
                WebviewActivity.cursorPos(WebviewActivity.this.et_search);
                WebviewActivity.this.progressBar.setVisibility(View.GONE);
                WebviewActivity.this.preLoad1();
                WebviewActivity.this.preLoad2();
                WebviewActivity.this.preLoad3(true);
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                boolean unused = WebviewActivity.this.isHasFocus = true;
                WebviewActivity.this.ivCloseRefresh.setImageResource(R.drawable.ic_close_gray_24dp);
                WebviewActivity.this.et_search.setText(webView.getUrl());
                WebviewActivity.cursorPos(WebviewActivity.this.et_search);
                WebviewActivity.this.progressBar.setVisibility(View.VISIBLE);
            }
        });
        this.webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                Log.i("onPreExecute", "onDownloadStart: ");
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                WebviewActivity.this.progressBar.setProgress(i);
            }
        });
        showLoading("Saving WebPage screenshot ..");
    }


    public void showLoading(String str) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(str);
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setCancelable(false);
    }


    public void hideLoading() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void cursorPos(AppCompatAutoCompleteTextView appCompatAutoCompleteTextView) {
        appCompatAutoCompleteTextView.setSelection(appCompatAutoCompleteTextView.getText().length());
    }


    /* renamed from: a */
    public boolean m15a(WebView webView2, String str) {
        Log.i("override", str);
        return str != null && !str.startsWith("http://") && !str.startsWith("https://");
    }


    public void preLoad3(Boolean bool) {
        String str = bool.booleanValue() ? "visible" : "hidden";
        WebView webView2 = this.webView;
        webView2.evaluateJavascript("(function() {var indicator_line_start = document.getElementById(\"" + getString(R.string.indicator_start_pos_line_id) + "\");if(indicator_line_start != null) {indicator_line_start.style.visibility = \"" + str + "\";};var indicator_text_start = document.getElementById(\"" + getString(R.string.indicator_start_pos_text_id) + "\");if(indicator_text_start != null) {indicator_text_start.style.visibility = \"" + str + "\";};var indicator_text_end = document.getElementById(\"" + getString(R.string.indicator_end_pos_text_id) + "\");if(indicator_text_end != null) {indicator_text_end.style.visibility = \"" + str + "\";};})();", (ValueCallback) null);
    }


    public void preLoad1() {
        WebView webView2 = this.webView;
        webView2.evaluateJavascript("(function() {var ratio = Math.round(screen.availWidth * window.devicePixelRatio / 4) * 4 / document.body.clientWidth;if(document.getElementById(\"" + getString(R.string.indicator_start_pos_line_id) + "\") != null) {var indicator_line = document.getElementById(\"" + getString(R.string.indicator_start_pos_line_id) + "\");indicator_line.style.top = `${(" + this.startPos + "/ratio)}px`;indicator_line.style.visibility = \"visible\";} else {var line = document.createElement(\"div\");line.id = \"" + getString(R.string.indicator_start_pos_line_id) + "\";line.style.width = window.innerWidth+\"px\";line.style.height = \"2px\";line.style.zIndex = \"99999\";line.style.position = \"absolute\";line.style.top = `${(" + this.startPos + "/ratio)}px`;line.style.left = \"0px\";line.style.background = \"red\";line.style.visibility = \"visible\";document.body.appendChild(line);};if(document.getElementById(\"" + getString(R.string.indicator_start_pos_text_id) + "\") != null) {var indicator_text = document.getElementById(\"" + getString(R.string.indicator_start_pos_text_id) + "\");indicator_text.style.top = `${(" + (this.startPos + 16) + "/ratio)}px`;indicator_text.style.visibility = \"visible\";} else {var text = document.createElement(\"p\");text.id = \"" + getString(R.string.indicator_start_pos_text_id) + "\";text.style.zIndex=\"99999\";var content = document.createTextNode(\"" + getString(R.string.indicator_start_pos) + "\");text.appendChild(content);text.style.position = \"absolute\";text.style.top =`${(" + (this.startPos + 16) + "/ratio)}px`;text.style.right = \"4px\";text.style.color = \"white\";text.style.fontSize = \"14px\";text.style.background = \"#000000\";text.style.opacity = \"0.5\";text.style.padding = \"8px 6px 8px 6px\";text.style.visibility = \"visible\";text.style.pointerEvents = \"none\";document.body.appendChild(text);}})();", (ValueCallback) null);
    }


    public void preLoad2() {
        WebView webView2 = this.webView;
        webView2.evaluateJavascript("(function() {var ratio = Math.round(screen.availWidth * window.devicePixelRatio / 4) * 4 / document.body.clientWidth;if(document.getElementById(\"" + getString(R.string.indicator_end_pos_text_id) + "\") != null) {var indicator_text = document.getElementById(\"" + getString(R.string.indicator_end_pos_text_id) + "\");indicator_text.style.visibility = \"visible\";} else {var text = document.createElement(\"p\");text.id = \"" + getString(R.string.indicator_end_pos_text_id) + "\";text.style.zIndex=\"99999\";var content = document.createTextNode(\"" + getString(R.string.indicator_end_pos) + "\");text.appendChild(content);text.style.position = \"fixed\";text.style.bottom = (16 / ratio) + \"px\";text.style.bottom = \"4px\";text.style.right = \"4px\";text.style.color = \"white\";text.style.fontSize = \"14px\";text.style.background = \"#000000\";text.style.opacity = \"0.5\";text.style.padding = \"8px 6px 8px 6px\";text.style.visibility = \"visible\";text.style.pointerEvents = \"none\";document.body.appendChild(text);}})();", (ValueCallback) null);
    }

    public static float convertDpToPixel(float f, Context context) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.endPos:
                if (this.webView.getUrl() != null && !this.webView.getUrl().isEmpty()) {
                    int scrollY = this.webView.getScrollY() + this.webView.getHeight();
                    this.endPos = scrollY;
                    if (scrollY > this.startPos) {
                        this.mProgressDialog.show();
                        preLoad3(false);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            public void run() {
                                if (AppPref.getImageFormat(WebviewActivity.this).equalsIgnoreCase("JPG")) {
                                    WebviewActivity.this.extension = "jpeg";
                                } else {
                                    WebviewActivity.this.extension = "png";
                                }
                                Bitmap access$600 = WebviewActivity.this.startToEnd();
                                WebviewActivity webviewActivity = WebviewActivity.this;
                                webviewActivity.path = webviewActivity.storeImage("WebFile." + WebviewActivity.this.extension, access$600);
                                WebviewActivity.this.sendData();
                            }
                        }, 300);
                        return;
                    }
                    Log.e("WebivewA", "Failed");
                    return;
                }
            case R.id.et_search:
                focusSearchView(true);
                return;
            case R.id.homeClick:
                onBackPressed();
                return;
            case R.id.iv_close:
                break;
            case R.id.removeStickyElement:
                if (this.webView.getUrl() != null && !this.webView.getUrl().isEmpty()) {
                    WebView webView2 = this.webView;
                    webView2.evaluateJavascript("(function() {  document.body.style.overflow = 'auto';  var i, elements = document.querySelectorAll('body *');  for (i = 0; i < elements.length; i++) {    var pos = getComputedStyle(elements[i]).position;    if ((pos === 'fixed' || pos === 'sticky' || pos === '-webkit-sticky')    && elements[i].id !== \"" + getString(R.string.indicator_end_pos_text_id) + "\") {      elements[i].parentNode.removeChild(elements[i]);    }  }})()", (ValueCallback) null);
                    return;
                }
                return;
            case R.id.startPos:
                if (this.webView.getUrl() != null && !this.webView.getUrl().isEmpty()) {
                    this.startPos = this.webView.getScrollY();
                    startPosCapture();
                    return;
                }
                return;
            case R.id.takeWVScreenshot:
                if (this.webView.getUrl() != null && !this.webView.getUrl().isEmpty()) {
                    this.mProgressDialog.show();
                    preLoad3(false);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public void run() {
                            if (AppPref.getImageFormat(WebviewActivity.this).equalsIgnoreCase("JPG")) {
                                WebviewActivity.this.extension = "jpeg";
                                WebviewActivity.this.format = Bitmap.CompressFormat.JPEG;
                            } else {
                                WebviewActivity.this.extension = "png";
                                WebviewActivity.this.format = Bitmap.CompressFormat.PNG;
                            }
                            WebviewActivity webviewActivity = WebviewActivity.this;
                            Bitmap access$900 = webviewActivity.captureWebViewall(webviewActivity.webView);
                            WebviewActivity webviewActivity2 = WebviewActivity.this;
                            webviewActivity2.path = webviewActivity2.storeImage("WebFile." + WebviewActivity.this.extension, access$900);
                            WebviewActivity.this.sendData();
                        }
                    }, 300);
                    return;
                }
                return;
            default:
                return;
        }
        if (this.et_search.getText().toString().trim().length() <= 0) {
            return;
        }
        if (this.isHasFocus) {
            this.et_search.setText("");
        } else {
            this.webView.reload();
        }
    }


    public void sendData() {

        startActivity(new Intent(this, ResultViewerActivity.class).putExtra("finish", false).putExtra(Constants.EditImageIntentKey, this.path));
        finish();
    }

    private String saveInGallery(Bitmap bitmap) {
        String str = AppPref.getPrefix(this) + "_" + System.currentTimeMillis();
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.extension = "jpeg";
            this.format = Bitmap.CompressFormat.JPEG;
        } else {
            this.extension = "png";
            this.format = Bitmap.CompressFormat.PNG;
        }
//        if (Build.VERSION.SDK_INT >= 29) {
//            try {
//                ContentResolver contentResolver = getContentResolver();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("relative_path", Environment.DIRECTORY_DOWNLOADS + "/" + Constants.FOLDER_NAME + "/");
//                contentValues.put("_display_name", str);
//                contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
//                contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
//                contentValues.put("mime_type", "image/" + this.extension);
//                contentValues.put("is_pending", 0);
//                contentValues.put("bucket_display_name", "/Screenshot");
//                Uri insert = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
//                Log.i("saveImageAndGetURI", "saveImageAndGetURI: " + insert);
//                OutputStream openOutputStream = contentResolver.openOutputStream(insert);
//                bitmap.compress(this.format, AppPref.getImageQuality(this), openOutputStream);
//                this.path = insert.toString();
//                openOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {

        String img_path = Constants.imagePath(WebviewActivity.this);
        File file = new File(img_path);

//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, AppPref.getPrefix(this) + "_" + System.currentTimeMillis() + "." + this.extension);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(this.format, AppPref.getImageQuality(this), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        AppConstants.refreshFiles(this, file2);
        this.path = file2.getAbsolutePath();
//        }
        return this.path;
    }


    public String storeImage(String str, Bitmap bitmap) {
        File file = new File(AppConstants.getCachePath(this), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e2) {
            Log.d("TAG", "Error accessing file: " + e2.getMessage());
        }
        return file.toString();
    }

    private void startPosCapture() {
        WebView webView2 = this.webView;
        webView2.evaluateJavascript("(function() {var ratio = Math.round(screen.availWidth * window.devicePixelRatio / 4) * 4 / document.body.clientWidth;if(document.getElementById(\"" + getString(R.string.indicator_start_pos_line_id) + "\") != null) {var indicator_line = document.getElementById(\"" + getString(R.string.indicator_start_pos_line_id) + "\");indicator_line.style.top = `${(" + this.startPos + "/ratio)}px`;indicator_line.style.visibility = \"visible\";} else {var line = document.createElement(\"div\");line.id = \"" + getString(R.string.indicator_start_pos_line_id) + "\";line.style.width = window.innerWidth+\"px\";line.style.height = \"2px\";line.style.zIndex = \"99999\";line.style.position = \"absolute\";line.style.top = `${(" + this.startPos + "/ratio)}px`;line.style.left = \"0px\";line.style.background = \"red\";line.style.visibility = \"visible\";document.body.appendChild(line);};if(document.getElementById(\"" + getString(R.string.indicator_start_pos_text_id) + "\") != null) {var indicator_text = document.getElementById(\"" + getString(R.string.indicator_start_pos_text_id) + "\");indicator_text.style.top = `${(" + (this.startPos + 16) + "/ratio)}px`;indicator_text.style.visibility = \"visible\";} else {var text = document.createElement(\"p\");text.id = \"" + getString(R.string.indicator_start_pos_text_id) + "\";text.style.zIndex=\"99999\";var content = document.createTextNode(\"" + getString(R.string.indicator_start_pos) + "\");text.appendChild(content);text.style.position = \"absolute\";text.style.top =`${(" + (this.startPos + 16) + "/ratio)}px`;text.style.right = \"4px\";text.style.color = \"white\";text.style.fontSize = \"14px\";text.style.background = \"#000000\";text.style.opacity = \"0.5\";text.style.padding = \"8px 6px 8px 6px\";text.style.visibility = \"visible\";text.style.pointerEvents = \"none\";document.body.appendChild(text);}})();", (ValueCallback) null);
    }


    public Bitmap startToEnd() {
        Bitmap createBitmap = Bitmap.createBitmap(this.webView.getMeasuredWidth(), this.endPos - this.startPos, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate(0.0f, (float) (-this.startPos));
        this.webView.draw(canvas);
        return createBitmap;
    }


    public Bitmap captureWebViewall(WebView webView2) {
        try {
            webView2.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            webView2.layout(0, 0, webView2.getMeasuredWidth(), webView2.getMeasuredHeight());
            Bitmap createBitmap = Bitmap.createBitmap(webView2.getMeasuredWidth(), webView2.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(createBitmap, 0.0f, (float) createBitmap.getHeight(), new Paint());
            webView2.draw(canvas);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AfterPermissionGranted(108)
    private void methodRequiresTwoPermission() {
        if (Build.VERSION.SDK_INT < 29 && !EasyPermissions.hasPermissions(this, this.permsa)) {
            EasyPermissions.requestPermissions((Activity) this, getString(R.string.camera_and_location_rationale), 108, this.permsa);
        } else if (Screen_Shot_Tab.getInstance() != null) {
            Screen_Shot_Tab.getInstance().isCheckPermission = false;
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list) || EasyPermissions.somePermissionDenied((Activity) this, this.permsa)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 16061) {
            methodRequiresTwoPermission();
        }
    }


    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_adpative_banner_Ad(this, mAdView);
                }
            }
        } else {
            custom_banner_ad.reload_applovin_banner_ad(this, mAdView);
        }

    }

    private void mNativeBanner() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1") || SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_banner_1);
            findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.mNativeBannerAd).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        } else {
            new Custom_NativeAd_Admob().showAppLovinNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        }

    }
}
