package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Helper;

import android.hardware.Camera;

import java.util.List;

public class DevSpy_CameraHelper {
    public static Camera.Size getOptimalVideoSize(List<Camera.Size> list, List<Camera.Size> list2, int i, int i2) {
        List<Camera.Size> list3 = list2;
        int i3 = i2;
        double d = (double) i;
        double d2 = (double) i3;
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        List<Camera.Size> list4 = list != null ? list : list3;
        Camera.Size size = null;
        double d4 = Double.MAX_VALUE;
        double d5 = Double.MAX_VALUE;
        for (Camera.Size next : list4) {
            double d6 = (double) next.width;
            double d7 = (double) next.height;
            Double.isNaN(d6);
            Double.isNaN(d7);
            Double.isNaN(d6);
            Double.isNaN(d7);
            if (Math.abs((d6 / d7) - d3) <= 0.1d && ((double) Math.abs(next.height - i3)) < d5 && list3.contains(next)) {
                d5 = (double) Math.abs(next.height - i3);
                size = next;
            }
        }
        if (size == null) {
            for (Camera.Size next2 : list4) {
                if (((double) Math.abs(next2.height - i3)) < d4 && list3.contains(next2)) {
                    d4 = (double) Math.abs(next2.height - i3);
                    size = next2;
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
