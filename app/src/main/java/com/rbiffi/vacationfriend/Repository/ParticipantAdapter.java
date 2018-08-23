package com.rbiffi.vacationfriend.Repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

    private Context context; // todo serve?

    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<Participant> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.field_partecipants_row, null);
        CircleImageView partecipantPictureView = convertView.findViewById(R.id.partecipant_picture);
        TextView partecipantNameView = convertView.findViewById(R.id.partecipant_name);

        Participant current = getItem(position);
        partecipantPictureView.setImageURI(current.picture);
        partecipantNameView.setText(current.name + " " + current.lastName);
        return convertView;
    }
}
