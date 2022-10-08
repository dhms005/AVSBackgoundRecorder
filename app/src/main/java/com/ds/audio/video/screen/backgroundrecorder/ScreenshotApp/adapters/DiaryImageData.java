package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class DiaryImageData implements Parcelable {
    public static final Creator<DiaryImageData> CREATOR = new Creator<DiaryImageData>() {
        public DiaryImageData createFromParcel(Parcel parcel) {
            return new DiaryImageData(parcel);
        }

        public DiaryImageData[] newArray(int i) {
            return new DiaryImageData[i];
        }
    };
    long date;
    String name;
    String path;
    long size;

    public int describeContents() {
        return 0;
    }

    public DiaryImageData(String path, String name, long date, long size) {
        this.path = path;
        this.name = name;
        this.date = date;
        this.size = size;
    }

    public DiaryImageData(String str, String str2, long j) {
        this.path = str;
        this.name = str2;
        this.date = j;
    }

    protected DiaryImageData(Parcel parcel) {
        this.path = parcel.readString();
        this.name = parcel.readString();
        this.date = parcel.readLong();
        this.size = parcel.readLong();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeString(this.name);
        parcel.writeLong(this.date);
        parcel.writeLong(this.size);
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.path.equals(((DiaryImageData) obj).path);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.path});
    }
}
