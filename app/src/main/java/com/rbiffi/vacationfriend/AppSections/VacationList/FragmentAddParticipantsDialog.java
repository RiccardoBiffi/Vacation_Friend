package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.ParticipantAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.NewVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.ArrayList;
import java.util.List;

// Frammento che permette di selezionare partecipanti tramite una dialog con lista e checkbox
public class FragmentAddParticipantsDialog extends DialogFragment {

    private NewVacationViewModel viewModel; //todo fanne un'altro e popolalo con le scelte fatte

    private ParticipantAdapter participantAdapter;
    private IAddParticipantsListener listener;
    private List<Participant> selectedParticipants;

    public FragmentAddParticipantsDialog() {
        selectedParticipants = new ArrayList<>();
    }

    public void setSelectedParticipants(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //todo metti la lista utenti selezionabili (da DB)
        participantAdapter = new ParticipantAdapter(getContext(), R.layout.dialog_participants_row, selectedParticipants);
        participantAdapter.setSelectedParticipants(selectedParticipants);
        viewModel = ViewModelProviders.of(this).get(NewVacationViewModel.class);
        viewModel.getAllParticipants().observe(this, new Observer<List<Participant>>() {
            @Override
            public void onChanged(@Nullable List<Participant> participants) {
                participantAdapter.setParticipantList(participants);
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_participants, null);
        ListView lv = v.findViewById(R.id.dialog_allparticipant_list);
        Button undoButton = v.findViewById(R.id.undoBottonAction);
        Button confirmButton = v.findViewById(R.id.saveBottonAction);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogNegativeClick(FragmentAddParticipantsDialog.this);
            }
        });

        confirmButton.setText(R.string.button_done);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogPositiveClick(FragmentAddParticipantsDialog.this, participantAdapter.getSelectedParticipants());
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = view.findViewById(R.id.dialog_participant_checkbox);
                cb.toggle();
                Participant current = (Participant) parent.getItemAtPosition(position);
                if (cb.isChecked()) {
                    participantAdapter.addSelectedParticipant(current);
                } else {
                    participantAdapter.removeSelectedParticipant(current);
                }
            }
        });

        View footer = inflater.inflate(R.layout.dialog_participants_footer, null);
        Button inviteButton = footer.findViewById(R.id.dialog_invite_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Start InviteContactsActivity", Toast.LENGTH_SHORT).show();
            }
        });
        lv.addFooterView(footer);

        lv.setAdapter(participantAdapter);
        builder.setView(v);

        builder.setTitle(R.string.dialog_participant_message);
        return builder.create();
    }

    @Override
    public void onAttach(Context callerContext) {
        // metodo chiamato subito prima di OnCreate()
        super.onAttach(callerContext);
        try {
            listener = (IAddParticipantsListener) callerContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(callerContext.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface IAddParticipantsListener {
        void onDialogPositiveClick(DialogFragment dialog, List<Participant> selectedParticipants);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
