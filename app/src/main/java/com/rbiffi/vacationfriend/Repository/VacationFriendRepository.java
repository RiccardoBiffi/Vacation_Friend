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
import com.rbiffi.vacationfriend.Utils.JoinVacationParticipantListWrapper;

import java.util.List;

//classe per gestire le sorgenti di dati. Utile a mettere in un unico luogo tutte risorse dati.
public class VacationFriendRepository {

    public interface IRepositoryListener {

        void onVacationOperationComplete(long rowId);

        void onGetVacationDetailsComplete(Vacation v);

        void onGetVacationParticipants(List<Participant> participants);



    }
    private static volatile VacationFriendRepository REPOSITORY = null;
    private IVacationDao vacationDao;
    private LiveData<List<VacationLite>> vacationListNow;

    private LiveData<List<VacationLite>> vacationListNext;

    private LiveData<List<VacationLite>> vacationListPrevious;
    private LiveData<List<VacationLite>> storedVacation;
    private IParticipantDao participantDao;

    private LiveData<List<Participant>> participantList;
    private IJoinVacationParticipantDao jVacationParticipantDao;

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
        participantList = participantDao.getAllParticipants();
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

    public LiveData<List<VacationLite>> getVacationListNow() {
        return vacationListNow;
    }

    public LiveData<List<VacationLite>> getVacationListNext() {
        return vacationListNext;
    }

    public LiveData<List<VacationLite>> getVacationListPrevious() {
        return vacationListPrevious;
    }

    public LiveData<List<VacationLite>> getStoredVacation() {
        return storedVacation;
    }

    public LiveData<List<Participant>> getParticipantList() {
        return participantList;
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

    public void getVacationDetails(long vId) {
        new GetDetailsAsyncTask(vacationDao).execute(vId);
    }

    public void getVacationParticipants(long vId) {
        new GetParticipantsAsyncTask(jVacationParticipantDao).execute(vId);
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
            listener.onGetVacationDetailsComplete(vacation);
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

    private static class GetParticipantsAsyncTask extends AsyncTask<Long, Void, List<Participant>> {

        private IJoinVacationParticipantDao asyncDao;

        GetParticipantsAsyncTask(IJoinVacationParticipantDao vacationDao) {
            asyncDao = vacationDao;
        }

        @Override
        protected List<Participant> doInBackground(Long... vacationIds) {
            return asyncDao.getParticipantsForVacation(vacationIds[0]);
        }

        @Override
        protected void onPostExecute(List<Participant> participants) {
            super.onPostExecute(participants);
            listener.onGetVacationParticipants(participants);
        }
    }
}
