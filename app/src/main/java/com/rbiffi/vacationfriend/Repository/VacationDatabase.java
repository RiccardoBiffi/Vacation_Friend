package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {
        Vacation.class,
        Participant.class}, version = 1)
public abstract class VacationDatabase extends RoomDatabase {

    private static VacationDatabase INSTANCE;

    public abstract IVacationDao getVacationDao();

    public static VacationDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (VacationDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabase.class,"VacationDB").build();
                }
            }
        }
        return INSTANCE;
    }

}
