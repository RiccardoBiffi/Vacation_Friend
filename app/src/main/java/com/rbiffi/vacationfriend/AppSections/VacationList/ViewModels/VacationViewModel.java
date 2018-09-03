package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    //todo dividi le richieste in base alla data ed in base se sono archiviate

    private VacationRepository repository;

    private LiveData<List<VacationLite>> allVacations;
    private Vacation selected;

    public VacationViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        //inizializzo i dati
        allVacations = repository.getActiveVacationList();
        selected = null;
    }

    public LiveData<List<VacationLite>> getAllVacations() {
        return allVacations;
    }

    public void delete(long v) {
        repository.delete(v);
    }

    public void store(long id) {
        repository.store(id);
    }

    public void updateSelectedVacation(long id, VacationRepository.IRepositoryListener listener) {
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
