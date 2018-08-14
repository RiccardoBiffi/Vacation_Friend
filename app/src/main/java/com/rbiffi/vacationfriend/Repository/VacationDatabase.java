package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {
        Vacation.class,
        Participant.class}, version = 1)
public abstract class VacationDatabase extends RoomDatabase {

    private static VacationDatabase INSTANCE;
    private static RoomDatabase.Callback roomDbCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public abstract IVacationDao getVacationDao();

    public static VacationDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (VacationDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), VacationDatabase.class,"VacationDB")
                            .addCallback(roomDbCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{

        private final IVacationDao vDao;

        public PopulateDbAsync(VacationDatabase db) {
            vDao = db.getVacationDao();
        }

        @Override
        protected Void doInBackground(final Void... vacations) {
            vDao.deleteAll();

            Vacation v = new Vacation();
            v.name = "Croazia 2018";
            v.note = "Prima prova";
            vDao.insert(v);

            v = new Vacation();
            v.name = "Ferragosto 2018";
            v.note = "Seconda prova";
            vDao.insert(v);

            return null;
        }
    }
}
