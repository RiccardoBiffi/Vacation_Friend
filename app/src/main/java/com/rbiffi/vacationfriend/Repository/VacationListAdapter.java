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

    private final LayoutInflater inflater;
    private IClickEvents listener;
    private List<VacationLite> vacationListCache;

    public VacationListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VacationListAdapter.VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea nuove view
        View view = inflater.inflate(R.layout.vacation_list_row, parent, false);
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
            //metti immagine grigia finchè non carica
        }
    }

    @Override
    public int getItemCount() {
        if (vacationListCache == null) return 0;
        return vacationListCache.size();
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

    /*
    public VacationListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    VacationListAdapter(Context context, int vacation_list_row, int rowText, ArrayList dataSource) {
        super(context, vacation_list_row, rowText, dataSource);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        convertView = maybeRecycleView(convertView, parent);

        //Operazioni agguntive da fare alla view
        setOverflowClickListener(position, convertView, (ListView) parent, R.id.vacationMenu);
        setImageClickListener(position, convertView, (ListView) parent);

        return convertView;
    }

    private void setImageClickListener(final int position, @NonNull View convertView, @NonNull final ListView parent) {
        ImageView iv = convertView.findViewById(R.id.vacationImage);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // rimando l'evento del click al frammento.onItemCLick(). Lo gestirà lui a seconda dell'elemento cliccato
                parent.performItemClick(view, position, 0);
            }
        });
    }

    private void setOverflowClickListener(final int position, @NonNull View convertView, @NonNull final ListView parent, int vacationMenu) {
        ImageButton ib = convertView.findViewById(vacationMenu);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // rimando l'evento del click al frammento.onItemCLick(). Lo gestirà lui a seconda dell'elemento cliccato
                parent.performItemClick(view, position, 0);
            }
        });
    }

    private View maybeRecycleView(@Nullable View convertView, @NonNull ViewGroup parent) {
        if( convertView == null ){
            //We must create a View:
            LayoutInflater inflater = getActivity().getLayoutInflater();
            convertView = inflater.inflate(R.layout.vacation_list_row, parent, false);
        }
        return convertView;
    }
    */
}