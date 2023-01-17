package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.ToolType;

import java.util.ArrayList;
import java.util.List;

public class EditingImageAdapter extends RecyclerView.Adapter<EditingImageAdapter.ViewHolder> {
    
    public OnItemSelected mOnItemSelected;
    
    public List<ToolModel> mToolList;

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    public EditingImageAdapter(OnItemSelected onItemSelected) {
        ArrayList arrayList = new ArrayList();
        this.mToolList = arrayList;
        this.mOnItemSelected = onItemSelected;
//        arrayList.add(new ToolModel("Crop", R.drawable.crop, ToolType.CROP));
        this.mToolList.add(new ToolModel("Undo", R.drawable.undo, ToolType.UNDO));
        this.mToolList.add(new ToolModel("Redo", R.drawable.redos, ToolType.REDO));
        this.mToolList.add(new ToolModel("Shape", R.drawable.ic_oval, ToolType.SHAPE));
        this.mToolList.add(new ToolModel("Text", R.drawable.ic_text, ToolType.TEXT));
        this.mToolList.add(new ToolModel("Eraser", R.drawable.ic_eraser, ToolType.ERASER));
        this.mToolList.add(new ToolModel("Filter", R.drawable.ic_photo_filter, ToolType.FILTER));
        this.mToolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon, ToolType.EMOJI));
    }

    class ToolModel {
        
        public int mToolIcon;
        
        public String mToolName;
        
        public ToolType mToolType;

        ToolModel(String str, int i, ToolType toolType) {
            this.mToolName = str;
            this.mToolIcon = i;
            this.mToolType = toolType;
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_editing_tools, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ToolModel toolModel = this.mToolList.get(i);
        viewHolder.txtTool.setText(toolModel.mToolName);
        viewHolder.imgToolIcon.setImageResource(toolModel.mToolIcon);
    }

    public int getItemCount() {
        return this.mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View view) {
            super(view);
            this.imgToolIcon = (ImageView) view.findViewById(R.id.imgToolIcon);
            this.txtTool = (TextView) view.findViewById(R.id.txtTool);
            view.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    ViewHolder.this.lambda$new$0$EditingImageAdapter$ViewHolder(view);
                }
            });
        }

        public void lambda$new$0$EditingImageAdapter$ViewHolder(View view) {
            EditingImageAdapter.this.mOnItemSelected.onToolSelected(((ToolModel) EditingImageAdapter.this.mToolList.get(getLayoutPosition())).mToolType);
        }
    }
}
