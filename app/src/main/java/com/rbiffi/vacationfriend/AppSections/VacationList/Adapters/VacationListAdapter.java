package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;

import java.util.List;


public class VacationListAdapter extends RecyclerView.Adapter<VacationListAdapter.VacationViewHolder> {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0; // todo non serve
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private final LayoutInflater inflater;
    private IVacationListClickEvents listener;
    private List<VacationLite> vacationListCache;


    public VacationListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //TODO a seconda del viewtype posso creare view diverse per gli oggetti, tipo il footer
        View view;
        switch (viewType){
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.vacation_list_row, parent, false);
                break;
                /*
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                view = inflater.inflate(R.layout.empty_vacation_list, parent, false);
                break;
                */
            default:
                //TODO probabilmente finisco sempre qua
                view = inflater.inflate(R.layout.vacation_list_row, parent, false);
                break;
        }
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VacationViewHolder holder, int position) {
        if (vacationListCache != null) {
            //rimpiazza i dati ed assegna i click per la posizione corrente
            final VacationLite current = vacationListCache.get(position);
            holder.vacationTitleView.setText(current.title);
            holder.vacationTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(current);
                    }
                }
            });

            holder.vacationImageView.setImageURI(current.photo);
            holder.vacationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(current);
                    }
                }
            });

            holder.vacationOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOverflowClick(v, current);
                    }
                }
            });
        } else {
            //todo metti immagine grigia finchè non carica
            // Dati non pronti, placeholder
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

    public void setListener(IVacationListClickEvents listener) {
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

        VacationViewHolder(View itemView) {
            super(itemView);
            vacationTitleView = itemView.findViewById(R.id.vacation_title);
            vacationImageView = itemView.findViewById(R.id.vacationImage);
            vacationOverflow = itemView.findViewById(R.id.vacationMenu);
        }
    }

}