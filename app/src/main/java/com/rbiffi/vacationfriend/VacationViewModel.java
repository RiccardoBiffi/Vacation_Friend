package com.rbiffi.vacationfriend;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationLite;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce il l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    private VacationRepository repository;

    // per cashare dati
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

    public void insert(Vacation v){
        //la logica dell'inserimento delle vacanze è completamente gestita dal repository
        repository.insert(v);
    }
}
