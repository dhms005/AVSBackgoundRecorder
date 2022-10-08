package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class EmojiBSFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                EmojiBSFragment.this.dismiss();
            }
        }
    };
    
    public EmojiListener mEmojiListener;

    public interface EmojiListener {
        void onEmojiClick(String str);
    }

    public void setupDialog(Dialog dialog, int i) {
        super.setupDialog(dialog, i);
        View inflate = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, (ViewGroup) null);
        dialog.setContentView(inflate);
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) ((View) inflate.getParent()).getLayoutParams()).getBehavior();
        if (behavior != null && (behavior instanceof BottomSheetBehavior)) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(this.mBottomSheetBehaviorCallback);
        }
        ((View) inflate.getParent()).setBackgroundColor(getResources().getColor(17170445));
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rvEmoji);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setAdapter(new EmojiAdapter());
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        this.mEmojiListener = emojiListener;
    }

    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
        ArrayList<String> emojisList;

        public EmojiAdapter() {
            this.emojisList = EmojiBSFragment.getEmojis(EmojiBSFragment.this.getActivity());
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_emoji, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtEmoji.setText(this.emojisList.get(i));
        }

        public int getItemCount() {
            return this.emojisList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(View view) {
                super(view);
                this.txtEmoji = (TextView) view.findViewById(R.id.txtEmoji);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (EmojiBSFragment.this.mEmojiListener != null) {
                            EmojiBSFragment.this.mEmojiListener.onEmojiClick(EmojiAdapter.this.emojisList.get(ViewHolder.this.getLayoutPosition()));
                        }
                        EmojiBSFragment.this.dismiss();
                    }
                });
            }
        }
    }

    public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String convertEmoji : context.getResources().getStringArray(R.array.photo_editor_emoji)) {
            arrayList.add(convertEmoji(convertEmoji));
        }
        return arrayList;
    }

    private static String convertEmoji(String str) {
        try {
            return new String(Character.toChars(Integer.parseInt(str.substring(2), 16)));
        } catch (NumberFormatException unused) {
            return "";
        }
    }
}
