package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.NewVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

    private Context context;
    private NewVacationViewModel viewModel;
    private int layoutResource;
    private List<Participant> participantList;
    private List<Participant> selectedParticipants;

    public ParticipantAdapter(@NonNull Context context, NewVacationViewModel viewModel, int resource) {
        super(context, resource, viewModel.getFieldParticipants());
        this.context = context;
        this.viewModel = viewModel;
        layoutResource = resource;
        participantList = viewModel.getFieldParticipants();
        selectedParticipants = new ArrayList<>();
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public List<Participant> getSelectedParticipants() {
        return selectedParticipants;
    }

    public void setSelectedParticipants(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layoutResource, null);
        switch (layoutResource) {
            case R.layout.field_partecipants_row:
                CircleImageView partecipantPictureView = convertView.findViewById(R.id.partecipant_picture);
                TextView partecipantNameView = convertView.findViewById(R.id.partecipant_name);
                ImageButton removeParticipantView = convertView.findViewById(R.id.remove_partic_button);

                Participant current = getItem(position);
                partecipantPictureView.setImageURI(current != null ? current.picture : null);
                partecipantNameView.setText((current != null ? current.name : "") + " " + (current != null ? current.lastName : ""));
                removeParticipantView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        participantList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                break;

            case R.layout.dialog_participants_row:
                CircleImageView dialogPartecipantPictureView = convertView.findViewById(R.id.dialog_participant_picture);
                TextView dialogPartecipantNameView = convertView.findViewById(R.id.dialog_partecipant_name);
                final CheckBox dialogCheckboxParticipantView = convertView.findViewById(R.id.dialog_participant_checkbox);

                final Participant selected = getItem(position);
                dialogPartecipantPictureView.setImageURI(selected != null ? selected.picture : null);
                dialogPartecipantNameView.setText((selected != null ? selected.name : "") + " " + (selected != null ? selected.lastName : ""));

                // gli oggetti con cui confronto sono sempre diversi da quelli salvati poiché ottenuti dal DB
                // devo confrontarli con il loro campo univoco, ossia l'email
                int elementPosition = isCurrentPartecipantSelected(selected, selectedParticipants);
                dialogCheckboxParticipantView.setChecked(elementPosition >= 0);
                dialogCheckboxParticipantView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogCheckboxParticipantView.isChecked()) {
                            addSelectedParticipant(selected);
                        } else {
                            removeSelectedParticipant(selected);
                        }
                    }
                });

                break;

            default:
                Toast.makeText(getContext(), "View non riconosciuta", Toast.LENGTH_SHORT).show();
                break;
        }

        return convertView;
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

    @Nullable
    @Override
    public Participant getItem(int position) {
        return participantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return participantList.size();
    }

    public void addSelectedParticipant(Participant current) {
        if (isCurrentPartecipantSelected(current, selectedParticipants) < 0) {
            selectedParticipants.add(current);
        }
    }

    public void removeSelectedParticipant(Participant current) {
        int position = isCurrentPartecipantSelected(current, selectedParticipants);
        if (position >= 0) {
            selectedParticipants.remove(position);
        }
    }

    public void updateParticipants() {
        this.participantList = viewModel.getFieldParticipants();
        notifyDataSetChanged();
    }
}
