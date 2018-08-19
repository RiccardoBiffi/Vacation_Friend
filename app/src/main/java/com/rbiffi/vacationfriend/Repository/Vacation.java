package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Utils.Constants;

import java.util.Arrays;
import java.util.List;

@Entity(indices = {@Index("id")})
public class Vacation implements IUserEditableObject {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String title;

    @NonNull
    @Embedded
    public Period period;

    public String place;

    //@Relation(parentColumn = "id", entityColumn = "vacationId")
    //public List<Participant> participants;

    public Uri photo;
    public String notes;
    public Boolean isAchieved;


    @Ignore
    @Override
    public List<String> getEditableFields() {
        return Arrays.asList(
                Constants.F_TITLE,
                Constants.F_PERIOD,
                Constants.F_PLACE,
                Constants.F_PARTECIP,
                Constants.F_PHOTO,
                Constants.F_NOTES);
    }
}

