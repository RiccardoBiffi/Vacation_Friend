package com.rbiffi.vacationfriend.Repository;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.VacationList.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.VacationList.NewVacationActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private IVacationFieldsEvents listener;
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
    public void onBindViewHolder(final FieldViewHolder holder, int position) {
        //todo assegna una precompilazione dei campi (se modifica) + connetti eventi a pulsanti
        // tutto vuoto per la new. Dati presi dal caller per la modifica

        final String field = fieldList.get(position);
        switch (field) {
            case Constants.F_PHOTO:
                holder.photoButtonAddView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onAddPhotoClick(v, holder.photoImageButtonView);
                        }
                    }
                });
                holder.photoImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onAddPhotoClick(holder.photoButtonAddView, v);
                        }
                    }
                });
                break;
            case Constants.F_PERIOD:
                final Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(holder.periodFromView, calendar);
                    }
                };
                final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(holder.periodToView, calendar);
                    }
                };

                holder.periodFromView.setInputType(InputType.TYPE_NULL);
                holder.periodFromView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (listener != null) {
                            listener.onDateFocus(v, hasFocus, calendar, fromDateListener);
                        }
                    }
                });

                holder.periodToView.setInputType(InputType.TYPE_NULL);
                holder.periodToView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (listener != null) {
                            listener.onDateFocus(v, hasFocus, calendar, toDateListener);
                        }
                    }
                });

                break;
            default:
                //todo valuta cosa inserire qua
                // dati non pronti, placeholder
        }

        /*
        if (field != null) {
            //rimpiazza i dati ed assegna i click per la posizione corrente

            VacationLite current = field.get(position);
            holder.vacationTitleView.setText(current.name);
            holder.vacationTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(vacation);
                    }
                }
            });

            holder.vacationImageView.setImageURI(current.photo);
            holder.vacationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVacationClick(vacation);
                    }
                }
            });

            holder.vacationOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOverflowClick(v, vacation);
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

    public void setListener(IVacationFieldsEvents listener) {
        this.listener = listener;
    }

    private void updateLabel(View dateView, Calendar calendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        EditText dateText = (EditText) dateView;
        dateText.setText(sdf.format(calendar.getTime()));
    }

    //todo valuta di inserire un metodo che aggiorna la visualizzazione dei campi
    // con notifyDataSetChanged(). Non dovrebbe servire per i campi, solo le vacanze.

    class FieldViewHolder extends RecyclerView.ViewHolder {

        //TODO poi devo salvare anche le altre view che mi interessano
        private final EditText titleFieldView;
        private final EditText periodFromView;
        private final EditText periodToView;
        private final Button photoButtonAddView;
        private final ImageButton photoImageButtonView;

        private FieldViewHolder(View itemView) {
            super(itemView);
            // mi interessa salvare solo alcuni campi
            // sicuro i partecipanti e le foto (per accedere ai bottoni)
            // forse ciò che l'utente scrive nei form. Non è chiara di chi è la responsabilità
            titleFieldView = itemView.findViewById(R.id.input_title);
            periodFromView = itemView.findViewById(R.id.input_period_from);
            periodToView = itemView.findViewById(R.id.input_period_to);
            photoButtonAddView = itemView.findViewById(R.id.input_photo);
            photoImageButtonView = itemView.findViewById(R.id.input_photo_choosed);
        }
    }
}
