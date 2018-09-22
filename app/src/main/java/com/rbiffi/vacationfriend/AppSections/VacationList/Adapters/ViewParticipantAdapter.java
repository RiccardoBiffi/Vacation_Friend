package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewParticipantAdapter extends RecyclerView.Adapter<ViewParticipantAdapter.ParticipantViewHolder> {

    private static final int VIEW_TYPE_OBJECT = 0;

    private final Context context;
    private final List<Participant> participants;

    private final LayoutInflater inflater;
    private final VacationViewModel viewModel;

    public ViewParticipantAdapter(Context context, VacationViewModel viewModel) {
        this.inflater = LayoutInflater.from(context);

        this.context = context;
        this.participants = viewModel.getParticipants();
        this.viewModel = viewModel;
    }

    @Override
    public ParticipantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_OBJECT:
                view = inflater.inflate(R.layout.field_partecipants_column, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.field_partecipants_column, parent, false);
                break;
        }
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParticipantViewHolder holder, int position) {
        if (position > 0) {
            Participant current = participants.get(position - 1);

            holder.profileView.setImageURI(current.picture);
            holder.nameView.setText(current.name);
        } else {
            holder.profileView.setImageURI(viewModel.getMyself().picture);
            holder.nameView.setText(context.getString(R.string.my_self));
        }
    }

    @Override
    public int getItemCount() {
        return participants.size() + 1; // lista con header
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_OBJECT;
    }

    public class ParticipantViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView profileView;
        private final TextView nameView;

        public ParticipantViewHolder(View itemView) {
            super(itemView);

            profileView = itemView.findViewById(R.id.partecipant_field_picture);
            nameView = itemView.findViewById(R.id.partecipant_short_name);
        }
    }
}
