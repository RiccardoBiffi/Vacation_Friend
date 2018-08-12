package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.sql.Date;
import java.util.List;

@Entity(indices = {@Index("id")})
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String name;

    //todo i tipi di dato commentati vanno convertiti in qualche modo prima di essere usati.
    // forse si può fare lo stesso con colonne contenenti liste di oggetti
    /*@NonNull
    public Date startDate;
    @NonNull
    public Date endDate;

    //@Relation(parentColumn = "id", entityColumn = "vacationId")
    //public List<Participant> participants;
    public Bitmap photo;
    */
    public String note;
}
