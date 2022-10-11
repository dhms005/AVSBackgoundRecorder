package com.github.mylibrary.Notification.UI;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.DeviceUtils;
import com.github.mylibrary.Notification.Push.PushUser;
import com.github.mylibrary.Notification.Server.Server;
import com.github.mylibrary.Notification.Server.ServerConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Dailoge_Option extends Activity {

    TextView tv_title;
    TextView tv_btn1, tv_btn2;


    private DisplayImageOptions options;
    String button1_color, button1_text_color,
            button2_text_color, button2_color, button1_text, button2_text,
            msg, type, title, bgcolor, btn_seprator,
            title_bgcolor, title_text_color, option1, option2, user_field;
    LinearLayout btn_bg, line;
    RelativeLayout main_bg;
    private RadioGroup radioGroup;
    private RadioButton RadioButton1, RadioButton2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.z_notification_gcm_dialog_option);


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_btn1 = (TextView) findViewById(R.id.tv_btn1);
        tv_btn2 = (TextView) findViewById(R.id.tv_btn2);

        btn_bg = (LinearLayout) findViewById(R.id.btn_bg);
        line = (LinearLayout) findViewById(R.id.line);
        main_bg = (RelativeLayout) findViewById(R.id.navigationItem);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        RadioButton1 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton2 = (RadioButton) findViewById(R.id.radioButton2);

        final Bundle b = getIntent().getExtras();
        title_text_color = b.getString("title_text_color");
        button1_color = b.getString("button1_color");
        button1_text_color = b.getString("button1_text_color");
        button2_text_color = b.getString("button2_text_color");
        button2_color = b.getString("button2_color");
        button1_text = b.getString("button1_text");
        button2_text = b.getString("button2_text");
        msg = b.getString("msg");
        type = b.getString("type");

        title = b.getString("title");
        bgcolor = b.getString("bgcolor");
        title = b.getString("title");

        btn_seprator = b.getString("btn_seprator");
        title_bgcolor = b.getString("title_bgcolor");

        option1 = b.getString("option1");
        option2 = b.getString("option2");
        user_field = b.getString("user_field");


        tv_btn1.setText(button1_text);
        tv_btn2.setText(button2_text);
        tv_title.setText(title);

        RadioButton1.setText("" + option1);
        RadioButton2.setText("" + option2);
        RadioButton1.setTextColor(Color.parseColor("#AA000000"));
        RadioButton2.setTextColor(Color.parseColor("#AA000000"));

        //tv_description.setText(msg);
        // Linkify.addLinks(tv_description, Linkify.ALL);


       /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(Integer.parseInt(btnmargin), 0, Integer.parseInt(btnmargin), Integer.parseInt(btnmargin));*/
        // btn_bg.setLayoutParams(params);


        tv_title.setTextColor(Color.parseColor(title_text_color));
        tv_btn1.setTextColor(Color.parseColor(button1_text_color));
        tv_btn2.setTextColor(Color.parseColor(button2_text_color));


        tv_title.setBackgroundColor(Color.parseColor(title_bgcolor));
        tv_btn1.setBackgroundColor(Color.parseColor(button1_color));
        tv_btn2.setBackgroundColor(Color.parseColor(button2_color));

        main_bg.setBackgroundColor(Color.parseColor(bgcolor));

        set_visible();


        tv_btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        tv_btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                if (RadioButton1.isChecked()) {
                    Toast.makeText(Dailoge_Option.this, RadioButton1.getText(), Toast.LENGTH_SHORT).show();

                    final SharedPreferences prefs = Dailoge_Option.this.getSharedPreferences("GCM",
                            Context.MODE_PRIVATE);
                    String api_key = prefs.getString(PushUser.API_KEY, "");

                    Update_Data(api_key, user_field, RadioButton1.getText().toString());
                } else {

                    final SharedPreferences prefs = Dailoge_Option.this.getSharedPreferences("GCM",
                            Context.MODE_PRIVATE);
                    String api_key = prefs.getString(PushUser.API_KEY, "");

                    Update_Data(api_key, user_field, RadioButton2.getText().toString());
                }


               /* SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PushUser.TOKEN, token);
                editor.commit();*/

            }
        });


    }

    private void Update_Data(final String token, final String user_field_name, final String user_field_value) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(Dailoge_Option.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getAbsoluteUrl(ServerConfig.UPDATE_DATA_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    Log.e("response", "" + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

//                        Log.d("response", "" + response);
                    finish();

                } catch (JSONException e) {
//                        Log.e("response", "" + e.getMessage());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                    Log.e("goterror",""+error);
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(PushUser.APP_FIELD_NAME, user_field_name + "");
                params.put(PushUser.APP_FIELD_VALUE, user_field_value + "");
                params.put(PushUser.APP_CODE, PushUser.APP_CODE_VALUE);
                params.put(PushUser.APP_ANDROID_ID, DeviceUtils.getDeviceId(Dailoge_Option.this) + "");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put(PushUser.APP_API_KEY, token);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
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


        if (btn_seprator.equals("1")) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }


    }
}