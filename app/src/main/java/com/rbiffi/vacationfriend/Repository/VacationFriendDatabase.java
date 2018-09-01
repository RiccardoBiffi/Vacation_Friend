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
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Utils.Converters;

@Database(
        entities = {
                Vacation.class,
                Participant.class,
                JoinVacationParticipant.class
        },
        version = 17,
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
            /*
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            Vacation v = new Vacation();
            v.title = "Croazia 2018";
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
            v.notes = "Prima prova";
            v.achived = false;
            vDao.insert(v);

            v = new Vacation();
            v.title = "Ferragosto 2018";
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
            v.notes = "Seconda prova";
            v.achived = false;
            vDao.insert(v);
            */
        }

        private void initializeParticipants() {
            Uri photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.blonde_woman);
            Participant p = new Participant("test1@domani.com", photoUri, "Sara", "Manzini");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.fine_woman);
            p = new Participant("test2@domani.com", photoUri, "Chiara", "Rocchi");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.happy_man);
            p = new Participant("test3@domani.com", photoUri, "Carlo", "Rossi");
            pDao.insert(p);

            photoUri = Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.businnes_man);
            p = new Participant("test4@domani.com", photoUri, "Marco", "Scalzi");
            pDao.insert(p);
        }
    }

}
