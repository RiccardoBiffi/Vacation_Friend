package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Period implements Parcelable {

    public Date startDate;
    public Date endDate;

    public Period(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Period(String startDate, String endDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.startDate = format.parse(startDate);
            this.endDate = format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // per rendere l'oggetto Parcelable

    protected Period(Parcel in) {
        // Leggo il long e lo converto in data
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
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
        // Passo il Long della data. Lo converto quando lo leggo.
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
    }


}
