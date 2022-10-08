package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import java.util.ArrayList;
import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    
    public List<Integer> colorPickerColors;
    private Context context;
    private LayoutInflater inflater;
    
    public OnColorPickerClickListener onColorPickerClickListener;

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int i);
    }

    ColorPickerAdapter(Context context2, List<Integer> list) {
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
        this.colorPickerColors = list;
    }

    public ColorPickerAdapter(Context context2) {
        this(context2, getDefaultColors(context2));
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.color_picker_item_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i == 0) {
            viewHolder.cardMain.setCardBackgroundColor(this.context.getResources().getColor(R.color.white2));
        } else {
            viewHolder.cardMain.setCardBackgroundColor(this.context.getResources().getColor(R.color.white));
        }
        viewHolder.colorPickerView.setCardBackgroundColor(this.colorPickerColors.get(i).intValue());
    }

    public int getItemCount() {
        return this.colorPickerColors.size();
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener2) {
        this.onColorPickerClickListener = onColorPickerClickListener2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardMain;
        CardView colorPickerView;

        public ViewHolder(View view) {
            super(view);
            this.colorPickerView = (CardView) view.findViewById(R.id.color_picker_view);
            this.cardMain = (CardView) view.findViewById(R.id.cardMain);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (ColorPickerAdapter.this.onColorPickerClickListener != null) {
                        ColorPickerAdapter.this.onColorPickerClickListener.onColorPickerClickListener(((Integer) ColorPickerAdapter.this.colorPickerColors.get(ViewHolder.this.getAdapterPosition())).intValue());
                    }
                }
            });
        }
    }

    public static List<Integer> getDefaultColors(Context context2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color1)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.black)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color2)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color3)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color4)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color5)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color6)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color7)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color8)));
        arrayList.add(Integer.valueOf(ContextCompat.getColor(context2, R.color.color9)));
        return arrayList;
    }
}
