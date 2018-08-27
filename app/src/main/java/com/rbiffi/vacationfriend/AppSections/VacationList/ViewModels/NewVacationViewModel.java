package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.List;

public class NewVacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    private VacationRepository repository;

    private String fieldTitle;
    private LiveData<List<Participant>> allPartecipants;
    //todo altri campi dell'activity

    public NewVacationViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        //inizializzo i dati
        fieldTitle = "";
        allPartecipants = repository.getParticipantList();
    }

    public LiveData<List<Participant>> getAllParticipants() {
        return allPartecipants;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public void insert(Vacation v) {
        //la logica dell'inserimento delle vacanze è completamente gestita dal repository
        repository.insert(v);
    }

}
