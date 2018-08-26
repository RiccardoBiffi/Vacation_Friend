package com.rbiffi.vacationfriend.Repository;

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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

    private Context context;
    private List<Participant> listData;

    public List<Participant> getListData() {
        return listData;
    }

    public void setListData(List<Participant> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<Participant> objects) {
        super(context, resource, objects);
        this.context = context;
        listData = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.field_partecipants_row, null);
        CircleImageView partecipantPictureView = convertView.findViewById(R.id.partecipant_picture);
        TextView partecipantNameView = convertView.findViewById(R.id.partecipant_name);
        ImageButton removeParticipantView = convertView.findViewById(R.id.remove_partic_button);

        Participant current = getItem(position);
        partecipantPictureView.setImageURI(current != null ? current.picture : null);
        partecipantNameView.setText((current != null ? current.name : "") + " " + (current != null ? current.lastName : ""));
        removeParticipantView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
