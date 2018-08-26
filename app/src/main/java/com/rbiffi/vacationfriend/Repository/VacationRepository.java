package com.rbiffi.vacationfriend.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rbiffi.vacationfriend.Utils.VacationLite;

import java.util.List;

//classe per gestire le sorgenti di dati. Utile a mettere in un unico luogo tutte risorse dati.
public class VacationRepository {
    private IVacationDao vacationDao;
    private LiveData<List<VacationLite>> vacationList;

    private IParticipantDao participantDao;
    private LiveData<List<Participant>> participantList;

    public VacationRepository(Application app) {
        VacationFriendDatabase db = VacationFriendDatabase.getDatabase(app); // prendo l'istanza del db
        vacationDao = db.getVacationDao(); // prendo dal db il DAO
        participantDao = db.getParticipantDao();

        // acquisisco quel che mi interessa dal db
        vacationList = vacationDao.getAllVacations();
        participantList = participantDao.getAllPartecipants();
    }

    public LiveData<List<VacationLite>> getVacationList() {
        return vacationList;
    }

    public LiveData<List<Participant>> getParticipantList() {
        return participantList;
    }

    public void insert(Vacation vacation) {
        // bisogna chiamarlo in un tread diverso da quello principale.
        new insertAsyncTask(vacationDao).execute(vacation);
    }

    // classe interna per la gestione dei task, asincroni rispetto l'UI.
    private class insertAsyncTask extends AsyncTask<Vacation, Void, Void> {

        private IVacationDao asyncDao;

        public insertAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(final Vacation... vacations) {
            asyncDao.insert(vacations[0]);
            return null;
        }
    }
}
