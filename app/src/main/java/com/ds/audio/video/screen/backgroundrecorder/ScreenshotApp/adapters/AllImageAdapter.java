package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AllImageAdapter extends RecyclerView.Adapter<AllImageAdapter.Myview> {
    public ActionMode actionMode;
    
    public ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            try {
                boolean unused = AllImageAdapter.this.multiSelect = true;
                AllImageAdapter.this.actionMode = actionMode;
                actionMode.getMenuInflater().inflate(R.menu.action_menu, menu);
                AllImageAdapter.this.selectAllMenu = menu.findItem(R.id.selectAll);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.delete) {
                AllImageAdapter.this.callDialog();
                return true;
            } else if (itemId != R.id.selectAll) {
                return true;
            } else {
                AllImageAdapter.this.selectAll();
                return true;
            }
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            boolean unused = AllImageAdapter.this.multiSelect = false;
            boolean unused2 = AllImageAdapter.this.selectAll = false;
            AllImageAdapter.this.selectedItems.clear();
            AllImageAdapter.this.notifyDataSetChanged();
        }
    };
    Context context;
    ArrayList<DiaryImageData> diaryImageData;
    
    public boolean multiSelect = false;
    RecyclerClick recyclerClick;
    RequestOptions requestOptions;
    
    public boolean selectAll = false;
    MenuItem selectAllMenu;
    
    public ArrayList<Integer> selectedItems = new ArrayList<>();

    public interface RecyclerClick {
        void onClick(int i);
    }

    public void selectAll() {
        this.selectedItems.clear();
        if (!this.selectAll) {
            this.selectAllMenu.setIcon(R.drawable.ic_selectall);
            for (int i = 0; i < this.diaryImageData.size(); i++) {
                this.selectedItems.add(Integer.valueOf(i));
            }
            this.selectAll = true;
        } else {
            this.actionMode.finish();
            this.selectAllMenu.setIcon(R.drawable.select_all_empty);
            this.selectAll = false;
        }
        ActionMode actionMode2 = this.actionMode;
        if (actionMode2 != null) {
            actionMode2.setTitle("" + this.selectedItems.size());
        }
        notifyDataSetChanged();
    }



    public void callDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);

        TextView title = dialog.findViewById(R.id.title);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        CardView cardCancel = dialog.findViewById(R.id.cardCancel);
        CardView cardSave = dialog.findViewById(R.id.cardSave);

        // dialog.show();

        title.setText("Delete Image");
        txtMsg.setText("Are you sure want to delete this image?");
        cardCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cardSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String[] strArr = new String[AllImageAdapter.this.selectedItems.size()];
                String[] strArr2 = new String[AllImageAdapter.this.selectedItems.size()];
                Collections.sort(AllImageAdapter.this.selectedItems);
                Collections.reverse(AllImageAdapter.this.selectedItems);
                Iterator it = AllImageAdapter.this.selectedItems.iterator();
                int i = 0;
                while (it.hasNext()) {
                    Integer num = (Integer) it.next();
//                    if (Build.VERSION.SDK_INT >= 29) {
////                        AllImageAdapter.this.context.getContentResolver().delete(Uri.parse(AllImageAdapter.this.diaryImageData.get(num.intValue()).getPath()), (String) null, (String[]) null);
//
//                        CY_M_FileHelper.removeAllForPaths(new String[]{AllImageAdapter.this.diaryImageData.get(num.intValue()).getPath()}, context);
//
//                        AllImageAdapter.this.diaryImageData.remove(num.intValue());
//                    } else {
                        File file = new File(AllImageAdapter.this.diaryImageData.get(num.intValue()).getPath());
                        if (file.exists() && file.delete()) {
                            strArr[i] = file.getAbsolutePath();
                            AllImageAdapter.this.diaryImageData.remove(num.intValue());
                        }
//                    }
                    i++;
                }
                if (Build.VERSION.SDK_INT < 29) {
                    MediaScannerConnection.scanFile(AllImageAdapter.this.context, strArr, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
                }
                AllImageAdapter.this.notifyDataSetChanged();
                if (GalleryActivity.getInstance() != null) {
                    GalleryActivity.getInstance().checkSize();
                }
                AllImageAdapter.this.actionMode.finish();

                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public AllImageAdapter(Context context2) {
        this.context = context2;
    }

    public AllImageAdapter(Context context2, ArrayList<DiaryImageData> arrayList, RecyclerClick recyclerClick2) {
        this.context = context2;
        this.diaryImageData = arrayList;
        RequestOptions requestOptions2 = new RequestOptions();
        this.requestOptions = requestOptions2;
        this.recyclerClick = recyclerClick2;
        requestOptions2.centerCrop();
    }

    public Myview onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Myview(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allimages, viewGroup, false));
    }

    public void onBindViewHolder(Myview myview, int i) {
        DiaryImageData diaryImageData2 = this.diaryImageData.get(i);
        if (diaryImageData2.getPath() != null) {
            Glide.with(this.context).load(diaryImageData2.getPath()).apply((BaseRequestOptions<?>) this.requestOptions).into(myview.image);
            if (!this.multiSelect || !this.selectedItems.contains(Integer.valueOf(i))) {
                myview.cardview.setCardBackgroundColor(this.context.getResources().getColor(17170445));
            } else {
                myview.cardview.setCardBackgroundColor(this.context.getResources().getColor(R.color.colorPrimaryAlpha));
            }
        }
    }

    public int getItemCount() {
        return this.diaryImageData.size();
    }

    public class Myview extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CardView cardview;
        ImageView image;

        public Myview(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.cardview = (CardView) view.findViewById(R.id.cardview);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            if (!AllImageAdapter.this.multiSelect) {
                AllImageAdapter.this.recyclerClick.onClick(getAdapterPosition());
            } else {
                selectItem(Integer.valueOf(getAdapterPosition()));
            }
        }

       
        public void selectItem(Integer num) {
            if (AllImageAdapter.this.multiSelect) {
                if (AllImageAdapter.this.selectedItems.contains(num)) {
                    AllImageAdapter.this.selectedItems.remove(num);
                } else {
                    AllImageAdapter.this.selectedItems.add(num);
                }
            }
            if (AllImageAdapter.this.actionMode != null) {
                ActionMode actionMode = AllImageAdapter.this.actionMode;
                actionMode.setTitle(( "" + AllImageAdapter.this.selectedItems.size()));
                if (AllImageAdapter.this.selectedItems.size() == 0) {
                    AllImageAdapter.this.actionMode.finish();
                }
            }
            AllImageAdapter.this.notifyDataSetChanged();
        }

        public boolean onLongClick(View view) {
            if (AllImageAdapter.this.multiSelect) {
                return true;
            }
            ((AppCompatActivity) view.getContext()).startSupportActionMode(AllImageAdapter.this.actionModeCallbacks);
            selectItem(Integer.valueOf(getAdapterPosition()));
            return true;
        }
    }

    public void onViewRecycled(Myview myview) {
        super.onViewRecycled(myview);
        Context context2 = this.context;
        if (context2 != null) {
            Glide.with(context2).clear((View) myview.image);
        }
    }
}
