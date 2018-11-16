package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

public class Stop extends RouteElement implements Parcelable {

    private long vacationId;

    public final String title;

    public final String place;

    @NonNull
    public Date day;

    @NonNull
    public StopTimeArrDep stopTime;

    public final Uri stopIcon;

    private final String notes;

    public Stop(String title, String place, @NonNull Date day, @NonNull StopTimeArrDep stopTime, Uri stopIcon, String notes) {
        this.title = title;
        this.place = place;
        this.day = day;
        this.stopTime = stopTime;
        this.stopIcon = stopIcon;
        this.notes = notes;
    }

    protected Stop(Parcel in) {
        vacationId = in.readLong();
        title = in.readString();
        place = in.readString();
        stopIcon = in.readParcelable(Uri.class.getClassLoader());
        notes = in.readString();
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>() {
        @Override
        public Stop createFromParcel(Parcel in) {
            return new Stop(in);
        }

        @Override
        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(vacationId);
        dest.writeString(title);
        dest.writeString(place);
        dest.writeParcelable(stopIcon, flags);
        dest.writeString(notes);
    }
}
