package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsDialogViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo
    //Salva la lista dei partecipanti presa dal DB e quali sono stati selezionati dall'utente

    private VacationFriendRepository repository;

    private LiveData<List<Participant>> allPartecipants;
    private List<Participant> selectedParticipants;

    public ParticipantsDialogViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationFriendRepository(app);

        // inizializzo campi
        allPartecipants = repository.getParticipantList();
        selectedParticipants = null;
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

    public void addSelectedParticipant(Participant current) {
        if (indexOf(current, selectedParticipants) < 0) {
            selectedParticipants.add(current);
        }
    }

    public void removeSelectedParticipant(Participant current) {
        int position = indexOf(current, selectedParticipants);
        if (position >= 0) {
            selectedParticipants.remove(position);
        }
    }

    private int indexOf(Participant selected, List<Participant> selectedParticipants) {
        String sEmail = selected.email;
        List<String> emailList = new ArrayList<>();
        for (Participant p :
                selectedParticipants) {
            emailList.add(p.email);
        }
        return emailList.indexOf(sEmail);
    }
}
