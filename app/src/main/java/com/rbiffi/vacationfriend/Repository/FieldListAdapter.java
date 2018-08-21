package com.rbiffi.vacationfriend.Repository;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.VacationList.IClickEvents;

import java.util.List;

/*  Classe per gestire le Recyclerview contenenti i campi degli oggetti da creare/modificare
    al'interno della app. Ogni volta che si crea o modifica un elemento, questo adapter specifica
    quale view usare e cosa succede al suo interno, per ogni campo dell'oggetto.
 */

public class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {

    //TODO lista di interi per tutti i possibili fieldList
    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_PERIOD = 1;
    private static final int VIEW_TYPE_PLACE = 2;
    private static final int VIEW_TYPE_PARTECIPANTS = 3;
    private static final int VIEW_TYPE_PHOTO = 4;
    private static final int VIEW_TYPE_NOTES = 5;
    //...

    private final LayoutInflater inflater;
    private IClickEvents listener;
    private List<String> fieldList;

    public FieldListAdapter(Context applicationContext, IUserEditableObject editableObj) {
        inflater = LayoutInflater.from(applicationContext);
        fieldList = editableObj.getEditableFields();
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO a seconda del viewtype posso creare view diverse per gli oggetti, tipo il footer
        View view;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                view = inflater.inflate(R.layout.field_title, parent, false);
                break;
            case VIEW_TYPE_PERIOD:
                view = inflater.inflate(R.layout.field_period, parent, false);
                break;
            case VIEW_TYPE_PLACE:
                view = inflater.inflate(R.layout.field_place, parent, false);
                break;
            case VIEW_TYPE_PARTECIPANTS:
                view = inflater.inflate(R.layout.field_partecipants, parent, false);
                break;
            case VIEW_TYPE_PHOTO:
                view = inflater.inflate(R.layout.field_photo, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.field_title, parent, false);
                break;
        }
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        //todo assegna una precompilazione dei campi (se modifica) + connetti eventi a pulsanti
        // tutto vuoto per la new. Dati presi dal caller per la modifica

        /*
        if (field != null) {
            //rimpiazza i dati ed assegna i click per la posizione corrente

            VacationLite current = field.get(position);
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
            // todo Dati non pronti
        }
        */
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
                return VIEW_TYPE_PARTECIPANTS;
            case Constants.F_PHOTO:
                return VIEW_TYPE_PHOTO;
            case Constants.F_NOTES:
                return VIEW_TYPE_NOTES;
            //todo e le altre
            default:
                return VIEW_TYPE_TITLE;
        }
    }

    //todo valuta di inserire un metodo che aggiorna la visualizzazione dei campi
    // con notifyDataSetChanged(). Non dovrebbe servire per i campi, solo le vacanze.

    class FieldViewHolder extends RecyclerView.ViewHolder {

        //TODO poi devo salvare anche le altre view che mi interessano
        private final EditText titleFieldView;
        private final EditText periodFromView;
        private final EditText periodToView;

        public FieldViewHolder(View itemView) {
            super(itemView);
            // mi interessa salvare solo alcuni campi
            // sicuro i partecipanti e le foto (per accedere ai bottoni)
            // forse ciò che l'utente scrive nei form. Non è chiara di chi è la responsabilità
            titleFieldView = itemView.findViewById(R.id.input_title);
            periodFromView = itemView.findViewById(R.id.input_period_from);
            periodToView = itemView.findViewById(R.id.input_period_to);
        }
    }
}
