package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("email")})
public class Participant {

    @PrimaryKey
    @NonNull
    public final String email;

    @NonNull
    public final Uri picture;
    @NonNull
    public final String name;
    @NonNull
    public final String lastName;

    public Participant(@NonNull String email, @NonNull Uri picture, @NonNull String name, @NonNull String lastName) {
        this.email = email;
        this.picture = picture;
        this.name = name;
        this.lastName = lastName;
    }
}
