package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("email")},
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "id",
                childColumns = "vacationId",
                onDelete = CASCADE
        )
)
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

    public Integer vacationId;

}
