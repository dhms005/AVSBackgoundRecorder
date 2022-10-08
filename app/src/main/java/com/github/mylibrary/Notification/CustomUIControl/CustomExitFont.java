package com.github.mylibrary.Notification.CustomUIControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by lenovo on 14/9/16.
 */
public class CustomExitFont extends AppCompatTextView {


    public CustomExitFont(Context context) {
        super(context);
        init();
    }

    public CustomExitFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomExitFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets() , "fonts/HELR45W.TTF");
        setTypeface(tf);
    }
}
