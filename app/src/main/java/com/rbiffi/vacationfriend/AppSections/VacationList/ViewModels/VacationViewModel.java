package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    //todo valuta di spezzarlo in 2, l'ideale è che ogni activity abbia il suo
    // VacationListViewModel - per salvare la lista delle vacanze
    // NewVacationViewModel - per salvare la lista degli attributi

    private VacationRepository repository;

    private LiveData<List<VacationLite>> allVacations;

    private LiveData<List<Participant>> allPartecipants;

    public VacationViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        //inizializzo i dati
        allVacations = repository.getVacationList();
        allPartecipants = repository.getParticipantList();
    }

    public LiveData<List<VacationLite>> getAllVacations() {
        return allVacations;
    }

}
