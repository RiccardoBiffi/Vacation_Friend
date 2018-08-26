package com.rbiffi.vacationfriend.VacationList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Participant;
import com.rbiffi.vacationfriend.Repository.ParticipantAdapter;
import com.rbiffi.vacationfriend.Utils.VacationViewModel;

import java.util.List;

// Frammento che permette di selezionare partecipanti tramite una dialog con lista e checkbox
public class AddParticipantsDialogFragment extends DialogFragment {

    private VacationViewModel viewModel;

    private ParticipantAdapter participantAdapter;
    private IAddParticipantsListener listener;
    private List<Participant> allParticipants;
    private List<Integer> selectedPartic;

    public AddParticipantsDialogFragment() {
        //todo capisci se passare la lista attuale (ed i selezionati) qui
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //todo metti la lista utenti selezionabili (da DB)
        participantAdapter = new ParticipantAdapter(getContext(), R.layout.field_partecipants_row, allParticipants);
        viewModel = ViewModelProviders.of(this).get(VacationViewModel.class);
        viewModel.getAllPartecipants().observe(this, new Observer<List<Participant>>() {
            @Override
            public void onChanged(@Nullable List<Participant> participants) {
                participantAdapter.setListData(participants);
            }
        });

        builder.setTitle(R.string.dialog_participant_message);

        builder.setMultiChoiceItems(R.array.toppings, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedPartic.add(which);
                        } else if (selectedPartic.contains(which)) {
                            selectedPartic.remove(Integer.valueOf(which));
                        }
                    }
                });

        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //todo aggiungi altri argomenti, tipo i selezionati
                listener.onDialogPositiveClick(AddParticipantsDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.button_ignore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(AddParticipantsDialogFragment.this);
            }
        });
        /*
        View v = getActivity().getLayoutInflater().inflate(R.layout.field_partecipants, null);
        ListView lv = v.findViewById(R.id.input_partes_list);
        lv.setAdapter(participantAdapter);
        builder.setView(v);
        */
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
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
