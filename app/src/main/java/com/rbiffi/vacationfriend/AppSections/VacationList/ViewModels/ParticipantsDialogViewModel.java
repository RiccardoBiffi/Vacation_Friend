package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsDialogViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo
    //Salva la lista dei partecipanti presa dal DB e quali sono stati selezionati dall'utente

    private VacationRepository repository;

    private LiveData<List<Participant>> allPartecipants;
    private List<Participant> selectedParticipants;

    public ParticipantsDialogViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        // inizializzo campi
        allPartecipants = repository.getParticipantList();
        selectedParticipants = new ArrayList<>();
    }

    public LiveData<List<Participant>> getAllParticipants() {
        return allPartecipants;
    }

    public List<Participant> getSelectedParticipants() {
        return selectedParticipants;
    }

    public void setSelectedParticipants(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }
}
