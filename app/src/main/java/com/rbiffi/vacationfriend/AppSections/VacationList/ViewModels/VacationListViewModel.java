package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.List;

public class VacationListViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    private final VacationFriendRepository repository;

    private final LiveData<List<Vacation>> vacationsNow;
    private final LiveData<List<Vacation>> vacationsNext;
    private final LiveData<List<Vacation>> vacationsPrevious;

    private final LiveData<List<Vacation>> storedVacation;
    private Vacation selected;

    public VacationListViewModel(@NonNull Application app) {
        super(app);
        repository = VacationFriendRepository.getInstance(app);

        //inizializzo i dati
        vacationsNow = repository.getVacationListNow();
        vacationsNext = repository.getVacationListNext();
        vacationsPrevious = repository.getVacationListPrevious();

        storedVacation = repository.getStoredVacation();
        selected = null;
    }

    public LiveData<List<Vacation>> getVacationsNow() {
        return vacationsNow;
    }

    public LiveData<List<Vacation>> getVacationsNext() {
        return vacationsNext;
    }

    public LiveData<List<Vacation>> getVacationsPrevious() {
        return vacationsPrevious;
    }

    public LiveData<List<Vacation>> getStoredVacation() {
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

    public LiveData<Vacation> loadClickedVacation(long id) {
        return repository.getVacationDetails(id);
    }

    public Vacation getSelectedVacation() {
        return selected;
    }

    public void setSelectedVacation(Vacation selected) {
        this.selected = selected;
    }
}
