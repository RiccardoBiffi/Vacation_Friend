package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("email")})
public class Participant {

    @PrimaryKey
    @NonNull
    public String email;

    @NonNull
    public Uri picture;
    @NonNull
    public String name;
    @NonNull
    public String lastName;

    //da usare come foreignkey per la relazione con la vacanza
    public int vacationId;

}
