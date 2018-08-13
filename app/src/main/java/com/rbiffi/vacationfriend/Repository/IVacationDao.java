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
public interface IVacationDao {

    //READ
    @Query("SELECT * FROM Vacation WHERE id = :id")
    Vacation getVacationDetails(int id);

    @Query("SELECT * FROM Vacation WHERE name < :startDate")
    List<VacationLite> getNextVacations(String startDate);

    @Query("SELECT * FROM Vacation WHERE name BETWEEN :startDate AND :endDate")
    List<VacationLite> getCurrentVacations(String startDate, String endDate);

    @Query("SELECT * FROM Vacation WHERE name > :endDate")
    List<VacationLite> getEndedVacations(String endDate);

    @Query("SELECT * from Vacation ORDER BY name ASC")
    LiveData<List<VacationLite>> getAllVacations();


    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Vacation vacation);


    //UPDATE
    @Update
    void updateVacation(Vacation vacation);


    //DELETE
    @Delete
    void deleteVacation(Vacation vacation);

    @Query("DELETE FROM Vacation")
    void deleteAll();

}
