package com.rbiffi.vacationfriend.Repository;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.Converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Database(entities = {
        Vacation.class,
        Participant.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class VacationDatabase extends RoomDatabase {

    private static VacationDatabase INSTANCE;
    private static RoomDatabase.Callback roomDbCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static VacationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), VacationDatabase.class, "VacationDB")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDbCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract IVacationDao getVacationDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final IVacationDao vDao;

        public PopulateDbAsync(VacationDatabase db) {
            vDao = db.getVacationDao();
        }

        @Override
        protected Void doInBackground(final Void... vacations) {
            vDao.deleteAll();

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            Vacation v = new Vacation();
            v.name = "Croazia 2018";
            String start = "25/08/2017";
            String end = "02/09/2017";
            try {
                v.startDate = format.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                v.endDate = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            v.photo = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.vacation_photo);
            v.note = "Prima prova";
            vDao.insert(v);

            v = new Vacation();
            v.name = "Ferragosto 2018";
            start = "12/08/2017";
            end = "17/08/2017";
            try {
                v.startDate = format.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                v.endDate = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            v.photo = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.vacation_photo1);
            v.note = "Seconda prova";
            vDao.insert(v);

            return null;
        }
    }

}
