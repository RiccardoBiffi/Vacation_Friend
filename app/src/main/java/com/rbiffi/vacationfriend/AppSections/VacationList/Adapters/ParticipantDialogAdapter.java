package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantDialogAdapter extends ArrayAdapter<Participant> {

    private final Context context;
    private final int layoutResource;
    private List<Participant> participantList;

    private IParticipantDialogEvents listener;
    private List<Participant> selectedParticipants;

    public ParticipantDialogAdapter(@NonNull Context context, int resource, @NonNull List<Participant> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutResource = resource;
        participantList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater != null ? inflater.inflate(layoutResource, parent, false) : null;

        CircleImageView dialogPartecipantPictureView = convertView.findViewById(R.id.dialog_participant_picture);
        TextView dialogPartecipantNameView = convertView.findViewById(R.id.dialog_partecipant_name);
        final CheckBox dialogCheckboxParticipantView = convertView.findViewById(R.id.dialog_participant_checkbox);

        final Participant selected = getItem(position);
        dialogPartecipantPictureView.setImageURI(selected != null ? selected.picture : null);
        dialogPartecipantNameView.setText((selected != null ? selected.name : "") + " " + (selected != null ? selected.lastName : ""));

        // gli oggetti con cui confronto sono sempre diversi da quelli salvati poiché ottenuti dal DB
        // devo confrontarli con il loro campo univoco, ossia l'email
        assert selected != null;
        int elementPosition = indexOf(selected, selectedParticipants);
        boolean isSelected = elementPosition >= 0;
        dialogCheckboxParticipantView.setChecked(isSelected);
        dialogCheckboxParticipantView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCheckboxParticipantView.isChecked()) {
                    listener.onAddSelected(selected);
                } else {
                    listener.onRemoveSelected(selected);
                }
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return participantList.size();
    }

    @Nullable
    @Override
    public Participant getItem(int position) {
        return participantList.get(position);
    }

    public void updateParticipants(List<Participant> participants) {
        this.participantList = participants;
        notifyDataSetChanged();
    }

    public void setSelectedParticipants(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }

    public void setListener(IParticipantDialogEvents listener) {
        this.listener = listener;
    }

    private int indexOf(Participant selected, List<Participant> selectedParticipants) {
        String sEmail = selected.email;
        List<String> lEamils = new ArrayList<>();
        for (Participant p :
                selectedParticipants) {
            lEamils.add(p.email);
        }
        return lEamils.indexOf(sEmail);
    }

    public interface IParticipantDialogEvents {
        void onAddSelected(Participant selected);

        void onRemoveSelected(Participant selected);
    }
}
