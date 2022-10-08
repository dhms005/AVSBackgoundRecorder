package com.ds.audio.video.screen.backgroundrecorder.exit;


import org.json.JSONObject;

/***
 * This Interface is used for the Callback listner for the API call
 */
public class CY_M_OnTaskCompleted {


    public interface CallBackListener {
        void onTaskCompleted(JSONObject result);

        void onTaskCompleted(JSONObject result, String Method);

        void onTaskCompleted(Boolean result);

    }

    CallBackListener listener;

    public CY_M_OnTaskCompleted(CallBackListener listener) {
        this.listener = listener;
    }

    public void onTaskCompleted(JSONObject result) {
        listener.onTaskCompleted(result);
    }

    public void onTaskCompleted(JSONObject result, String Method) {
        listener.onTaskCompleted(result, Method);
    }

    public void onTaskCompleted(Boolean result) {
        listener.onTaskCompleted(result);
    }

}
