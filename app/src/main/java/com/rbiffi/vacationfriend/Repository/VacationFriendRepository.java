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
import com.rbiffi.vacationfriend.Utils.JoinVacationParticipantListWrapper;

import java.util.List;

//classe per gestire le sorgenti di dati. Utile a mettere in un unico luogo tutte risorse dati.
public class VacationFriendRepository {

    public interface IRepositoryListener {

        void onVacationOperationComplete(long rowId);

    }


    private static volatile VacationFriendRepository REPOSITORY = null;
    private IVacationDao vacationDao;

    private LiveData<List<Vacation>> vacationListNow;
    private LiveData<List<Vacation>> vacationListNext;
    private LiveData<List<Vacation>> vacationListPrevious;
    private LiveData<List<Vacation>> storedVacation;
    private IParticipantDao participantDao;
    private LiveData<List<Participant>> allParticipantList;
    private IJoinVacationParticipantDao jVacationParticipantDao;

    private LiveData<Vacation> currentVacation;
    private LiveData<List<Participant>> currentParticipants;

    private static IRepositoryListener listener;
    private VacationFriendRepository(Application app) {

        VacationFriendDatabase db = VacationFriendDatabase.getDatabase(app); // prendo l'istanza del db
        vacationDao = db.getVacationDao(); // prendo dal db il DAO
        participantDao = db.getParticipantDao();
        jVacationParticipantDao = db.getJoinVacationParticipantDao();

        // acquisisco quel che mi interessa dal db
        vacationListNow = vacationDao.getCurrentVacations();
        vacationListNext = vacationDao.getNextVacations();
        vacationListPrevious = vacationDao.getEndedVacations();
        storedVacation = vacationDao.getAchievedVacations();
        allParticipantList = participantDao.getAllParticipants();
    }

    public static VacationFriendRepository getInstance(Application app) {
        if (REPOSITORY == null) {
            synchronized (VacationFriendRepository.class) {
                if (REPOSITORY == null) {
                    REPOSITORY = new VacationFriendRepository(app);
                }
            }
        }
        return REPOSITORY;
    }

    public LiveData<Vacation> getVacationDetails(long vId) {
        currentVacation = vacationDao.getVacationDetails(vId);
        return currentVacation;
    }

    public LiveData<List<Participant>> getVacationParticipants(long vId) {
        currentParticipants = jVacationParticipantDao.getParticipantsForVacation(vId);
        return currentParticipants;
    }

    public LiveData<List<Vacation>> getVacationListNow() {
        return vacationListNow;
    }

    public LiveData<List<Vacation>> getVacationListNext() {
        return vacationListNext;
    }

    public LiveData<List<Vacation>> getVacationListPrevious() {
        return vacationListPrevious;
    }

    public LiveData<List<Vacation>> getStoredVacation() {
        return storedVacation;
    }

    public LiveData<List<Participant>> getAllParticipantList() {
        return allParticipantList;
    }

    public void insert(Vacation vacation) {
        new InsertAsyncTask(vacationDao).execute(vacation);
    }

    public void insertParticipants(List<JoinVacationParticipant> jvps) {
        JoinVacationParticipantListWrapper jvplw = new JoinVacationParticipantListWrapper(jvps);
        new InsertListParticipantsAsyncTask(jVacationParticipantDao).execute(jvplw);
    }

    public void delete(Long vacationId) {
        new DeleteAsyncTask(vacationDao).execute(vacationId);
    }

    public void store(Long vacationId) {
        new StoreAsyncTask(vacationDao).execute(vacationId);
    }

    public void unstore(long vacationId) {
        new UnstoreAsyncTask(vacationDao).execute(vacationId);
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
            listener.onVacationOperationComplete(aLong);
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

    private static class UnstoreAsyncTask extends AsyncTask<Long, Void, Void> {

        private IVacationDao asyncDao;

        UnstoreAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Void doInBackground(Long... vacationIds) {
            asyncDao.unstoreFromID(vacationIds[0]);
            return null;
        }
    }


    private static class InsertListParticipantsAsyncTask extends AsyncTask<JoinVacationParticipantListWrapper, Void, Void> {

        private IJoinVacationParticipantDao asyncDao;

        InsertListParticipantsAsyncTask(IJoinVacationParticipantDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected final Void doInBackground(JoinVacationParticipantListWrapper... vacationIds) {
            JoinVacationParticipantListWrapper jvplw = vacationIds[0];
            asyncDao.insertList(jvplw.unwrap());
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Vacation, Void, Long> {

        private IVacationDao asyncDao;

        UpdateAsyncTask(IVacationDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected Long doInBackground(Vacation... vacationIds) {
            return asyncDao.insert(vacationIds[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            listener.onVacationOperationComplete(aLong);
        }
    }

}
