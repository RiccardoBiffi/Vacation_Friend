package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.util.Date;

@Entity(indices = {@Index("id")})
public class Vacation{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String name;
    @NonNull
    public Date startDate;
    @NonNull
    public Date endDate;

    //@Relation(parentColumn = "id", entityColumn = "vacationId")
    //public List<Participant> participants;
    public Uri photo;
    public String note;
}

