package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper;

import android.hardware.Camera;

import java.util.List;

public class Video_CameraHelper {
    public static Camera.Size getOptimalVideoSize(List<Camera.Size> list, List<Camera.Size> list2, int i, int i2) {
        double d = (double) i;
        double d2 = (double) i2;
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        List<Camera.Size> list3 = list != null ? list : list2;
        Camera.Size size = null;
        double d4 = Double.MAX_VALUE;
        double d5 = Double.MAX_VALUE;
        for (Camera.Size size2 : list3) {
            double d6 = (double) size2.width;
            double d7 = (double) size2.height;
            Double.isNaN(d6);
            Double.isNaN(d7);
            Double.isNaN(d6);
            Double.isNaN(d7);
            if (Math.abs((d6 / d7) - d3) <= 0.1d && ((double) Math.abs(size2.height - i2)) < d5 && list2.contains(size2)) {
                d5 = (double) Math.abs(size2.height - i2);
                size = size2;
            }
        }
        if (size == null) {
            for (Camera.Size size3 : list3) {
                if (((double) Math.abs(size3.height - i2)) < d4 && list2.contains(size3)) {
                    d4 = (double) Math.abs(size3.height - i2);
                    size = size3;
                }
            }
        }
        return size;
    }

    public static Camera getDefaultCameraInstance() {
        return Camera.open();
    }

    public static Camera getDefaultBackFacingCameraInstance() {
        return getDefaultCamera(0);
    }

    public static Camera getDefaultFrontFacingCameraInstance() {
        return getDefaultCamera(1);
    }

    private static Camera getDefaultCamera(int i) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i2 = 0; i2 < numberOfCameras; i2++) {
            Camera.getCameraInfo(i2, cameraInfo);
            if (cameraInfo.facing == i) {
                return Camera.open(i2);
            }
        }
        return null;
    }
}
