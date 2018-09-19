package com.rbiffi.vacationfriend.AppSections.Home.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.Constants;

import java.util.List;

/*
    Classe per gestire le Recyclerview contenenti i campi degli oggetti da visualizzare
    al'interno della app. Ogni volta che si richiede il dettaglio di un elemento, questo adapter
    specifica quale view usare e cosa succede al suo interno, per ogni campo dell'oggetto.
 */

public class ReadFieldListAdapter extends RecyclerView.Adapter<ReadFieldListAdapter.SummaryViewHolder> {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_PERIOD = 1;
    private static final int VIEW_TYPE_PLACE = 2;
    private static final int VIEW_TYPE_PARTICIPANTS = 3;
    private static final int VIEW_TYPE_PHOTO = 4;
    private static final int VIEW_TYPE_NOTES = 5;

    private Context context;
    private VacationViewModel viewModel;
    private List<String> fieldList;

    private final LayoutInflater inflater;
    private IVacationListClickEvents listener;

    public ReadFieldListAdapter(Context context, List<String> fieldList, VacationViewModel viewModel) {
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.fieldList = fieldList;
        this.viewModel = viewModel;
    }

    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_PERIOD:
                view = inflater.inflate(R.layout.field_period, parent, false);
                break;
            case VIEW_TYPE_PLACE:
                view = inflater.inflate(R.layout.field_place, parent, false);
                break;
            case VIEW_TYPE_PARTICIPANTS:
                view = inflater.inflate(R.layout.field_partecipants, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.field_title, parent, false);
                break;
        }
        return new ReadFieldListAdapter.SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SummaryViewHolder holder, final int position) {
        final String field = fieldList.get(position);
        switch (field) {
            case Constants.F_PERIOD:
                break;

            case Constants.F_PLACE:
                break;

            case Constants.F_PARTECIP:
                //todo rispetto al edit, il read dei partecipanti è in lista orizzontale
                /*
                fieldParticipantsAdapter = new ParticipantAdapter(appContext, R.layout.input_partecipants_row, viewModel.getFieldParticipants());

                setParticipantsListHeader(holder);
                setParticipantsListFooter(holder);
                holder.partecipantListView.setAdapter(fieldParticipantsAdapter);
                */
                break;

            default:
                // dati non pronti, placeholder
        }
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String field = fieldList.get(position);
        switch (field) {
            case Constants.F_TITLE:
                return VIEW_TYPE_TITLE;
            case Constants.F_PERIOD:
                return VIEW_TYPE_PERIOD;
            case Constants.F_PLACE:
                return VIEW_TYPE_PLACE;
            case Constants.F_PARTECIP:
                return VIEW_TYPE_PARTICIPANTS;
            case Constants.F_PHOTO:
                return VIEW_TYPE_PHOTO;
            case Constants.F_NOTES:
                return VIEW_TYPE_NOTES;
            //todo e le altre
            default:
                return VIEW_TYPE_TITLE;
        }
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {

        // todo elementi di ogni view del sommario
        private final TextView periodFromView;
        private final TextView periodToView;

        private final TextView placeView;

        private final ListView partecipantListView;


        SummaryViewHolder(View itemView) {
            super(itemView);

            periodFromView = itemView.findViewById(R.id.field_period_from);
            periodToView = itemView.findViewById(R.id.field_period_to);

            placeView = itemView.findViewById(R.id.field_place);

            partecipantListView = itemView.findViewById(R.id.field_partes_list);
        }
    }
}
