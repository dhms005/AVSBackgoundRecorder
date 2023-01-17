package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment.CropFragment;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment.EmojiBSFragment;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment.ShapeBSFragment;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment.TextEditorDialogFragment;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.DiaryImageData;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.EditingImageAdapter;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.FilterViewAdapter;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.Constants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FileSaveHelper;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FilterListener;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.ToolType;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityEditImageBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder;
import ja.burhanrashid52.photoeditor.shape.ShapeType;

public class EditImageActivity extends AppCompatActivity implements EditingImageAdapter.OnItemSelected, FilterListener, ShapeBSFragment.Properties, EmojiBSFragment.EmojiListener, OnPhotoEditorListener, View.OnClickListener {
    public static final String FILE_PATH = "file_path";
    public static final String FROM_GALLARY = "FROM_GALLARY";
    public static final String REFERENCE = "REFERENCE";
    BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    FilterViewAdapter adapter;
    ActivityEditImageBinding binding;
    Bitmap bitmap;
    Uri cropUri;
    String destinationFileName;
    EditingImageAdapter editingImageAdapter;
    String extension;
    String filePath;
    Bitmap.CompressFormat format;
    String iamgePath;
    String imageName;
    boolean isBeenSaved = false;
    boolean isFromFullImageView;
    boolean isFromGallery = false;
    boolean isFromPrivate;
    ConstraintSet mConstraintSet = new ConstraintSet();
    EmojiBSFragment mEmojiBSFragment;
    boolean mIsFilterVisible;
    PhotoEditor mPhotoEditor;
    FileSaveHelper mSaveFileHelper;
    ShapeBSFragment mShapeBSFragment;
    ShapeBuilder mShapeBuilder;
    String path;


    public void onRemoveViewListener(ViewType viewType, int i) {
    }

    public void onStartViewChangeListener(ViewType viewType) {
    }

    public void onStopViewChangeListener(ViewType viewType) {
    }

    public void onTouchSourceImage(MotionEvent motionEvent) {
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActivityEditImageBinding activityEditImageBinding = (ActivityEditImageBinding) DataBindingUtil.setContentView(this, R.layout.activity_edit_image);
        this.binding = activityEditImageBinding;
//        AdConstant.loadBanner(this, activityEditImageBinding.frmMainBannerView, this.binding.frmShimmer, this.binding.bannerView);
        boolean booleanExtra = getIntent().getBooleanExtra(Constants.PINCH_TEXT_SCALABLE_INTENT_KEY, true);
        this.isFromGallery = getIntent().getBooleanExtra(FROM_GALLARY, false);
        this.isFromFullImageView = getIntent().getBooleanExtra("isFromFullImageView", false);
        this.isFromPrivate = getIntent().getBooleanExtra("FromPrivate", false);
        this.mPhotoEditor = new PhotoEditor.Builder(this, this.binding.photoEditor).setPinchTextScalable(booleanExtra).build();
        this.mSaveFileHelper = new FileSaveHelper((AppCompatActivity) this);
        this.filePath = getIntent().getStringExtra(FILE_PATH);
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        fragments();
        setImage();
        setToolsRecycler();
        setFilterAdapter();
        clicks();
        showFilter(false);
    }

    private void setImage() {
        showProgressBar();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                Uri uri;
                if (EditImageActivity.this.isFromFullImageView) {
                    if (!EditImageActivity.this.isFromPrivate) {
                        uri = Uri.parse(EditImageActivity.this.filePath);
                    } else {

                       Log.e("@@@",filePath.toString());
                        uri = FileProvider.getUriForFile(EditImageActivity.this, "com.ds.audio.video.screen.backgroundrecorder.provider", new File(EditImageActivity.this.filePath));
                    }
                    try {
                        EditImageActivity editImageActivity = EditImageActivity.this;
                        editImageActivity.bitmap = editImageActivity.getThumbnail(uri, editImageActivity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EditImageActivity.this.binding.photoEditor.getSource().setImageBitmap(EditImageActivity.this.bitmap);
                } else {
                    EditImageActivity.this.binding.photoEditor.getSource().setImageURI(Uri.fromFile(new File(EditImageActivity.this.filePath)));
                }
                EditImageActivity.this.hideProgressBar();
            }
        }, 500);
    }

    public Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException {
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        int photoOrientation = getPhotoOrientation(uri);
        Matrix matrix = new Matrix();
        if (Build.VERSION.SDK_INT >= 24) {
            matrix.postRotate((float) photoOrientation);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(openInputStream, (Rect) null, options);
        openInputStream.close();
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        int i = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
        double d = i > 2048 ? (double) (i / 2048) : 1.0d;
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = getPowerOfTwoForSampleRatio(d);
        options2.inDither = true;
        options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream openInputStream2 = context.getContentResolver().openInputStream(uri);
        Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, (Rect) null, options2);
        openInputStream2.close();
        return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
    }

    private static int getPowerOfTwoForSampleRatio(double d) {
        int highestOneBit = Integer.highestOneBit((int) Math.floor(d));
        if (highestOneBit == 0) {
            return 1;
        }
        return highestOneBit;
    }

    private int getPhotoOrientation(Uri uri) {
        ExifInterface exifInterface;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                exifInterface = new ExifInterface(inputStream);
                inputStream.close();
            } else {
                ExifInterface exifInterface2 = new ExifInterface(uri.getPath());
                Log.e("ExifInterface", uri.getPath());
                exifInterface = exifInterface2;
            }
            int attributeInt = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 3) {
                return SubsamplingScaleImageView.ORIENTATION_180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return SubsamplingScaleImageView.ORIENTATION_270;
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    private void clicks() {
        this.binding.imgUndo.setOnClickListener(this);
        this.binding.imgRedo.setOnClickListener(this);
        this.binding.imgClose.setOnClickListener(this);
        this.binding.imgSave.setOnClickListener(this);
    }

    private void fragments() {
        ShapeBSFragment shapeBSFragment = new ShapeBSFragment();
        this.mShapeBSFragment = shapeBSFragment;
        shapeBSFragment.setPropertiesChangeListener(this);
        EmojiBSFragment emojiBSFragment = new EmojiBSFragment();
        this.mEmojiBSFragment = emojiBSFragment;
        emojiBSFragment.setEmojiListener(this);
    }

    private void setToolsRecycler() {
        this.editingImageAdapter = new EditingImageAdapter(this);
        this.binding.rvTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.binding.rvTools.setAdapter(this.editingImageAdapter);
    }

    private void setFilterAdapter() {
        this.adapter = new FilterViewAdapter(this);
        this.binding.rvFilters.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.binding.rvFilters.setAdapter(this.adapter);
    }
    



    public void onToolSelected(ToolType toolType) {
        switch (toolType.ordinal()) {
            case 0:
                Log.e("Crop Image", "");
                this.binding.txtCurrentTool.setText("Crop");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        saveImageForCrop();
                    }
                }, 300);
                return;
            case 1:
                Log.e("undo Image", "");
                this.binding.txtCurrentTool.setText("Undo");
                this.mPhotoEditor.undo();
                return;
            case 2:
                Log.e("redo Image", "");
                this.binding.txtCurrentTool.setText("Redo");
                this.mPhotoEditor.redo();
                return;
            case 3:
                Log.e("brush drawing", "");
                this.mPhotoEditor.setBrushDrawingMode(true);
                ShapeBuilder shapeBuilder = new ShapeBuilder();
                this.mShapeBuilder = shapeBuilder;
                this.mPhotoEditor.setShape(shapeBuilder);
                this.binding.txtCurrentTool.setText(R.string.label_shape);
                showBottomSheetDialogFragment(this.mShapeBSFragment);
                return;
            case 4:
                Log.e("Text ", "");
                TextEditorDialogFragment.show(this).setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    public final void onDone(String str, int i) {

                        TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
                        textStyleBuilder.withTextColor(i);
                        mPhotoEditor.addText(str, textStyleBuilder);
                        binding.txtCurrentTool.setText(R.string.label_text);
                    }
                });
                return;
            case 5:
                Log.e("brush image", "");
                this.mPhotoEditor.brushEraser();
                this.binding.txtCurrentTool.setText(R.string.label_eraser_mode);
                return;
            case 6:
                Log.e("filter Image", "");
                this.binding.txtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                return;
            case 7:
                Log.e("emoji open", "");
                showBottomSheetDialogFragment(this.mEmojiBSFragment);
                return;
            default:
                return;
        }
    }

    
    public Uri storeImage(String str, Bitmap bitmap2) {
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.format = Bitmap.CompressFormat.JPEG;
        } else {
            this.format = Bitmap.CompressFormat.PNG;
        }
        File file = new File(AppConstants.getCachePath(this), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e2) {
            Log.d("TAG", "Error accessing file: " + e2.getMessage());
        }
        return Uri.parse(file.toString());
    }

    public void onFilterSelected(PhotoFilter photoFilter) {
        this.mPhotoEditor.setFilterEffect(photoFilter);
    }

    private void showBottomSheetDialogFragment(BottomSheetDialogFragment bottomSheetDialogFragment) {
        if (bottomSheetDialogFragment != null && !bottomSheetDialogFragment.isAdded()) {
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }


    public void showFilter(boolean z) {
        this.mIsFilterVisible = z;
        this.mConstraintSet.clone(this.binding.rootView);
        if (z) {
            this.mConstraintSet.clear(this.binding.rvFilters.getId(), 6);
            this.mConstraintSet.connect(this.binding.rvFilters.getId(), 6, 0, 6);
            this.mConstraintSet.connect(this.binding.rvFilters.getId(), 7, 0, 7);
        } else {
            this.mConstraintSet.connect(this.binding.rvFilters.getId(), 6, 0, 7);
            this.mConstraintSet.clear(this.binding.rvFilters.getId(), 7);
        }
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(this.binding.rootView, changeBounds);
        this.mConstraintSet.applyTo(this.binding.rootView);
    }

    public void onBackPressed() {
        AppConstants.deleteTempFile(this);
        if (this.mIsFilterVisible) {
            showFilter(false);
        } else {
            onSaveTaskDone(this.path);
        }
    }

    public void onColorChanged(int i) {
        this.mPhotoEditor.setShape(this.mShapeBuilder.withShapeColor(i));
        this.binding.txtCurrentTool.setText(R.string.label_brush);
    }

    public void onOpacityChanged(int i) {
        this.mPhotoEditor.setShape(this.mShapeBuilder.withShapeOpacity(i));
        this.binding.txtCurrentTool.setText(R.string.label_brush);
    }

    public void onShapeSizeChanged(int i) {
        this.mPhotoEditor.setShape(this.mShapeBuilder.withShapeSize((float) i));
        this.binding.txtCurrentTool.setText(R.string.label_brush);
    }

    public void onShapePicked(ShapeType shapeType) {
        this.mPhotoEditor.setShape(this.mShapeBuilder.withShapeType(shapeType));
    }

    public void onEmojiClick(String str) {
        this.mPhotoEditor.addEmoji(str);
        this.binding.txtCurrentTool.setText(R.string.label_emoji);
    }

    public void onEditTextChangeListener(View view, String str, int i) {
        TextEditorDialogFragment.show(this, str, i).setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {

            public final void onDone(String str, int i) {

                TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
                textStyleBuilder.withTextColor(i);
                mPhotoEditor.editText(view, str, textStyleBuilder);
                binding.txtCurrentTool.setText(R.string.label_text);
            }
        });
    }

    public void onAddViewListener(ViewType viewType, int i) {
        Log.d("TAG", "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                onBackPressed();
                return;
            case R.id.imgRedo:
                this.mPhotoEditor.redo();
                return;
            case R.id.imgSave:
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        saveImage();
                    }
                }, 20);
                return;
            case R.id.imgUndo:
                this.mPhotoEditor.undo();
                return;
            default:
                return;
        }
    }


    public void saveImageForCrop() {
        showProgressBar();
        this.mPhotoEditor.saveAsBitmap(new SaveSettings.Builder().setClearViewsEnabled(true).setClearViewsEnabled(true).setTransparencyEnabled(true).setCompressFormat(Bitmap.CompressFormat.PNG).setCompressQuality(100).build(), new OnSaveBitmap() {
            public void onFailure(Exception exc) {
            }

            public void onBitmapReady(Bitmap bitmap) {
                if (AppPref.getImageFormat(EditImageActivity.this).equalsIgnoreCase("JPG") || AppPref.getImageFormat(EditImageActivity.this).equalsIgnoreCase("JPEG")) {
                    EditImageActivity.this.extension = "jpg";
                } else {
                    EditImageActivity.this.extension = "png";
                }
                cropUri = storeImage("SourceTempFile." + EditImageActivity.this.extension, bitmap);
                EditImageActivity.this.hideProgressBar();
                Log.e("@uri=>", "" + cropUri);

                FragmentManager mFragmentManager = getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                CropFragment cropFragment = new CropFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString("uri",cropUri.toString());
                cropFragment.setArguments(mBundle);
                mFragmentTransaction.add(R.id.frameLayout, cropFragment).commit();


            }

        });
    }

    
    public void saveImage() {
        showProgressBar();
        this.mSaveFileHelper.createFile(System.currentTimeMillis() + ".png", new FileSaveHelper.OnFileCreateResult() {
            public final void onFileCreateResult(boolean z, String str, String str2, Uri uri) {

                if (z) {
                    mPhotoEditor.saveAsBitmap(new SaveSettings.Builder().setClearViewsEnabled(true).setTransparencyEnabled(true).setCompressQuality(100).build(), new OnSaveBitmap() {
                        public void onBitmapReady(Bitmap bitmap) {
                            EditImageActivity.this.binding.photoEditor.getSource().setImageBitmap(bitmap);
                            EditImageActivity.this.hideProgressBar();
                            EditImageActivity.this.isBeenSaved = true;
                            EditImageActivity editImageActivity = EditImageActivity.this;
                            editImageActivity.path = editImageActivity.saveInGallery(bitmap);
                            AppConstants.deleteTempFile(EditImageActivity.this);
                            if (EditImageActivity.this.isFromGallery) {
                                EditImageActivity.this.onSaveTaskDone(EditImageActivity.this.path);
                                return;
                            }
                            EditImageActivity editImageActivity2 = EditImageActivity.this;
                            editImageActivity2.onSaveTaskDone(editImageActivity2.path);
                        }

                        public void onFailure(Exception exc) {
                            EditImageActivity.this.hideProgressBar();
                            Toast.makeText(EditImageActivity.this, "Failed To save", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                hideProgressBar();
            }
        });
    }


    
    public Bitmap uriToBitmap(Uri uri) {
        try {
            ParcelFileDescriptor openFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor());
            openFileDescriptor.close();
            return decodeFileDescriptor;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public void onSaveTaskDone(String str) {
        Intent intent = new Intent();
        if (this.isBeenSaved) {
            intent.putExtra(FILE_PATH, str);
            intent.putExtra("isBeenSaved", this.isBeenSaved);
        }
        setResult(-1, intent);
        finish();
    }

    
    public String saveInGallery(Bitmap bitmap2) {
        String str = AppPref.getPrefix(this) + "_" + System.currentTimeMillis();
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.extension = "jpeg";
            this.format = Bitmap.CompressFormat.JPEG;
        } else {
            this.extension = "png";
            this.format = Bitmap.CompressFormat.PNG;
        }

           String img_path = Constants.imagePath(EditImageActivity.this);
            File file = new File(img_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, AppPref.getPrefix(this) + "_" + System.currentTimeMillis() + "." + this.extension);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap2.compress(this.format, AppPref.getImageQuality(this), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            AppConstants.refreshFiles(this, file2);
            this.iamgePath = file2.getAbsolutePath();
//        }
        if (GalleryActivity.getInstance() != null) {
            GalleryActivity.getInstance().addData(new DiaryImageData(this.iamgePath, str, System.currentTimeMillis()));
        }
        return this.iamgePath;
    }

    private void setViewInteract(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            int i = 0;
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i < viewGroup.getChildCount()) {
                    setViewInteract(viewGroup.getChildAt(i), z);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void showProgressBar() {
        setViewInteract(this.binding.rootView, false);
        this.binding.progressBar.setVisibility(View.VISIBLE);
    }

    
    public void hideProgressBar() {
        setViewInteract(this.binding.rootView, true);
        this.binding.progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Log.e("onActivity", "=> " + resultUri.toString());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("onActivity", "=> " + cropError.toString());
        }
    }



    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_adpative_banner_Ad(this, mAdView);
                }
            }
        } else {
            custom_banner_ad.reload_applovin_banner_ad(this, mAdView);
        }

    }

    private void mNativeBanner() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1") || SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_banner_1);
            findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.mNativeBannerAd).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        } else {
            new Custom_NativeAd_Admob().showAppLovinNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        }

    }
}
