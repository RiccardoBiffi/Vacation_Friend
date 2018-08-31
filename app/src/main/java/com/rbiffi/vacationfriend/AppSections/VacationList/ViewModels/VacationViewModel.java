package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    //todo dividi le richieste in base alla data ed n base se sono archiviate

    private VacationRepository repository;

    private LiveData<List<VacationLite>> allVacations;

    public VacationViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        //inizializzo i dati
        allVacations = repository.getVacationList();
    }

    public LiveData<List<VacationLite>> getAllVacations() {
        return allVacations;
    }

}
