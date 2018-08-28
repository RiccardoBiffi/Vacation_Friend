package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ParticipantsDialogViewModel;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.ArrayList;
import java.util.List;

class ParticipantDialogAdapter extends ArrayAdapter<Participant> {

    private Context context;
    private ParticipantsDialogViewModel viewModel;
    private List<Participant> allParticipants;

    public ParticipantDialogAdapter(@NonNull Context context, int resource, @NonNull List<Participant> objects) {
        super(context, resource, objects);

        this.context = context;
        this.allParticipants = objects;
    }

    public void removeSelectedParticipant(Participant current) {
        int position = isCurrentPartecipantSelected(current, selectedParticipants);
        if (position >= 0) {
            selectedParticipants.remove(position);
        }
    }

    public void updateParticipants() {
        this.participantList = viewModel.getAllParticipants();
        notifyDataSetChanged();
    }


    private int isCurrentPartecipantSelected(Participant selected, List<Participant> selectedParticipants) {
        String sEmail = selected.email;
        List<String> lEamils = new ArrayList<>();
        for (Participant p :
                selectedParticipants) {
            lEamils.add(p.email);
        }
        return lEamils.indexOf(sEmail);
    }

    public void setViewModel(ParticipantsDialogViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
