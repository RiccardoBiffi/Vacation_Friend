package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Vacation {

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

}

