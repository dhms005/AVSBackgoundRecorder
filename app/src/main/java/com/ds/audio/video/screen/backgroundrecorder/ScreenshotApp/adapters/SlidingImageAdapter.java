package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.chrisbanes.photoview.PhotoView;
import java.io.File;
import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {
    private Context context;
    int from;
    private ArrayList<DiaryImageData> imagesList;
    private LayoutInflater inflater;
    RequestOptions requestOptions = new RequestOptions();

    public int getItemPosition(Object obj) {
        return -2;
    }

    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    public Parcelable saveState() {
        return null;
    }

    public SlidingImageAdapter(Context context2, ArrayList<DiaryImageData> arrayList, int i) {
        this.context = context2;
        this.imagesList = arrayList;
        this.inflater = LayoutInflater.from(context2);
        this.from = i;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public int getCount() {
        return this.imagesList.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = this.inflater.inflate(R.layout.slidingimages_layout, viewGroup, false);
        PhotoView photoView = (PhotoView) inflate.findViewById(R.id.image);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.noimage);
        if (Build.VERSION.SDK_INT >= 29) {
            imageView.setVisibility(View.GONE);
//            Glide.with(this.context).load(Uri.parse(this.imagesList.get(i).getPath())).into((ImageView) photoView);
            Glide.with(this.context).load(this.imagesList.get(i).getPath()).into((ImageView) photoView);
        } else if (new File(this.imagesList.get(i).getPath()).exists()) {
            imageView.setVisibility(View.GONE);
            Glide.with(this.context).load(this.imagesList.get(i).getPath()).into((ImageView) photoView);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        viewGroup.addView(inflate, 0);
        return inflate;
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }
}
