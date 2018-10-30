package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.rbiffi.vacationfriend.Utils.Converters;

import java.sql.Date;

public class ResolutionItem implements Parcelable {

    private Uri payerIcon;
    private String title;
    private String sign;
    private String value;
    private Date date;

    protected ResolutionItem(Parcel in) {
        payerIcon = in.readParcelable(Uri.class.getClassLoader());
        title = in.readString();
        sign = in.readString();
        value = in.readString();
    }

    public static final Creator<ResolutionItem> CREATOR = new Creator<ResolutionItem>() {
        @Override
        public ResolutionItem createFromParcel(Parcel in) {
            return new ResolutionItem(in);
        }

        @Override
        public ResolutionItem[] newArray(int size) {
            return new ResolutionItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(payerIcon, flags);
        dest.writeString(title);
        dest.writeString(sign);
        dest.writeString(value);
    }

    public Uri getPayerIcon() {
        return payerIcon;
    }

    public String getExpenseTitle() {
        return title;
    }

    public String getExpenseSign() {
        return sign;
    }

    public String getExpenseValue() {
        return value;
    }

    public String getExpenseDate() {
        String dateString = Converters.dateToUserInterface(date);
        return dateString.substring(0, dateString.length() - 6);
    }
}
