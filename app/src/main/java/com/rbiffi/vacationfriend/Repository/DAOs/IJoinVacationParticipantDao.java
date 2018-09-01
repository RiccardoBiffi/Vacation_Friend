package com.rbiffi.vacationfriend.Repository.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.List;

@Dao
public interface IJoinVacationParticipantDao {

    //READ
    @Query("SELECT email, picture, name, lastName " +
            "FROM Participant " +
            "INNER JOIN JoinVacationParticipant " +
            "ON Participant.email = JoinVacationParticipant.userEmail " +
            "WHERE JoinVacationParticipant.vacationId = :id")
    List<Participant> getParticipantsForVacation(int id);


    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinVacationParticipant jvp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertList(List<JoinVacationParticipant> jvps);


    //UPDATE
    @Update
    void update(JoinVacationParticipant jvp);


    //DELETE
    @Delete
    void delete(JoinVacationParticipant jvp);

}
