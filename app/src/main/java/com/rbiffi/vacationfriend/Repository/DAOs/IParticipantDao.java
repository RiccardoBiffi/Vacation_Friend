package com.rbiffi.vacationfriend.Repository.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.List;

@Dao
public interface IParticipantDao {

//READ
    @Query("SELECT * FROM Participant ORDER BY lastName ASC")
    LiveData<List<Participant>> getAllParticipants();

//INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Participant participant);

//UPDATE
    @Update
    void update(Participant participant);

//DELETE
    @Delete
    void delete(Participant participant);
}
