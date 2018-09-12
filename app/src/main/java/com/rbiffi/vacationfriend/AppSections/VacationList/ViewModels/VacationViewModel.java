package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    //todo dividi le richieste in base alla data ed in base se sono archiviate

    private VacationFriendRepository repository;

    private LiveData<List<VacationLite>> vacationsNow;
    private LiveData<List<VacationLite>> vacationsNext;
    private LiveData<List<VacationLite>> vacationsPrevious;

    private LiveData<List<VacationLite>> storedVacation;
    private Vacation selected;

    public VacationViewModel(@NonNull Application app) {
        super(app);
        repository = VacationFriendRepository.getInstance(app);

        //inizializzo i dati
        vacationsNow = repository.getVacationListNow();
        vacationsNext = repository.getVacationListNext();
        vacationsPrevious = repository.getVacationListPrevious();

        storedVacation = repository.getStoredVacation();
        selected = null;
    }

    public LiveData<List<VacationLite>> getVacationsNow() {
        return vacationsNow;
    }

    public LiveData<List<VacationLite>> getVacationsNext() {
        return vacationsNext;
    }

    public LiveData<List<VacationLite>> getVacationsPrevious() {
        return vacationsPrevious;
    }

    public LiveData<List<VacationLite>> getStoredVacation() {
        return storedVacation;
    }

    public void delete(long v) {
        repository.delete(v);
    }

    public void store(long id) {
        repository.store(id);
    }

    public void unstore(long id) {
        repository.unstore(id);
    }

    public void updateSelectedVacation(long id, VacationFriendRepository.IRepositoryListener listener) {
        repository.addListener(listener);
        repository.getVacationDetails(id);
    }

    public Vacation getSelectedVacation() {
        return selected;
    }

    public void setSelectedVacation(Vacation selected) {
        this.selected = selected;
    }
}
