package com.rbiffi.vacationfriend.Repository;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.VacationLite;
import com.rbiffi.vacationfriend.VacationList.IClickEvents;

import java.util.List;


public class VacationListAdapter extends RecyclerView.Adapter<VacationListAdapter.VacationViewHolder> {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private final LayoutInflater inflater;
    private IClickEvents listener;
    private List<VacationLite> vacationListCache;


    public VacationListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VacationListAdapter.VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //TODO a seconda del viewtype posso creare view diverse per gli oggetti, tipo il footer
        View view;
        switch (viewType){
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.vacation_list_row, parent, false);
                break;
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                view = inflater.inflate(R.layout.empty_vacation_list, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.vacation_list_row, parent, false);
                break;
        }
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VacationListAdapter.VacationViewHolder holder, int position) {
        final VacationLite vacation = vacationListCache.get(position);
        if (vacationListCache != null) {
            //rimpiazza i dati ed assegna i click per la posizione corrente
            VacationLite current = vacationListCache.get(position);
            holder.vacationTitleView.setText(current.name);
            holder.vacationTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.vacationClick(vacation);
                    }
                }
            });

            holder.vacationImageView.setImageURI(current.photo);
            holder.vacationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.vacationClick(vacation);
                    }
                }
            });

            holder.vacationOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.overflowClick(v, vacation);
                    }
                }
            });
        } else {
            // Dati non pronti, placeholder
            holder.vacationTitleView.setText("Caricamento...");
            //todo metti immagine grigia finchè non carica
        }
    }

    @Override
    public int getItemCount() {
        if (vacationListCache == null) return 0;
        return vacationListCache.size();
    }

    @Override
    public int getItemViewType(int position) {
        return vacationListCache.isEmpty() ? VIEW_TYPE_EMPTY_LIST_PLACEHOLDER: VIEW_TYPE_OBJECT_VIEW;
    }

    public void setListener(IClickEvents listener) {
        this.listener = listener;
    }

    public void setVacations(List<VacationLite> vacations) {
        vacationListCache = vacations;
        notifyDataSetChanged();
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {

        //TODO poi devo salvare anche le altre view che mi interessano
        private final TextView vacationTitleView;
        private final ImageView vacationImageView;
        private final ImageButton vacationOverflow;

        private VacationViewHolder(View itemView) {
            super(itemView);
            vacationTitleView = itemView.findViewById(R.id.vacation_title);
            vacationImageView = itemView.findViewById(R.id.vacationImage);
            vacationOverflow = itemView.findViewById(R.id.vacationMenu);
        }
    }

}