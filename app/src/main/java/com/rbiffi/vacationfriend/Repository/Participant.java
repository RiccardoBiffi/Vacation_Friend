package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("email")})
public class Participant {

    @PrimaryKey
    @NonNull
    public String email;

    public int vacationId;

    @NonNull
    public String name;
    @NonNull
    public String lastName;

    //@NonNull
    //public Bitmap picture;

}