package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Adapter.ColorAdapter;
import com.raed.drawingview.BrushView;
import com.raed.drawingview.DrawingView;
import com.raed.drawingview.brushes.BrushSettings;

import java.util.ArrayList;

public class BrushService extends Service implements View.OnTouchListener, View.OnClickListener {
    public static LinearLayout lbrus;
    private ColorAdapter colorAdapter;
    public DrawingView drawingView;
    private ConstraintLayout mLayout;
    private NotificationManager mNotificationManager;
    private WindowManager.LayoutParams mParams;
    private String path = "";
    private WindowManager windowManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public void onCreate() {
        super.onCreate();
    }

    private void initView() {
        this.windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        ConstraintLayout constraintLayout = (ConstraintLayout) ((LayoutInflater) getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.screenrecorder_main_brush, (ViewGroup) null);
        this.mLayout = constraintLayout;
        this.drawingView = (DrawingView) constraintLayout.findViewById(R.id.drawview);
        RecyclerView recyclerView = (RecyclerView) this.mLayout.findViewById(R.id.rcv);
        ConstraintLayout constraintLayout2 = (ConstraintLayout) this.mLayout.findViewById(R.id.container_color);
        lbrus = (LinearLayout) this.mLayout.findViewById(R.id.layout_brush);
        ImageView imgPaint = (ImageView) this.mLayout.findViewById(R.id.imgPaint);
        ImageView imgEraser = (ImageView) this.mLayout.findViewById(R.id.imgEraser);
        ImageView imgUndo = (ImageView) this.mLayout.findViewById(R.id.imgUndo);
        SeekBar size_seek_bar = (SeekBar) this.mLayout.findViewById(R.id.size_seek_bar);
        ((ImageView) this.mLayout.findViewById(R.id.imgClose)).setOnClickListener(this);
        ((ImageView) this.mLayout.findViewById(R.id.imv_close)).setOnClickListener(view -> constraintLayout2.setVisibility(View.GONE));
        this.mParams = new WindowManager.LayoutParams(-1, -1, Build.VERSION.SDK_INT < 26 ?2002 : 2038, 8, -3);
        final BrushView brushView = (BrushView) this.mLayout.findViewById(R.id.brush_view);
        brushView.setDrawingView(this.drawingView);
        final BrushSettings brushSettings = this.drawingView.getBrushSettings();
        brushSettings.setSelectedBrush(0);
        this.drawingView.setUndoAndRedoEnable(true);
//        imgEraser.setOnClickListener(new BrushService$$ExternalSyntheticLambda1(brushSettings));
        imgEraser.setOnClickListener(view -> brushSettings.setSelectedBrush(4));
        imgUndo.setOnClickListener(view -> drawingView.undo());
        imgPaint.setOnClickListener(view -> {
            brushSettings.setSelectedBrush(0);
            constraintLayout2.setVisibility(0);
        });
        this.windowManager.addView(this.mLayout, this.mParams);
        size_seek_bar.setMax(100);
        size_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brushSettings.setSelectedBrushSize(((float) i) / 100.0f);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                brushView.setVisibility(0);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                brushView.setVisibility(8);
            }
        });
        ColorAdapter colorAdapter2 = new ColorAdapter(this, initColors(), new ColorAdapter.OnClick() {
            @Override
            public void onClickColor(int i) {
                brushSettings.setColor(i);
            }
        });
        this.colorAdapter = colorAdapter2;
        recyclerView.setAdapter(colorAdapter2);
    }



    private ArrayList<Integer> initColors() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(Color.parseColor("#ffffff")));
        arrayList.add(Integer.valueOf(Color.parseColor("#039BE5")));
        arrayList.add(Integer.valueOf(Color.parseColor("#00ACC1")));
        arrayList.add(Integer.valueOf(Color.parseColor("#00897B")));
        arrayList.add(Integer.valueOf(Color.parseColor("#FDD835")));
        arrayList.add(Integer.valueOf(Color.parseColor("#FFB300")));
        arrayList.add(Integer.valueOf(Color.parseColor("#F4511E")));
        return arrayList;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();
        return 2;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgClose) {
            stopSelf();
        }
    }

    public void onDestroy() {
        ConstraintLayout constraintLayout;
        WindowManager windowManager2 = this.windowManager;
        if (!(windowManager2 == null || (constraintLayout = this.mLayout) == null)) {
            windowManager2.removeView(constraintLayout);
        }
        super.onDestroy();
    }
}
