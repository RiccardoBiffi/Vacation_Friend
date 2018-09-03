package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class Period implements Parcelable {
    public String startDate;
    public String endDate;

    public Period(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    // per rendere l'oggetto Parcelable

    protected Period(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
    }

    public static final Creator<Period> CREATOR = new Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);
    }
}
