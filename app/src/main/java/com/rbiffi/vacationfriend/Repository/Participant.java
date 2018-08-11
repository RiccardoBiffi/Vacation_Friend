package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

@Entity(indices = {@Index("email")})
public class Participant {

    @PrimaryKey
    public String email;

    public String name;
    public String lastName;
    public Bitmap picture;

}
