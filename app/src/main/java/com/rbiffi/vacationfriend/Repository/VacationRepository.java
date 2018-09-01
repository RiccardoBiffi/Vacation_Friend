package com.rbiffi.vacationfriend.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rbiffi.vacationfriend.Repository.DAOs.IJoinVacationParticipantDao;
import com.rbiffi.vacationfriend.Repository.DAOs.IParticipantDao;
import com.rbiffi.vacationfriend.Repository.DAOs.IVacationDao;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
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

    private IJoinVacationParticipantDao jVacationParticipantDao;

    private static IInsertListener listener;

    public VacationRepository(Application app) {
        VacationFriendDatabase db = VacationFriendDatabase.getDatabase(app); // prendo l'istanza del db
        vacationDao = db.getVacationDao(); // prendo dal db il DAO
        participantDao = db.getParticipantDao();
        jVacationParticipantDao = db.getJoinVacationParticipantDao();

        // acquisisco quel che mi interessa dal db
        vacationList = vacationDao.getActiveVacations();
        participantList = participantDao.getAllPartecipants();
    }

    public LiveData<List<VacationLite>> getActiveVacationList() {
        return vacationList;
    }

    public LiveData<List<Participant>> getParticipantList() {
        return participantList;
    }

    public void insert(Vacation vacation) {
        // bisogna chiamarlo in un tread diverso da quello principale.
        new InsertAsyncTask(vacationDao).execute(vacation);
    }

    public void insertList(List<JoinVacationParticipant> jvps) {
        new InsertListAsyncTask(jVacationParticipantDao).execute(jvps);
    }

    public void delete(int vacationId) {
        new DeleteAsyncTask(vacationDao).execute(vacationId);
    }

    public void store(int vacationId) {
        new StoreAsyncTask(vacationDao).execute(vacationId);
    }

    public void addUpdateListener(IInsertListener listener) {
        this.listener = listener;
    }


    // classe interna per la gestione dei task, asincroni rispetto l'UI.
    private static class InsertAsyncTask extends AsyncTask<Vacation, Void, Long> {

        private IVacationDao asyncDao;

        InsertAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Long doInBackground(final Vacation... vacations) {
            return asyncDao.insert(vacations[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            listener.onInsertComplete(aLong);
        }
    }

    public interface IInsertListener {
        void onInsertComplete(long rowId);
    }


    // classe interna per la gestione dei task, asincroni rispetto l'UI.
    private static class DeleteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private IVacationDao asyncDao;

        DeleteAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Integer... vacationIds) {
            asyncDao.deleteFromID(vacationIds[0]);
            return null;
        }
    }


    private static class StoreAsyncTask extends AsyncTask<Integer, Void, Void> {

        private IVacationDao asyncDao;

        StoreAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Integer... vacationIds) {
            asyncDao.storeFromID(vacationIds[0]);
            return null;
        }
    }


    private static class InsertListAsyncTask extends AsyncTask<List<JoinVacationParticipant>, Void, Void> {

        private IJoinVacationParticipantDao asyncDao;

        InsertListAsyncTask(IJoinVacationParticipantDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(List<JoinVacationParticipant>... vacationIds) {
            asyncDao.insertList(vacationIds[0]);
            return null;
        }
    }
}
