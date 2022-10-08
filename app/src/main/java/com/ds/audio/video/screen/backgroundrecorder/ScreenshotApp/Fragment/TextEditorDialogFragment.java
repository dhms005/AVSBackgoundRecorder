package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.ColorPickerAdapter;

public class TextEditorDialogFragment extends DialogFragment {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "TextEditorDialogFragment";
    private TextView mAddTextDoneTextView;
    
    public EditText mAddTextEditText;
    
    public int mColorCode;
    
    public InputMethodManager mInputMethodManager;
    
    public TextEditor mTextEditor;

    public interface TextEditor {
        void onDone(String str, int i);
    }

    public static TextEditorDialogFragment show(AppCompatActivity appCompatActivity, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        TextEditorDialogFragment textEditorDialogFragment = new TextEditorDialogFragment();
        textEditorDialogFragment.setArguments(bundle);
        textEditorDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return textEditorDialogFragment;
    }

    public static TextEditorDialogFragment show(AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.add_text_dialog, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mAddTextEditText = (EditText) view.findViewById(R.id.add_text_edit_text);
        this.mInputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
        this.mAddTextDoneTextView = (TextView) view.findViewById(R.id.add_text_done_tv);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.add_text_color_picker_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            public void onColorPickerClickListener(int i) {
                int unused = TextEditorDialogFragment.this.mColorCode = i;
                TextEditorDialogFragment.this.mAddTextEditText.setTextColor(i);
            }
        });
        recyclerView.setAdapter(colorPickerAdapter);
        this.mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        int i = getArguments().getInt(EXTRA_COLOR_CODE);
        this.mColorCode = i;
        this.mAddTextEditText.setTextColor(i);
        this.mAddTextEditText.requestFocus();
        this.mInputMethodManager.toggleSoftInput(2, 0);
        this.mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextEditorDialogFragment.this.mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                TextEditorDialogFragment.this.dismiss();
                String obj = TextEditorDialogFragment.this.mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(obj) && TextEditorDialogFragment.this.mTextEditor != null) {
                    TextEditorDialogFragment.this.mTextEditor.onDone(obj, TextEditorDialogFragment.this.mColorCode);
                }
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor) {
        this.mTextEditor = textEditor;
    }
}
