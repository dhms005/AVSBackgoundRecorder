package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Audio_Record_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Extra_Main;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.ScreenRecord_Record_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Video_Record_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Screen_Shot_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Tools_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Utils.CustomViewPager;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_PrivacyPolicyActivity;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class DevSpy_ActivityHomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static TabLayout tabLayout;
    protected CustomViewPager pager;
    private ViewPagerAdapter adapter;
    Context context;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    ImageView drawer_home, drawer_premium;
    TextView drawer_title;
    Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.activity_home_menu);

        context = DevSpy_ActivityHomeMenu.this;
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer_home = findViewById(R.id.drawer_home);
        drawer_premium = findViewById(R.id.drawer_premium);
        drawer_title = findViewById(R.id.drawer_title);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        pager = findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(5);
        pager.setPagingEnabled(false);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        pager.setCurrentItem(2);

        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }


        drawer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isOpen()) {
                    drawerLayout.close();
                } else {
                    drawerLayout.open();
                }
            }
        });
        drawer_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevSpy_ActivityHomeMenu.this, ExploreActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dv_rateUs:
                // User chose the "Settings" item, show the app settings UI...
                Bundle params = new Bundle();
                params.putString("mRateApp", "mRateApp");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                return true;

            case R.id.dv_share:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle params1 = new Bundle();
                params1.putString("mShareApp", "mShareApp");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                startActivity(Intent.createChooser(share, "Share link!"));

                return true;

            case R.id.dv_privacyPolicy:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle params2 = new Bundle();
                params2.putString("mTextPrivacyPolicy", "mTextPrivacyPolicy");
                if (!DevSpy_Utility.isConnectivityAvailable(DevSpy_ActivityHomeMenu.this)) {
                    Toast.makeText(DevSpy_ActivityHomeMenu.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(DevSpy_ActivityHomeMenu.this, DevSpy_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
                return true;

            case R.id.dv_faq:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent intent = new Intent(DevSpy_ActivityHomeMenu.this, DevSpy_FaqActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {


        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new ScreenRecord_Record_Tab();
                    break;

                case 1:
                    result = new Audio_Record_Tab();
                    break;

                case 2:
                    result = new Video_Record_Tab();
                    break;

                case 3:
                    result = new Screen_Shot_Tab();
                    break;

                case 4:
                    result = new Tools_Tab();
                    break;

                default:
                    result = new Extra_Main();
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            registeredFragments.remove(position);

            super.destroyItem(container, position, object);

        }


        public Fragment getRegisteredFragment(int position) {

            return registeredFragments.get(position);

        }


    }

    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        TextView title1 = view1.findViewById(R.id.text);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screen_s));
        title1.setText("Screen Recorder");
        title1.setTextColor(context.getResources().getColor(R.color.white));
        tabLayout.getTabAt(0).setCustomView(view1);


        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        TextView title2 = view2.findViewById(R.id.text);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_audiorecord));
//        imageView2.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title2.setText("Audio Recorder");
        title2.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
        tabLayout.getTabAt(1).setCustomView(view2);


        View view3 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);


        ImageView imageView3 = view3.findViewById(R.id.image);
        TextView title3 = view3.findViewById(R.id.text);
        imageView3.setImageDrawable(null);
//        imageView2.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title3.setText("");
        title3.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
        tabLayout.getTabAt(2).setCustomView(view3);


        View view4 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView4 = view4.findViewById(R.id.image);
        TextView title4 = view4.findViewById(R.id.text);
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screenshot));
//        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title4.setText("Screenshot");
        title4.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
        tabLayout.getTabAt(3).setCustomView(view4);


        View view5 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView5 = view5.findViewById(R.id.image);
        TextView title5 = view5.findViewById(R.id.text);
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_tools));
//        imageView5.setColorFilter(ContextCompat.getColor(context, R.color.colorwhite_50), android.graphics.PorterDuff.Mode.SRC_IN);
        title5.setText("Tools");
        title5.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
        tabLayout.getTabAt(4).setCustomView(view5);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        //  Functions.blackStatusBar(getActivity());

//                        onHomeClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screen_s));
                        title.setTextColor(context.getResources().getColor(R.color.white));
                        drawer_title.setText("Screen Recorder");
                        break;

                    case 1:


//                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_audiorecord_s));
//                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.white));
                        drawer_title.setText("Audio Recorder");
                        break;


                    case 2:

                        drawer_title.setText("Video Recorder");
                        break;

                    case 3:


//                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screenshot_s));
//                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.white));

                        drawer_title.setText("Screen Shot");
                        break;
                    case 4:

//                        onotherTabClick();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_tools_s));
//                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.white));
                        drawer_title.setText("Tools");
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screen));
                        title.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_audiorecord));
                        title.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
                        break;

                    case 2:
                        image.setImageDrawable(null);
                        title.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
                        break;

                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_screenshot));
                        title.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
                        break;
                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_menu_tools));
                        title.setTextColor(context.getResources().getColor(R.color.tab_unselected_color));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

  //  @Override
  /*  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.dv_rateUs:
                // User chose the "Settings" item, show the app settings UI...
                Bundle params = new Bundle();
                params.putString("mRateApp", "mRateApp");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                return true;

            case R.id.dv_share:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle params1 = new Bundle();
                params1.putString("mShareApp", "mShareApp");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                startActivity(Intent.createChooser(share, "Share link!"));

                return true;

            case R.id.dv_privacyPolicy:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle params2 = new Bundle();
                params2.putString("mTextPrivacyPolicy", "mTextPrivacyPolicy");
                if (!CY_M_Utility.isConnectivityAvailable(CY_M_ActivityHomeMenu.this)) {
                    Toast.makeText(CY_M_ActivityHomeMenu.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(CY_M_ActivityHomeMenu.this, CY_M_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
                return true;

            case R.id.dv_faq:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent intent = new Intent(CY_M_ActivityHomeMenu.this, CY_M_FaqActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }*/

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}