package com.rbiffi.vacationfriend.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface IParticipantDao {

//READ
    @Query("SELECT * FROM Participant ORDER BY lastName ASC")
    List<Participant> getAllPartecipants();

    @Query("SELECT * from Participant WHERE vacationId = :vacationId")
    LiveData<List<Participant>> getParticipants(int vacationId);

//INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Participant participant);

//UPDATE
    @Update
    void updatePartecipant(Participant participant);

//DELETE
    @Delete
    void delete(Participant participant);
}
