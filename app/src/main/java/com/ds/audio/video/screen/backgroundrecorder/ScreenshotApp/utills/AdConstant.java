package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

public class AdConstant {
    public static String AD_AppOpen = "strADAppOpen()";
    public static String AD_Banner = "strADBANNER()";
    public static String AD_Full = "strADFULL()";
    public static String AD_Native = "strAdNative()";
    public static String AD_baseUrl = "StrbaseUrl()";
    public static int adCount = 0;
    public static int adLimit = 3;
    public static boolean npaflag = false;
    public static String[] publisherIds = {"strPUBLISHERID()"};

    private static native String StrbaseUrl();

    private static native String strADAppOpen();

    private static native String strADBANNER();

    private static native String strADFULL();

    private static native String strAdNative();

    private static native String strPUBLISHERID();

    static {
        System.loadLibrary("native-lib");
    }



}
