package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Vacation implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public final String title;

    @NonNull
    @Embedded
    public final Period period;

    public final String place;

    public final Uri photo;

    public final Boolean isAchieved;

    public Vacation(@NonNull String title, @NonNull Period period, String place, Uri photo, Boolean isAchieved) {
        this.title = title;
        this.period = period;
        this.place = place;
        this.photo = photo;
        this.isAchieved = isAchieved;
    }


    // per rendere l'oggetto Parcelable

    protected Vacation(Parcel in) {
        id = in.readLong();
        title = in.readString();
        period = in.readParcelable(Period.class.getClassLoader());
        place = in.readString();
        photo = in.readParcelable(Uri.class.getClassLoader());
        byte tmpIsAchieved = in.readByte();
        isAchieved = tmpIsAchieved == 0 ? null : tmpIsAchieved == 1;
    }

    @Ignore
    public static final Creator<Vacation> CREATOR = new Creator<Vacation>() {
        @Override
        public Vacation createFromParcel(Parcel in) {
            return new Vacation(in);
        }

        @Override
        public Vacation[] newArray(int size) {
            return new Vacation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeParcelable(period, flags);
        dest.writeString(place);
        dest.writeParcelable(photo, flags);
        dest.writeByte((byte) (isAchieved == null ? 0 : isAchieved ? 1 : 2));
    }
}

