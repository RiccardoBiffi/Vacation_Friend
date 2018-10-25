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
import com.rbiffi.vacationfriend.Repository.DAOs.IJoinVacationParticipantDao;
import com.rbiffi.vacationfriend.Repository.DAOs.IParticipantDao;
import com.rbiffi.vacationfriend.Repository.DAOs.IVacationDao;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Period;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Utils.Converters;


@Database(
        entities = {
                Vacation.class,
                Participant.class,
                JoinVacationParticipant.class
        },
        version = 20,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class VacationFriendDatabase extends RoomDatabase {

    private static VacationFriendDatabase INSTANCE;
    private static RoomDatabase.Callback roomDbCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    public static VacationFriendDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationFriendDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), VacationFriendDatabase.class, "VacationDB")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDbCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract IVacationDao getVacationDao();
    public abstract IParticipantDao getParticipantDao();

    public abstract IJoinVacationParticipantDao getJoinVacationParticipantDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final IVacationDao vDao;
        private final IParticipantDao pDao;
        private final IJoinVacationParticipantDao jvpDao;

        PopulateDbAsync(VacationFriendDatabase db) {
            vDao = db.getVacationDao();
            pDao = db.getParticipantDao();
            jvpDao = db.getJoinVacationParticipantDao();
        }

        @Override
        protected Void doInBackground(final Void... vacations) {
            vDao.deleteAll();

            initializeVacations();
            initializeParticipants();
            return null;
        }

        private void initializeVacations() {
            Period p = new Period("30/08/2018", "15/09/2018");
            Uri photo = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.vacation_photo);
            Vacation v = new Vacation("Croazia 2018", p, "Rovigno", photo, false);
            vDao.insert(v);

            p = new Period("25/08/2017", "02/09/2017");
            photo = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.vacation_photo1);
            v = new Vacation("Croazia 2017", p, "Rovigno", photo, true);
            vDao.insert(v);

            p = new Period("25/08/2019", "02/09/2019");
            photo = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.vacation_photo2);
            v = new Vacation("Croazia 2019", p, "Rovigno", photo, true);
            vDao.insert(v);
        }

        private void initializeParticipants() {
            Uri photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.blonde_woman);
            Participant p = new Participant("test1@domain.com", photoUri, "Sara", "Manzini");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.fine_woman);
            p = new Participant("test2@domain.com", photoUri, "Chiara", "Rocchi");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.happy_man);
            p = new Participant("test3@domain.com", photoUri, "Carlo", "Rossi");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.businnes_man);
            p = new Participant("test4@domain.com", photoUri, "Marco", "Scalzi");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.black_man);
            p = new Participant("test5@domain.com", photoUri, "Andre", "Smith");
            pDao.insert(p);
        }
    }

}
