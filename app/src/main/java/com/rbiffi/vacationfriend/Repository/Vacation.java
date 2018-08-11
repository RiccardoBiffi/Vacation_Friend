package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import java.sql.Date;
import java.util.List;

@Entity(indices = {@Index("id")})
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public Date startDate;
    public Date endDate;
    public List<Participant> participants;
    public Bitmap photo;
    public String note;
}
