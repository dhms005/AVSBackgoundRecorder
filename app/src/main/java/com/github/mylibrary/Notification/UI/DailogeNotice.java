package com.github.mylibrary.Notification.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class DailogeNotice extends Activity {

    TextView tv_title, tv_description;
    TextView tv_btn1, tv_btn2;

    ImageView n2_img_icon, n2_img_icon2, img_close;
    private DisplayImageOptions options;
    String button1_color, button1_text_color, msgcolor,
            button2_text_color, button2_color, button1_text, button2_text,
            msg, type, image, link1, link2, title, close_button_color, bgcolor, btnmargin, btn_seprator, img_top,
            title_bgcolor, title_text_color, close_button_icon, close_button_exist, full_image;
    LinearLayout btn_bg, line;
    RelativeLayout main_bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.z_notification_gcm_dialog_images);


        tv_title = (TextView) findViewById(R.id.tv_title);
        n2_img_icon = (ImageView) findViewById(R.id.n2_img_icon);
        n2_img_icon2 = (ImageView) findViewById(R.id.n2_img_icon2);
        img_close = (ImageView) findViewById(R.id.img_close);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_btn1 = (TextView) findViewById(R.id.tv_btn1);
        tv_btn2 = (TextView) findViewById(R.id.tv_btn2);

        btn_bg = (LinearLayout) findViewById(R.id.btn_bg);
        line = (LinearLayout) findViewById(R.id.line);
        main_bg = (RelativeLayout) findViewById(R.id.navigationItem);

        final Bundle b = getIntent().getExtras();
        title_text_color = b.getString("title_text_color");
        button1_color = b.getString("button1_color");
        button1_text_color = b.getString("button1_text_color");
        msgcolor = b.getString("msgcolor");
        button2_text_color = b.getString("button2_text_color");
        button2_color = b.getString("button2_color");
        button1_text = b.getString("button1_text");
        button2_text = b.getString("button2_text");
        msg = b.getString("msg");
        type = b.getString("type");
        image = b.getString("image");
        link1 = b.getString("link1");
        link2 = b.getString("link2");
        title = b.getString("title");
        bgcolor = b.getString("bgcolor");
        close_button_color = b.getString("close_button_color");
        title = b.getString("title");

        btnmargin = b.getString("btnmargin");
        btn_seprator = b.getString("btn_seprator");
        img_top = b.getString("img_top");
        close_button_icon = b.getString("close_button_icon");
        title_bgcolor = b.getString("title_bgcolor");
        close_button_exist = b.getString("close_button_exist");
        full_image = b.getString("full_image");
        tv_btn1.setText(button1_text);
        tv_btn2.setText(button2_text);
        tv_title.setText(title);
        tv_description.setText(msg);
        Linkify.addLinks(tv_description, Linkify.ALL);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);

        try {
            if (img_top.equals("1")) {
                ImageLoader.getInstance().displayImage(image, n2_img_icon);
            } else {
                ImageLoader.getInstance().displayImage(image, n2_img_icon2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_title.setTextColor(Color.parseColor(title_text_color));
        tv_description.setTextColor(Color.parseColor(msgcolor));
        tv_btn1.setTextColor(Color.parseColor(button1_text_color));
        tv_btn2.setTextColor(Color.parseColor(button2_text_color));


        tv_title.setBackgroundColor(Color.parseColor(title_bgcolor));
        tv_btn1.setBackgroundColor(Color.parseColor(button1_color));
        tv_btn2.setBackgroundColor(Color.parseColor(button2_color));

        img_close.setBackgroundColor(Color.parseColor(close_button_color));
        main_bg.setBackgroundColor(Color.parseColor(bgcolor));

        set_visible();


        tv_btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (link2.startsWith("http://") || link1.startsWith("https://")) {
                    Intent intent = new Intent(getApplicationContext(), CustomeWebView.class);

                    intent.putExtra("link1", link2);
                    startActivity(intent);
                } else if (link2.startsWith("com")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=" + link2));
                    startActivity(i);
                } else {
                    finish();
                }
            }
        });

        tv_btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (link1.startsWith("http://") || link1.startsWith("https://")) {
                    Intent intent = new Intent(getApplicationContext(), CustomeWebView.class);

                    intent.putExtra("link1", link1);
                    startActivity(intent);
                } else if (link1.startsWith("com")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=" + link1));
                    startActivity(i);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Notification_MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        img_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }

    public void set_visible() {
        if (button1_text.equals("")) {
            tv_btn1.setVisibility(View.GONE);
        }
        if (button2_text.equals("")) {
            tv_btn2.setVisibility(View.GONE);
        }
        if (title.equals("")) {
            tv_title.setVisibility(View.GONE);
        }
        if (msg.equals("")) {
            tv_description.setVisibility(View.GONE);
        }
        if (image.equals("")) {
            n2_img_icon.setVisibility(View.GONE);
            n2_img_icon2.setVisibility(View.GONE);
        }
        if (btn_seprator.equals("1")) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
        if (img_top.equals("1")) {
            n2_img_icon.setVisibility(View.VISIBLE);
            n2_img_icon2.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(image, n2_img_icon);
        } else {
            ImageLoader.getInstance().displayImage(image, n2_img_icon2);
            n2_img_icon.setVisibility(View.GONE);
            n2_img_icon2.setVisibility(View.VISIBLE);
        }

        if (close_button_icon.equals("1")) {
            Bitmap close_icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.z_black_close);
            img_close.setImageBitmap(close_icon);
        }
        if (close_button_exist.equals("1")) {
            img_close.setVisibility(View.VISIBLE);
        } else {
            img_close.setVisibility(View.GONE);
        }

        if (full_image.equals("1")) {
            n2_img_icon.setScaleType(ImageView.ScaleType.FIT_XY);
            n2_img_icon2.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            img_close.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);
            n2_img_icon.setLayoutParams(params);
            //n2_img_icon2.setLayoutParams(params);
        }

    }
}