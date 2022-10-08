package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FilterListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {
    
    public FilterListener mFilterListener;
    
    public List<Pair<String, PhotoFilter>> mPairList = new ArrayList();

    public FilterViewAdapter(FilterListener filterListener) {
        this.mFilterListener = filterListener;
        setupFilters();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_filter_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Pair pair = this.mPairList.get(i);
        viewHolder.mImageFilterView.setImageBitmap(getBitmapFromAsset(viewHolder.itemView.getContext(), (String) pair.first));
        viewHolder.mTxtFilterName.setText(((PhotoFilter) pair.second).name().replace("_", " "));
    }

    public int getItemCount() {
        return this.mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View view) {
            super(view);
            this.mImageFilterView = (ImageView) view.findViewById(R.id.imgFilterView);
            this.mTxtFilterName = (TextView) view.findViewById(R.id.txtFilterName);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    FilterViewAdapter.this.mFilterListener.onFilterSelected((PhotoFilter) ((Pair) FilterViewAdapter.this.mPairList.get(ViewHolder.this.getLayoutPosition())).second);
                }
            });
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupFilters() {
        this.mPairList.add(new Pair("filters/original.jpg", PhotoFilter.NONE));
        this.mPairList.add(new Pair("filters/auto_fix.png", PhotoFilter.AUTO_FIX));
        this.mPairList.add(new Pair("filters/brightness.png", PhotoFilter.BRIGHTNESS));
        this.mPairList.add(new Pair("filters/contrast.png", PhotoFilter.CONTRAST));
        this.mPairList.add(new Pair("filters/documentary.png", PhotoFilter.DOCUMENTARY));
        this.mPairList.add(new Pair("filters/dual_tone.png", PhotoFilter.DUE_TONE));
        this.mPairList.add(new Pair("filters/fill_light.png", PhotoFilter.FILL_LIGHT));
        this.mPairList.add(new Pair("filters/fish_eye.png", PhotoFilter.FISH_EYE));
        this.mPairList.add(new Pair("filters/grain.png", PhotoFilter.GRAIN));
        this.mPairList.add(new Pair("filters/gray_scale.png", PhotoFilter.GRAY_SCALE));
        this.mPairList.add(new Pair("filters/lomish.png", PhotoFilter.LOMISH));
        this.mPairList.add(new Pair("filters/negative.png", PhotoFilter.NEGATIVE));
        this.mPairList.add(new Pair("filters/posterize.png", PhotoFilter.POSTERIZE));
        this.mPairList.add(new Pair("filters/saturate.png", PhotoFilter.SATURATE));
        this.mPairList.add(new Pair("filters/sepia.png", PhotoFilter.SEPIA));
        this.mPairList.add(new Pair("filters/sharpen.png", PhotoFilter.SHARPEN));
        this.mPairList.add(new Pair("filters/temprature.png", PhotoFilter.TEMPERATURE));
        this.mPairList.add(new Pair("filters/tint.png", PhotoFilter.TINT));
        this.mPairList.add(new Pair("filters/vignette.png", PhotoFilter.VIGNETTE));
        this.mPairList.add(new Pair("filters/cross_process.png", PhotoFilter.CROSS_PROCESS));
        this.mPairList.add(new Pair("filters/b_n_w.png", PhotoFilter.BLACK_WHITE));
        this.mPairList.add(new Pair("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL));
        this.mPairList.add(new Pair("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL));
        this.mPairList.add(new Pair("filters/rotate.png", PhotoFilter.ROTATE));
    }
}
