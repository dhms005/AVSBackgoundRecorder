package com.ds.audio.video.screen.backgroundrecorder.exit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.ArrayList;
import java.util.Random;

public class DevSpy_RecomendedAppsDailogAdapter_New extends RecyclerView.Adapter<DevSpy_RecomendedAppsDailogAdapter_New.MyViewHolder> {

    public ArrayList<DevSpy_RecommandDatabaseModel> recomendedAppArrayList;
    private Context mContext;
    int type = 1;
    String iconType;

    public DevSpy_RecomendedAppsDailogAdapter_New(Context context, ArrayList<DevSpy_RecommandDatabaseModel> recomendedAppArrayList, int type, String iconType) {
        this.recomendedAppArrayList = recomendedAppArrayList;
        this.mContext = context;
        this.type = type;
        this.iconType = iconType;
        notifyAnimation();

        for (int i = 0; i < recomendedAppArrayList.size(); i++) {

            float min = 3.5f, max = (float) 5.0;
            Random r = new Random();
            float finalX = r.nextFloat() * (max - min) + min;
            recomendedAppArrayList.get(i).setRating(finalX);
        }

    }

    private void notifyAnimation() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if (type == 8) {
            view = LayoutInflater.from(mContext).inflate(R.layout.devspy_recomended_apps_item_8, viewGroup, false);
        } else if (type == 9) {
            view = LayoutInflater.from(mContext).inflate(R.layout.devspy_recomended_apps_item_9, viewGroup, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        if (iconType.equals("3")) {
            Glide.with(mContext).load("http://www.apps.s4apps.in/uploads/RecommendedApplication/" + recomendedAppArrayList.get(position).getIcon3().trim()).into(holder.img);
        } else if (iconType.equals("2")) {
            Glide.with(mContext).load("http://www.apps.s4apps.in/uploads/RecommendedApplication/" + recomendedAppArrayList.get(position).getIcon2().trim()).into(holder.img);
        } else {
            Glide.with(mContext).load("http://www.apps.s4apps.in/uploads/RecommendedApplication/" + recomendedAppArrayList.get(position).getIcon().trim()).into(holder.img);
        }

        holder.key.setText(recomendedAppArrayList.get(position).getName().trim());

//        holder.ratingbar1.setRating(recomendedAppArrayList.get(position).getRating());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recomendedAppArrayList.get(position).getLink().trim())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recomendedAppArrayList.get(position).getLink().trim())));
                }

            }
        });

        holder.btn_using_ppo_no_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recomendedAppArrayList.get(position).getLink().trim())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recomendedAppArrayList.get(position).getLink().trim())));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return recomendedAppArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView key;
        LinearLayout cardView;
        ImageView img;
        LinearLayout btn_using_ppo_no_click;
        // ImageView ratingbar1;
        TextView txtInstall;

        public MyViewHolder(View v) {
            super(v);

            key = (TextView) v.findViewById(R.id.key_RecomendedAppsAdapter);
            img = (ImageView) v.findViewById(R.id.img_RecomendedAppsAdapter);
            cardView = (LinearLayout) v.findViewById(R.id.cardview_VoterCardAdapter);
            btn_using_ppo_no_click = (LinearLayout) v.findViewById(R.id.btn_using_ppo_no_click);
            //ratingbar1 = (ImageView) v.findViewById(R.id.ratingBar1);
            txtInstall = (TextView) v.findViewById(R.id.txtInstall);

        }
    }
}
