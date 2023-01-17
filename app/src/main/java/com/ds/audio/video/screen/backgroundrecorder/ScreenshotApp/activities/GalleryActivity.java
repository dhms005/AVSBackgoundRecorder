package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.AllImageAdapter;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.DiaryImageData;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.Constants;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityGalleryBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    public static AllImageAdapter allImageAdapter;
    public static ArrayList<DiaryImageData> diaryImageData = new ArrayList<>();
    private static GalleryActivity instance;
    BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    ActivityGalleryBinding binding;
    ArrayList<Integer> deletedpos;
    CompositeDisposable disposable = new CompositeDisposable();
    String path;

    private File[] listFile = null;
    private ArrayList<String> photosTemp = new ArrayList<>();

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        ActivityGalleryBinding activityGalleryBinding = (ActivityGalleryBinding) DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        this.binding = activityGalleryBinding;
        instance = this;
        AppPref.setGalleryOpen(this, true);
        setSupportActionBar(this.binding.toolBar);
        this.binding.ivBack.setOnClickListener(this);
        this.deletedpos = new ArrayList<>();
        openDisposal();
         if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
    }

    private void openDisposal() {
        diaryImageData = new ArrayList<>();
        showProgressBar();
        this.disposable.add(Observable.fromCallable(new Callable() {
            public final Object call() {
                try {
                    return GalleryActivity.this.GetAllGalleryActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            public final void accept(Object obj) {
                hideProgressBar();
                setAdapter();
            }
        }));
    }

    public Boolean GetAllGalleryActivity() throws Exception {


        getAllImages();

        return false;
    }


    public static GalleryActivity getInstance() {
        return instance;
    }

    private void setAdapter() {
        allImageAdapter = new AllImageAdapter(this, diaryImageData, new AllImageAdapter.RecyclerClick() {
            public void onClick(int i) {
                Intent intent = new Intent(GalleryActivity.this, FullImageActivity.class);
                intent.putExtra("ImagePosition", i);
                GalleryActivity.this.activityLauncher.launch(intent, new BetterActivityResult.OnActivityResult() {
                    public final void onActivityResult(Object obj) {

                        ActivityResult activityResult = (ActivityResult) obj;
                        Intent data;
                        if (activityResult.getResultCode() == -1 && (data = activityResult.getData()) != null) {
                            GalleryActivity.this.deletedpos = data.getIntegerArrayListExtra("DeletedPosList");
                            if (GalleryActivity.this.deletedpos.size() > 0) {
                                GalleryActivity.allImageAdapter.notifyDataSetChanged();
                                GalleryActivity.this.checkSize();
                            }
                        }

//
                    }
                });
            }


        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.binding.recycler.setLayoutManager(gridLayoutManager);
        this.binding.recycler.setAdapter(allImageAdapter);
        sortList();
        checkSize();
    }

    public void addData(DiaryImageData diaryImageData2) {
        diaryImageData.add(diaryImageData2);
        sortList();
        allImageAdapter.notifyDataSetChanged();
    }

    private void sortList() {
        if (diaryImageData.size() > 1) {
            Collections.sort(diaryImageData, new Comparator<DiaryImageData>() {
                public int compare(DiaryImageData diaryImageData, DiaryImageData diaryImageData2) {
                    return Long.compare(diaryImageData.getDate(), diaryImageData2.getDate());
                }
            });
        }
    }

    public void checkSize() {
        if (diaryImageData.size() > 0) {
            this.binding.recycler.setVisibility(View.VISIBLE);
            this.binding.linNoData.setVisibility(View.GONE);
            return;
        }
        this.binding.recycler.setVisibility(View.GONE);
        this.binding.linNoData.setVisibility(View.VISIBLE);
    }

    private void setViewInteract(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            int i = 0;
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i < viewGroup.getChildCount()) {
                    setViewInteract(viewGroup.getChildAt(i), z);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void showProgressBar() {
        setViewInteract(this.binding.rlContainer, false);
        this.binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        setViewInteract(this.binding.rlContainer, true);
        this.binding.progressBar.setVisibility(View.GONE);
    }

    private void getAllImages() {
//        this.path = Environment.DIRECTORY_DOWNLOADS + "/" + Constants.FOLDER_NAME;
        this.path = Constants.imagePath(GalleryActivity.this);


        diaryImageData.clear();
        File file = new File(path);
        if (file.isDirectory()) {
            this.listFile = file.listFiles();
            Arrays.sort(this.listFile, new Comparator() {
                public int compare(Object obj, Object obj2) {
                    File file = (File) obj;
                    File file2 = (File) obj2;
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                }
            });

            int i = 0;
            while (true) {
                File[] fileArr = this.listFile;
                if (i >= fileArr.length) {
                    break;
                }
                if (fileArr[i].getName().contains(".jpg") || fileArr[i].getName().contains(".png") || fileArr[i].getName().contains(".jpeg")) {
                    this.photosTemp.add(this.listFile[i].getAbsolutePath());
                } else {
                    this.listFile[i].delete();
                }
                i++;
            }


            for (int i2 = 0; i2 < this.photosTemp.size(); i2++) {

                String filename = photosTemp.get(i2).substring(photosTemp.get(i2).lastIndexOf("/") + 1);

                diaryImageData.add(new DiaryImageData(photosTemp.get(i2),filename,0,0));
            }
        }


//        String[] strArr = {"%" + this.path + "%"};
//        Cursor query = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.getContentUri("external"), new String[]{"_id", "bucket_display_name", "bucket_id", "date_modified", "date_added", "_display_name", "_size"}, "relative_path like ? ", strArr, "date_modified DESC");
//        if (query != null && query.getCount() > 0 && query.moveToFirst()) {
//            do {
//                long j = query.getLong(query.getColumnIndex("_size"));
//                String valueOf = String.valueOf(ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), query.getLong(query.getColumnIndex("_id"))));
//                long j2 = query.getLong(query.getColumnIndex("date_added"));
//                diaryImageData.add(new DiaryImageData(valueOf, query.getString(query.getColumnIndex("bucket_display_name")), 1000 * j2, j));
//            } while (query.moveToNext());
//        }
    }

   
//    public void getAllImages() {
//        this.path = FileUtills.rootStoreDir().getAbsolutePath();
//        String[] strArr = {"%" + this.path + "%"};
//        Cursor query = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.getContentUri("external"), new String[]{"_data", "title", "date_added"}, "_data like?", strArr, "date_modified DESC");
//        if (query != null && query.getCount() > 0 && query.moveToFirst()) {
//            do {
//                diaryImageData.add(new DiaryImageData(query.getString(query.getColumnIndexOrThrow("_data")), query.getString(query.getColumnIndex("title")), query.getLong(query.getColumnIndex("date_added")) * 1000));
//            } while (query.moveToNext());
//        }
//    }

    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            AppPref.setGalleryOpen(this, false);
            DevSpy_Admob_Full_AD_New.getInstance().showInterBack(this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    finish();
                }
            });
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
