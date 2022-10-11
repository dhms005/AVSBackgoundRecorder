package com.github.mylibrary.Notification.UI;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;


public class CategorySelectionActivity extends AppCompatActivity {

    ListView listView;

    //CategoryAdapter adapter;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    JSONObject savedCat;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.z_notification_activity_category_selection);

       /* options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        try {
            savedCat = new JSONObject(sp.getString("CAT", "{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.listView);
        RequestParams param = new RequestParams();
        param.put("email",
                "");
        adapter = new CategoryAdapter(this);
        listView.setAdapter(adapter);
        Server.get("/user/categories", param, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(CategorySelectionActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray jsonArray= null;
                try {
                    jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Categeries categeries = new Categeries();
                        try {
                            categeries.setId(jsonArray.getJSONObject(i).getString("id"));
                            categeries.setName(jsonArray.getJSONObject(i).getString("cat_name"));
                            categeries.setSlug(jsonArray.getJSONObject(i).getString("cat_slug"));
                            categeries.setImage(jsonArray.getJSONObject(i).getString("image"));
                            adapter.add(categeries);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.cancel();
            }

        });

    }


    public void addCategory(String cat) {
        try {
            savedCat.put(cat, true);
            saveCat();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveCat() {
        editor.putString("CAT", savedCat.toString());
        editor.commit();
    }

    public void removeCat(String cat) {
        if (savedCat.has(cat)) {
            savedCat.remove(cat);
            saveCat();
        }
    }

    public class CategoryAdapter extends ArrayAdapter<Categeries> {

        private final LayoutInflater inflater;

        public CategoryAdapter(Context context) {
            super(context, 0);
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.notification_category_item, parent, false);
            }
            final CheckBox chk = (CheckBox) convertView.findViewById(R.id.checkBox); // title
            final ImageView image= (ImageView) convertView.findViewById(R.id.image); // title
            if (savedCat.has(getItem(position).getSlug())) {
                chk.setChecked(true);
            } else {
                chk.setChecked(false);
            }
            chk.setText(getItem(position).getName());
            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chk.isChecked()) {
                        addCategory(getItem(position).getSlug());
                        FirebaseMessaging.getInstance().subscribeToTopic(getItem(position).getSlug());
                    } else {
                        removeCat(getItem(position).getSlug());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(getItem(position).getSlug());
                    }
                }
            });

            ImageLoader.getInstance().displayImage(getItem(position).getImage(),image);
            return convertView;

        }
*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }

}
