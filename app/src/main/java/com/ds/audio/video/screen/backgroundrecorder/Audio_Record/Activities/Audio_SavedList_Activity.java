package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Adapter.AudioRecordAdapterGrid;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Adapter.AudioRecordAdapterList;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Model.AudioRecordModel;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_ActivityHomeMenu;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Model.VideoRecordModel;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Audio_SavedList_Activity extends AppCompatActivity {
    RecyclerView rvVideos;
    RecyclerView rvVideos_grid;
    Toolbar toolbar;
    private AudioRecordAdapterList adapter;
    private AudioRecordAdapterGrid adapterGrid;
    private List<AudioRecordModel> listVideo;
    TextView noVideoText;
    ImageView imgViewChnage;

    private File[] listFile = null;
    private ArrayList<String> photosTemp = new ArrayList<>();

    public static boolean check_deleted = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.audio_savedlist_activity);

        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgViewChnage = findViewById(R.id.imgViewChnage);

        rvVideos = (RecyclerView) findViewById(R.id.rv_list_videos);
        rvVideos_grid = (RecyclerView) findViewById(R.id.rv_list_videos_grid);
        noVideoText = (TextView) findViewById(R.id.noVideoText);
        toolbar = (Toolbar) findViewById(R.id.toolbar_folder_activity);
        setSupportActionBar(this.toolbar);

        setupRecylerView();

        imgViewChnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Audio_SavedList_Activity.this, "Click", Toast.LENGTH_SHORT).show();
                if (rvVideos.getVisibility() == View.VISIBLE) {
                    rvVideos.setVisibility(View.GONE);
                    imgViewChnage.setImageResource(R.drawable.ic_video_list);
                    rvVideos_grid.setVisibility(View.VISIBLE);
                } else {
                    imgViewChnage.setImageResource(R.drawable.ic_video_grid);
                    rvVideos.setVisibility(View.VISIBLE);
                    rvVideos_grid.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupRecylerView() {
        this.listVideo = new ArrayList();

        this.adapter = new AudioRecordAdapterList(this, this.listVideo);
        this.adapterGrid = new AudioRecordAdapterGrid(this, this.listVideo);

        this.rvVideos.setAdapter(this.adapter);
        this.rvVideos.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.rvVideos.setHasFixedSize(true);
        rvVideos.setNestedScrollingEnabled(false);

        this.rvVideos_grid.setAdapter(this.adapterGrid);
        this.rvVideos_grid.setLayoutManager(new GridLayoutManager(this, 2));
        this.rvVideos_grid.setHasFixedSize(true);
        rvVideos_grid.setNestedScrollingEnabled(false);

        this.adapter.setItemClickCallBack(new AudioRecordAdapterList.ItemClickCallBack() {
            @Override
            public void onImageClick(View view, final int i) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(Audio_SavedList_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent2 = new Intent(Audio_SavedList_Activity.this, Audio_SaveAudioActivity.class);
                        intent2.putExtra("videourl", ((AudioRecordModel) Audio_SavedList_Activity.this.listVideo.get(i)).getFilePath());
                        Audio_SavedList_Activity.this.startActivity(intent2);
                    }
                });
            }

            @Override
            public void onMenuClick(int menuItem, int i) {
                onAdadapterClick(menuItem, i);
            }

            @Override
            public void onChkBoxClick(View view, int i, boolean z) {
                ((AudioRecordModel) Audio_SavedList_Activity.this.listVideo.get(i)).setSelect(z);
            }
        });

        this.adapterGrid.setItemClickCallBack(new AudioRecordAdapterGrid.ItemClickCallBack() {
            @Override
            public void onImageClick(View view, final int i) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(Audio_SavedList_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {

                        Intent intent2 = new Intent(Audio_SavedList_Activity.this, Audio_SaveAudioActivity.class);
                        intent2.putExtra("videourl", ((AudioRecordModel) Audio_SavedList_Activity.this.listVideo.get(i)).getFilePath());
                        Audio_SavedList_Activity.this.startActivity(intent2);

                    }
                });
            }

            @Override
            public void onMenuClick(int menuItem, int i) {
                onAdadapterClick(menuItem, i);
            }

            @Override
            public void onChkBoxClick(View view, int i, boolean z) {
                ((AudioRecordModel) Audio_SavedList_Activity.this.listVideo.get(i)).setSelect(z);
            }
        });

        new LoadVideo().execute(Audio_FileHelper.videoPath(Audio_SavedList_Activity.this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void onAdadapterClick(int itemId, int selected_item) {

        Log.e("all ", "" + selected_item);
        if (itemId != R.id.action_delete) {
            switch (itemId) {
                case R.id.action_rename:


                    break;

                case R.id.action_select_all:
                    for (AudioRecordModel cCTVVideoRecord : this.listVideo) {
                        cCTVVideoRecord.setSelect(true);
                    }
                    this.adapter.notifyDataSetChanged();
                    this.adapterGrid.notifyDataSetChanged();
                    break;
                case R.id.action_share:
                    Audio_FileHelper.shareVideo(this, this.listVideo.get(selected_item).getFilePath());
                    break;

            }
        } else {
            AlertDialog.Builder message3 = new AlertDialog.Builder(this).setCancelable(true).setTitle(getString(R.string.confirm)).setMessage(getString(R.string.do_you_want_delete));
            message3.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            message3.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {

                    deleteFile(listVideo.get(selected_item).getFilePath());
                    listVideo.remove(selected_item);
                    adapter.notifyDataSetChanged();
                    adapterGrid.notifyDataSetChanged();

                }
            });
            message3.create().show();
        }

    }

    public boolean deleteFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.delete()) {
//            removeAt(i);
            Toast.makeText(Audio_SavedList_Activity.this, "Delete Success", Toast.LENGTH_SHORT).show();
            Audio_SavedList_Activity.check_deleted = true;
            return true;
        }
        Toast.makeText(Audio_SavedList_Activity.this, "Delete Failed", Toast.LENGTH_SHORT).show();

        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LoadVideo extends AsyncTask<String, Void, Void> {
        static final boolean $assertionsDisabled = false;
        ProgressDialog dialog;

        LoadVideo() {
        }

        public void onPreExecute() {
            this.dialog = new ProgressDialog(Audio_SavedList_Activity.this);
            this.dialog.setMessage(Audio_SavedList_Activity.this.getString(R.string.loading));
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        public Void doInBackground(String... strArr) {


//            listVideo.add()
//            LoadVideo loadVideo = this;
            String str = strArr[0];


            listVideo.clear();
            File file = new File(str);
            if (file.isDirectory()) {
                listFile = file.listFiles();
                Arrays.sort(listFile, new Comparator() {
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
                    File[] fileArr = listFile;
                    if (i >= fileArr.length) {
                        break;
                    }
                    if (fileArr[i].getName().contains(".3gp")) {
                        photosTemp.add(listFile[i].getAbsolutePath());
                    } else {
                        listFile[i].delete();
                    }
                    i++;
                }
                for (int i2 = 0; i2 < photosTemp.size(); i2++) {
//                    photos.add(photosTemp.get(i2));
                    String filename = photosTemp.get(i2).substring(photosTemp.get(i2).lastIndexOf("/") + 1);
                    Bitmap bitmap = BitmapFactory.decodeFile(listFile[i2].getAbsolutePath());
                    listVideo.add(new AudioRecordModel(photosTemp.get(i2), bitmap, "", filename, ""));
                }
            }
            return null;
        }

        public void onPostExecute(Void r1) {
            super.onPostExecute((Void) r1);
            ProgressDialog progressDialog = this.dialog;
            check_deleted = false;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (listVideo.size() > 0) {
                noVideoText.setVisibility(View.GONE);
                rvVideos.setVisibility(View.VISIBLE);
                Audio_SavedList_Activity.this.adapter.notifyDataSetChanged();
                Audio_SavedList_Activity.this.adapterGrid.notifyDataSetChanged();
            } else {
                noVideoText.setVisibility(View.VISIBLE);
                rvVideos.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void onBackPressed() {

        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");

        if (check_deleted) {
            Intent intent = new Intent(Audio_SavedList_Activity.this, Audio_SavedList_Activity.class);
            startActivity(intent);
            finish();
//            setupRecylerView();
        }
    }


    @Override
    protected void onPause() {
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
        super.onPause();
    }

    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_11);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            }
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
