package com.rbiffi.vacationfriend.AppSections.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.ViewParticipantAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.Constants;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

    private final Context context;
    private final VacationViewModel viewModel;
    private final List<String> fieldList;

    private final LayoutInflater inflater;
    private IVacationListClickEvents listener;
    private ViewParticipantAdapter fieldParticipantsAdapter;

    public ReadFieldListAdapter(Context context, List<String> fieldList, VacationViewModel viewModel) {
        this.inflater = LayoutInflater.from(context);

        this.context = context;
        this.viewModel = viewModel;
        this.fieldList = removeEmptyFields(fieldList);
    }

    private List<String> removeEmptyFields(List<String> fieldList) {
        List<String> valuedFields = new LinkedList<>(fieldList);

        if (viewModel.getFieldPlace().isEmpty())
            valuedFields.remove(Constants.F_PLACE);

        return valuedFields;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, final int position) {
        final String field = fieldList.get(position);
        switch (field) {
            case Constants.F_PERIOD:
                holder.periodDaysView.setText(String.format(Locale.getDefault(),
                        "%d %s",
                        countPeriodDays(viewModel.getFieldPeriodFrom(), viewModel.getFieldPeriodTo()),
                        context.getString(R.string.field_period_days)));
                holder.periodFromView.setText(viewModel.getFieldPeriodFrom());
                holder.periodToView.setText(viewModel.getFieldPeriodTo());
                break;

            case Constants.F_PLACE:
                holder.placeView.setText(viewModel.getFieldPlace());
                // todo al click sull'elemento si potrebbe aprire maps
                break;

            case Constants.F_PARTECIP:
                if (viewModel.getParticipants().size() == 0) {
                    holder.partecipantNumber.setText(String.format(Locale.getDefault(),
                            "%d %s",
                            viewModel.getParticipants().size() + 1,
                            context.getString(R.string.field_partic_singol)));
                } else {
                    holder.partecipantNumber.setText(String.format(Locale.getDefault(),
                            "%d %s",
                            viewModel.getParticipants().size() + 1,
                            context.getString(R.string.field_partic_number)));
                }

                fieldParticipantsAdapter = new ViewParticipantAdapter(context, viewModel);
                holder.partecipantListView.setAdapter(fieldParticipantsAdapter);

                RecyclerView.LayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                holder.partecipantListView.setLayoutManager(llm);

                break;

            default:
                // dati non pronti, placeholder
        }
    }

    private int countPeriodDays(String fieldPeriodFrom, String fieldPeriodTo) {
        Date fromDate = formattedStringToDate(fieldPeriodFrom);
        Date toDate = formattedStringToDate(fieldPeriodTo);
        LocalDate from = new LocalDate(fromDate);
        LocalDate to = new LocalDate(toDate);
        return Days.daysBetween(from, to).getDays() + 1; // +1 per renderlo inclusivo
    }

    private Date formattedStringToDate(String value) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = value == null ? null : format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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

        private final TextView periodDaysView;
        private final TextView periodFromView;
        private final TextView periodToView;

        private final TextView placeView;

        private final TextView partecipantNumber;
        private final RecyclerView partecipantListView;

        // todo view degli altri elementi del sommario


        SummaryViewHolder(View itemView) {
            super(itemView);

            periodDaysView = itemView.findViewById(R.id.label_total_period_days);
            periodFromView = itemView.findViewById(R.id.field_period_from);
            periodToView = itemView.findViewById(R.id.field_period_to);

            placeView = itemView.findViewById(R.id.field_place);

            partecipantNumber = itemView.findViewById(R.id.label_number_partes);
            partecipantListView = itemView.findViewById(R.id.field_partes_list);
        }
    }
}
