package com.rbiffi.vacationfriend.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IVacationDao {

    @Query("SELECT * from Vacation ORDER BY name ASC")
    LiveData<List<Vacation>> getAllVacations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Vacation vacation);

    @Query("DELETE FROM Vacation")
    void deleteAll();
}
