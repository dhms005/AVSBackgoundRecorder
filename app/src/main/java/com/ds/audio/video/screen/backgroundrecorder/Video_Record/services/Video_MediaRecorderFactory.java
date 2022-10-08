package com.ds.audio.video.screen.backgroundrecorder.Video_Record.services;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_CameraHelper;

public class Video_MediaRecorderFactory {
    public static CamcorderProfile setCamcorderProfile(String str) {
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt == 0) {
                return CamcorderProfile.get(1);
            }
            if (parseInt == 1) {
                return CamcorderProfile.get(4);
            }
            if (parseInt != 2) {
                return CamcorderProfile.get(1);
            }
            return CamcorderProfile.get(0);
        } catch (Exception unused) {
            return CamcorderProfile.get(1);
        }
    }

    public static Camera setVideoCamera(String str) {
        Camera camera;
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt == 0) {
                camera = Video_CameraHelper.getDefaultBackFacingCameraInstance();
            } else if (parseInt != 1) {
                camera = Video_CameraHelper.getDefaultCameraInstance();
            } else {
                camera = Video_CameraHelper.getDefaultFrontFacingCameraInstance();
            }
        } catch (Exception unused) {
            camera = Video_CameraHelper.getDefaultCameraInstance();
        }
        return camera == null ? Video_CameraHelper.getDefaultCameraInstance() : camera;
    }

    public static MediaRecorder setAudioSource(MediaRecorder mediaRecorder, String str) {
        int parseInt = Integer.parseInt(str);
        if (parseInt == 0) {
            mediaRecorder.setAudioSource(1);
        } else if (parseInt == 1) {
            mediaRecorder.setAudioSource(5);
        } else if (parseInt != 2) {
            mediaRecorder.setAudioSource(5);
        } else {
            mediaRecorder.setAudioSource(0);
        }
        return mediaRecorder;
    }

    public static MediaRecorder setSource(MediaRecorder mediaRecorder, boolean z, String str, CamcorderProfile camcorderProfile) {
        if (z) {
            mediaRecorder.setVideoSource(1);
            mediaRecorder.setOutputFormat(2);
            mediaRecorder.setVideoFrameRate(camcorderProfile.videoFrameRate);
            mediaRecorder.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            mediaRecorder.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
            mediaRecorder.setVideoEncoder(camcorderProfile.videoCodec);
            return mediaRecorder;
        }
        MediaRecorder.getAudioSourceMax();
        MediaRecorder audioSource = setAudioSource(mediaRecorder, str);
        audioSource.setVideoSource(1);
        audioSource.setOutputFormat(2);
        audioSource.setVideoFrameRate(camcorderProfile.videoFrameRate);
        audioSource.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        audioSource.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
        audioSource.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
        audioSource.setAudioChannels(camcorderProfile.audioChannels);
        audioSource.setAudioSamplingRate(camcorderProfile.audioSampleRate);
        audioSource.setVideoEncoder(camcorderProfile.videoCodec);
        audioSource.setAudioEncoder(camcorderProfile.audioCodec);
        return audioSource;
    }

    public static MediaRecorder setOrientation(MediaRecorder mediaRecorder, String str, String str2) {
        int parseInt = Integer.parseInt(str);
        if (Integer.parseInt(str2) == 1) {
            if (parseInt == 0) {
                mediaRecorder.setOrientationHint(270);
            } else if (parseInt == 1) {
                mediaRecorder.setOrientationHint(270);
            } else if (parseInt == 2) {
                mediaRecorder.setOrientationHint(360);
            } else if (parseInt == 3) {
                mediaRecorder.setOrientationHint(90);
            } else if (parseInt == 4) {
                mediaRecorder.setOrientationHint(180);
            }
        } else if (parseInt == 0) {
            mediaRecorder.setOrientationHint(90);
        } else if (parseInt == 1) {
            mediaRecorder.setOrientationHint(90);
        } else if (parseInt == 2) {
            mediaRecorder.setOrientationHint(180);
        } else if (parseInt == 3) {
            mediaRecorder.setOrientationHint(270);
        } else if (parseInt == 4) {
            mediaRecorder.setOrientationHint(360);
        }
        return mediaRecorder;
    }
}
