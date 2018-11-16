package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.rbiffi.vacationfriend.Utils.Converters;

import java.util.Date;


public class ResolutionItem implements Parcelable {

    private final Uri payerIcon;
    private final String title;
    private final String sign;
    private final String value;
    private Date date;

    public ResolutionItem(Uri payerIcon, String title, String sign, String value, Date date) {
        this.payerIcon = payerIcon;
        this.title = title;
        this.sign = sign;
        this.value = value;
        this.date = date;
    }

    private ResolutionItem(Parcel in) {
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
        assert dateString != null;
        return dateString.substring(0, dateString.length() - 5);
    }
}
