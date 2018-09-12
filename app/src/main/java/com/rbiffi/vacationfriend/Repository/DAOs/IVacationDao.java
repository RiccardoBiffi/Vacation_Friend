package com.rbiffi.vacationfriend.Repository.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;

import java.util.List;

@Dao
public interface IVacationDao {

//READ
    @Query("SELECT * FROM Vacation WHERE id = :id")
    Vacation getVacationDetails(long id);

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE date('now') BETWEEN startDate AND endDate " +
            "AND isAchieved = 0")
    LiveData<List<VacationLite>> getCurrentVacations();

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE date('now') < startDate " +
            "AND isAchieved = 0")
    LiveData<List<VacationLite>> getNextVacations();

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE date('now') > endDate " +
            "AND isAchieved = 0")
    LiveData<List<VacationLite>> getEndedVacations();

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE isAchieved = 1 " +
            "ORDER BY endDate DESC")
    LiveData<List<VacationLite>> getAchievedVacations();

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE isAchieved = 0 " +
            "ORDER BY title ASC")
    LiveData<List<VacationLite>> getActiveVacations();


//INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Vacation vacation);


//UPDATE
    @Update
    void update(Vacation vacation);

    @Query("UPDATE Vacation " +
            "SET isAchieved = 1 " +
            "WHERE id = :vacationId")
    void storeFromID(long vacationId);

    @Query("UPDATE Vacation " +
            "SET isAchieved = 0 " +
            "WHERE id = :vacationId")
    void unstoreFromID(Long vacationId);

//DELETE

    @Delete
    void delete(Vacation vacation);

    @Query("DELETE " +
            "FROM Vacation " +
            "WHERE id = :vacationId ")
    void deleteFromID(long vacationId);

    @Query("DELETE FROM Vacation")
    void deleteAll();
}
