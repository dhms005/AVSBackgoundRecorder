package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_SaveVideoActivity;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Model.VideoRecordModel;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.List;

public class VideoRecordAdapterList extends RecyclerView.Adapter<VideoRecordAdapterList.VideoHolder> {
    private ItemClickCallBack itemClickCallBack;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<VideoRecordModel> mList;

    public VideoRecordAdapterList(Activity context, List<VideoRecordModel> list) {
        this.mContext = context;
        this.mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack2) {
        this.itemClickCallBack = itemClickCallBack2;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoHolder(this.mLayoutInflater.inflate(R.layout.item_video_list, viewGroup, false));
    }

    public void onBindViewHolder(VideoHolder videoHolder, int i) {
        VideoRecordModel cCTVVideoRecord = this.mList.get(i);

        videoHolder.vidoplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(mContext, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(mContext, Video_SaveVideoActivity.class);
                        intent.putExtra("videourl", cCTVVideoRecord.getFilePath());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                });
            }
        });

        videoHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, videoHolder.checkBox);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.menu_folder, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        itemClickCallBack.onMenuClick(menuItem.getItemId(),i);
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


        videoHolder.tvVideoName.setText(cCTVVideoRecord.getFileName());
        videoHolder.tvVideoDuration.setText(cCTVVideoRecord.getTimeVideo());
        videoHolder.tvVideoDate.setText(cCTVVideoRecord.getDate());
//        videoHolder.checkBox.setChecked(cCTVVideoRecord.isSelect());
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }


    public interface ItemClickCallBack {
        void onChkBoxClick(View view, int i, boolean z);

        void onImageClick(View view, int i);

        void onMenuClick(int menuItem, int i);
    }


    public class VideoHolder extends RecyclerView.ViewHolder {
        ImageView checkBox;
        LinearLayout rootLayout;
        ImageView vidoplayer;
        TextView tvVideoDate;
        TextView tvVideoDuration;
        TextView tvVideoName;

        public VideoHolder(View view) {
            super(view);

            checkBox = (ImageView) view.findViewById(R.id.item_cb);
            rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            vidoplayer = (ImageView) view.findViewById(R.id.vidoplayer);
            tvVideoDate = (TextView) view.findViewById(R.id.tv_video_date);
            tvVideoDuration = (TextView) view.findViewById(R.id.tv_video_duration);
            tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);


            this.rootLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VideoRecordAdapterList.this.itemClickCallBack.onImageClick(view, VideoHolder.this.getAdapterPosition());
                }
            });

//            this.checkBox.setOnCheckedChangeListener(new AnimateCheckBox.OnCheckedChangeListener() {
//
//
//                @Override
//                public void onCheckedChanged(View view, boolean z) {
//                    CY_M_VideoRecordAdapter.this.itemClickCallBack.onChkBoxClick(view, VideoHolder.this.getAdapterPosition(), z);
//                }
//            });
        }
    }
}
