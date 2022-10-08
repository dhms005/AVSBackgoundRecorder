package com.hbisoft.hbrecorder;

public interface HBRecorderListener {
    void HBRecorderOnComplete();

    void HBRecorderOnError(int i, String str);

    void HBRecorderOnStart();
}
