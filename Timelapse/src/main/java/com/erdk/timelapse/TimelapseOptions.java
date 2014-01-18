package com.erdk.timelapse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by erdk on 18.01.14.
 */
class TimelapseOptions implements Parcelable {
    private int mDuration;
    private int mInterval;
    private int mFPS;

    public static final Creator<TimelapseOptions> CREATOR = new Creator<TimelapseOptions>() {
        @Override
        public TimelapseOptions createFromParcel(Parcel source) {
            return new TimelapseOptions(source);
        }

        @Override
        public TimelapseOptions[] newArray(int size) {
            return new TimelapseOptions[size];
        }
    };

    public TimelapseOptions() {
        mDuration = 1;
        mInterval = 1;
        mFPS = 30;
    }

    public TimelapseOptions(int pDuration, int pInterval, int pFPS) {
        mDuration = pDuration;
        mInterval = pInterval;
        mFPS = pFPS;
    }

    private TimelapseOptions(Parcel in) {
        mDuration = in.readInt();
        mInterval = in.readInt();
        mFPS = in.readInt();
    }

    public int getDuration() {
        return mDuration;
    }

    public int getInterval() {
        return mInterval;
    }

    public int getFPS() {
        return mFPS;
    }

    public void setDuration(int pDuration) {
        mDuration = pDuration;
    }

    public void setInterval(int pInterval) {
        mInterval = pInterval;
    }

    public void setFPS(int pFPS) {
        mFPS = pFPS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDuration);
        dest.writeInt(mInterval);
        dest.writeInt(mFPS);
    }
}
