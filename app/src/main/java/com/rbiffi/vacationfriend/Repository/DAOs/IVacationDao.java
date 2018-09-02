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
            "WHERE date('now') < :startDate")
    List<VacationLite> getNextVacations(String startDate);

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE date('now') BETWEEN :startDate AND :endDate")
    List<VacationLite> getCurrentVacations(String startDate, String endDate);

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE date('now') > :endDate")
    List<VacationLite> getEndedVacations(String endDate);

    @Query("SELECT id, title, startDate, endDate, photo " +
            "FROM Vacation " +
            "WHERE isAchieved = 1")
    List<VacationLite> getAchievedVacations();

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
