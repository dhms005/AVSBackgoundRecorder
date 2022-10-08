package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.audio.video.screen.backgroundrecorder.Utils.cropper.CropImage;
import com.ds.audio.video.screen.backgroundrecorder.Utils.cropper.CropImageView;
import com.ds.audio.video.screen.backgroundrecorder.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CropFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CropFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CropImageView cropImageView;
    private String cropUri;

    public CropFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CropFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CropFragment newInstance(String param1, String param2) {
        CropFragment fragment = new CropFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crop, container, false);
        cropImageView = v.findViewById(R.id.cropImageView);

        Bundle bundle = getArguments();
        cropUri = bundle.getString("uri","");


        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity().start(getActivity());
//        cropImageView.setImageUriAsync(Uri.parse(cropUri));


//        cropImageView.setImageUriAsync(Uri.parse(cropUri));
//
//        //start crop image
//           CropImage.activity(Uri.parse(cropUri)).start(getActivity());
//        //set image
//          cropImageView.setImageUriAsync(Uri.parse(cropUri));


        return v;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                Log.e("@cropfrg", "" + resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Log.e("@cropfrg", "" + error);
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                cropImageView.getCroppedImageAsync();
// or
                Bitmap cropped = cropImageView.getCroppedImage();
                cropImageView.setImageBitmap(cropped);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}