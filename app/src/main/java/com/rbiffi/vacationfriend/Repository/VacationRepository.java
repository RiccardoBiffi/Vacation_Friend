package com.rbiffi.vacationfriend.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rbiffi.vacationfriend.Repository.DAOs.IParticipantDao;
import com.rbiffi.vacationfriend.Repository.DAOs.IVacationDao;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;

import java.util.List;

//classe per gestire le sorgenti di dati. Utile a mettere in un unico luogo tutte risorse dati.
public class VacationRepository {
    private IVacationDao vacationDao;
    private LiveData<List<VacationLite>> vacationList;

    private IParticipantDao participantDao;
    private LiveData<List<Participant>> participantList;

    private static IInsertListener listener;

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
        new InsertAsyncTask(vacationDao).execute(vacation);
    }

    public void addUpdateListener(IInsertListener listener) {
        this.listener = listener;
    }

    // classe interna per la gestione dei task, asincroni rispetto l'UI.
    private static class InsertAsyncTask extends AsyncTask<Vacation, Void, Void> {

        private IVacationDao asyncDao;

        InsertAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(final Vacation... vacations) {
            long rowId = asyncDao.insert(vacations[0]);
            listener.onInsertComplete(rowId);
            return null;
        }
    }

    public interface IInsertListener {
        void onInsertComplete(long rowId);
    }
}
