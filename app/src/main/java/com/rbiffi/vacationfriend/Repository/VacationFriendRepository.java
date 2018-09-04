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
public class VacationFriendRepository {

    public interface IRepositoryListener {
        void onInsertComplete(long rowId);

        void onGetComplete(Vacation v);
    }

    private IVacationDao vacationDao;
    private LiveData<List<VacationLite>> vacationList;

    private IParticipantDao participantDao;
    private LiveData<List<Participant>> participantList;

    private IJoinVacationParticipantDao jVacationParticipantDao;

    private static IRepositoryListener listener;

    public VacationFriendRepository(Application app) {
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

    public void delete(Long vacationId) {
        new DeleteAsyncTask(vacationDao).execute(vacationId);
    }

    public void store(Long vacationId) {
        new StoreAsyncTask(vacationDao).execute(vacationId);
    }

    public void getVacationDetails(long vId) {
        new GetDetailsAsyncTask(vacationDao).execute(vId);
    }

    public void update(Vacation builtVacation) {
        new UpdateAsyncTask(vacationDao).execute(builtVacation);
    }

    public void addListener(IRepositoryListener listener) {
        VacationFriendRepository.listener = listener;
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


    // classe interna per la gestione dei task, asincroni rispetto l'UI.
    private static class DeleteAsyncTask extends AsyncTask<Long, Void, Void> {

        private IVacationDao asyncDao;

        DeleteAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Long... vacationIds) {
            asyncDao.deleteFromID(vacationIds[0]);
            return null;
        }
    }


    private static class StoreAsyncTask extends AsyncTask<Long, Void, Void> {

        private IVacationDao asyncDao;

        StoreAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Long... vacationIds) {
            asyncDao.storeFromID(vacationIds[0]);
            return null;
        }
    }


    private static class InsertListAsyncTask extends AsyncTask<List<JoinVacationParticipant>, Void, Void> {

        private IJoinVacationParticipantDao asyncDao;

        InsertListAsyncTask(IJoinVacationParticipantDao vacationDao) {
            asyncDao = vacationDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<JoinVacationParticipant>... vacationIds) {
            asyncDao.insertList(vacationIds[0]);
            return null;
        }
    }

    private static class GetDetailsAsyncTask extends AsyncTask<Long, Void, Vacation> {

        private IVacationDao asyncDao;

        GetDetailsAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Vacation doInBackground(Long... vacationIds) {
            return asyncDao.getVacationDetails(vacationIds[0]);
        }

        @Override
        protected void onPostExecute(Vacation vacation) {
            super.onPostExecute(vacation);
            listener.onGetComplete(vacation);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Vacation, Void, Void> {

        private IVacationDao asyncDao;

        UpdateAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Vacation... vacationIds) {
            asyncDao.insert(vacationIds[0]);
            return null;
        }
    }
}
