package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

    private Context context;
    private int layoutResource;
    private List<Participant> participantList;


    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<Participant> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutResource = resource;
        participantList = objects;
    }

    public List<Participant> getParticipantList() {
        //TODO non dovrebbe esere lui a restituire il dato
        // l'activity chiamante dovrebbe avere giò il dato tramite avento
        return participantList;
    }

    //public List<Participant> getSelectedParticipants() {
    //    return selectedParticipants;
    //}

    //public void setSelectedParticipants(List<Participant> selectedParticipants) {
    //    this.selectedParticipants = selectedParticipants;
    //}

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater != null ? inflater.inflate(layoutResource, parent, false) : null;
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

    public void updateParticipants(List<Participant> participantList) {
        this.participantList = participantList;
        notifyDataSetChanged();
    }

}
