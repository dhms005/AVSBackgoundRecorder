package com.github.mylibrary.Notification.CustomUIControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by lenovo on 14/9/16.
 */
public class CustomLatoTextViewLight extends AppCompatTextView {


    public CustomLatoTextViewLight(Context context) {
        super(context);
        init();
    }

    public CustomLatoTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomLatoTextViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets() , "fonts/Roboto-Thin.ttf");
        setTypeface(tf);
    }
}