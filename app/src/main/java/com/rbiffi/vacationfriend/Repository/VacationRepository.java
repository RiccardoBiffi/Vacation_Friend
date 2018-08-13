package com.rbiffi.vacationfriend.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

//classe per gestire le sorgenti di dati. Utile a mettere in un unico luogo tutte risorse dati.
public class VacationRepository {
    private IVacationDao vacationDao;
    private LiveData<List<VacationLite>> vacationList;

    public VacationRepository(Application app){
        VacationDatabase db = VacationDatabase.getDatabase(app); // prendo l'istanza del db
        vacationDao = db.getVacationDao(); // prendo dal db il DAO

        // acquisisco quel che mi interessa dal db
        vacationList = vacationDao.getAllVacations();
    }

    public LiveData<List<VacationLite>> getVacationList() {
        return vacationList;
    }

    public void insert(Vacation vacation){
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
