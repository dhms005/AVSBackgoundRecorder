package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.ColorPickerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import ja.burhanrashid52.photoeditor.shape.ShapeType;

public class ShapeBSFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    
    public Properties mProperties;
    int type = 1;

    public interface Properties {
        void onColorChanged(int i);

        void onOpacityChanged(int i);

        void onShapePicked(ShapeType shapeType);

        void onShapeSizeChanged(int i);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_bottom_shapes_dialog, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.shapeColors);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.shapeOpacity);
        SeekBar seekBar2 = (SeekBar) view.findViewById(R.id.shapeSize);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.shapeRadioGroup);
        CardView cardView = (CardView) view.findViewById(R.id.cardBrush);
        CardView cardView2 = (CardView) view.findViewById(R.id.cardLine);
        CardView cardView3 = (CardView) view.findViewById(R.id.cardOval);
        CardView cardView4 = (CardView) view.findViewById(R.id.cardRectangle);
        int i = this.type;
        if (i == 1) {
            setCardColor(cardView, cardView2, cardView3, cardView4);
            this.mProperties.onShapePicked(ShapeType.BRUSH);
        } else if (i == 2) {
            setCardColor(cardView2, cardView, cardView3, cardView4);
            this.mProperties.onShapePicked(ShapeType.LINE);
        } else if (i == 3) {
            setCardColor(cardView3, cardView2, cardView, cardView4);
            this.mProperties.onShapePicked(ShapeType.OVAL);
        } else if (i == 4) {
            setCardColor(cardView4, cardView3, cardView2, cardView);
            this.mProperties.onShapePicked(ShapeType.RECTANGLE);
        }
        final CardView cardView5 = cardView;
        final CardView cardView6 = cardView2;
        final CardView cardView7 = cardView3;
        final CardView cardView8 = cardView4;
        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShapeBSFragment.this.type = 1;
                ShapeBSFragment.this.mProperties.onShapePicked(ShapeType.BRUSH);
                ShapeBSFragment.this.setCardColor(cardView5, cardView6, cardView7, cardView8);
            }
        });
        final CardView cardView9 = cardView2;
        final CardView cardView10 = cardView;
        cardView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShapeBSFragment.this.type = 2;
                ShapeBSFragment.this.mProperties.onShapePicked(ShapeType.LINE);
                ShapeBSFragment.this.setCardColor(cardView9, cardView10, cardView7, cardView8);
            }
        });
        final CardView cardView11 = cardView3;
        final CardView cardView12 = cardView2;
        final CardView cardView13 = cardView;
        cardView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShapeBSFragment.this.type = 3;
                ShapeBSFragment.this.mProperties.onShapePicked(ShapeType.OVAL);
                ShapeBSFragment.this.setCardColor(cardView11, cardView12, cardView13, cardView8);
            }
        });
        final CardView cardView14 = cardView4;
        final CardView cardView15 = cardView3;
        final CardView cardView16 = cardView2;
        final CardView cardView17 = cardView;
        cardView4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ShapeBSFragment.this.type = 4;
                ShapeBSFragment.this.mProperties.onShapePicked(ShapeType.RECTANGLE);
                ShapeBSFragment.this.setCardColor(cardView14, cardView15, cardView16, cardView17);
            }
        });
        seekBar.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            public final void onColorPickerClickListener(int i) {

                if (mProperties != null) {
                    dismiss();
                    mProperties.onColorChanged(i);
                }
            }
        });
        recyclerView.setAdapter(colorPickerAdapter);
    }


    public void setPropertiesChangeListener(Properties properties) {
        this.mProperties = properties;
    }

    
    public void setCardColor(CardView cardView, CardView cardView2, CardView cardView3, CardView cardView4) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.save));
        cardView2.setCardBackgroundColor(getResources().getColor(R.color.black));
        cardView3.setCardBackgroundColor(getResources().getColor(R.color.black));
        cardView4.setCardBackgroundColor(getResources().getColor(R.color.black));
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        Properties properties;
        int id = seekBar.getId();
        if (id == R.id.shapeOpacity) {
            Properties properties2 = this.mProperties;
            if (properties2 != null) {
                properties2.onOpacityChanged(i);
            }
        } else if (id == R.id.shapeSize && (properties = this.mProperties) != null) {
            properties.onShapeSizeChanged(i);
        }
    }
}
