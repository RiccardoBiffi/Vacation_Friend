package com.rbiffi.vacationfriend.AppSections.VacationList.Adapters;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ChangeVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Utils.Constants;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/*  Classe per gestire le Recyclerview contenenti i campi degli oggetti da creare/modificare
    al'interno della app. Ogni volta che si crea o modifica un elemento, questo adapter specifica
    quale view usare e cosa succede al suo interno, per ogni campo dell'oggetto.
 */

public class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {

    //TODO lista di interi per tutti i possibili fieldList di tutta la App
    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_PERIOD = 1;
    private static final int VIEW_TYPE_PLACE = 2;
    private static final int VIEW_TYPE_PARTICIPANTS = 3;
    private static final int VIEW_TYPE_PHOTO = 4;
    private static final int VIEW_TYPE_NOTES = 5;
    //...

    private final LayoutInflater inflater;

    private Context appContext;
    private List<String> fieldList;
    private ChangeVacationViewModel viewModel; // per facilitare il passaggio di dati

    private ParticipantAdapter fieldParticipantsAdapter;

    private IVacationFieldsEvents listener;

    public FieldListAdapter(Context applicationContext, List<String> fieldList, ChangeVacationViewModel viewModel) {
        inflater = LayoutInflater.from(applicationContext);

        appContext = applicationContext;
        this.fieldList = fieldList;
        this.viewModel = viewModel;
    }

    public void setListener(IVacationFieldsEvents listener) {
        this.listener = listener;
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            case VIEW_TYPE_PARTICIPANTS:
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


            case Constants.F_TITLE:
                holder.titleFieldView.setText(viewModel.getFieldTitle());
                holder.titleFieldView.requestFocus();
                holder.titleFieldView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (listener != null) {
                            listener.saveFieldTitleState(holder.titleFieldView.getText().toString());
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
                        listener.saveFieldPeriodFromState(holder.periodFromView.getText().toString());
                    }
                };
                final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(holder.periodToView, calendar);
                        listener.saveFieldPeriodToState(holder.periodToView.getText().toString());
                    }
                };

                holder.periodFromView.setInputType(InputType.TYPE_NULL);
                holder.periodFromView.setText(viewModel.getFieldPeriodFrom());
                holder.periodFromView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (listener != null) {
                            listener.onDateFocus(v, hasFocus, calendar, fromDateListener);
                        }
                    }
                });

                holder.periodToView.setInputType(InputType.TYPE_NULL);
                holder.periodToView.setText(viewModel.getFieldPeriodTo());
                holder.periodToView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (listener != null) {
                            listener.onDateFocus(v, hasFocus, calendar, toDateListener);
                        }
                    }
                });
                break;


            case Constants.F_PLACE:
                holder.placeView.setText(viewModel.getFieldPlace());
                holder.placeView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (listener != null) {
                            listener.saveFieldPlaceState(holder.placeView.getText().toString());
                        }
                    }
                });
                break;


            case Constants.F_PARTECIP:
                fieldParticipantsAdapter = new ParticipantAdapter(appContext, R.layout.field_partecipants_row, viewModel.getFieldParticipants());

                setPartecipantsListHeader(holder);
                setParticipantsListFooter(holder);
                holder.partecipantListView.setAdapter(fieldParticipantsAdapter);
                break;


            case Constants.F_PHOTO:
                holder.photoButtonAddView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // devo utilizzare l'activity per chiamare startActivityForResult
                        // ed ascoltare il risultato
                        if (listener != null) {
                            listener.onAddPhotoClick(v, holder.photoImageButtonView);
                        }
                    }
                });
                holder.photoImageButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // devo utilizzare l'activity per chiamare startActivityForResult
                        // ed ascoltare il risultato
                        if (listener != null) {
                            listener.onAddPhotoClick(holder.photoButtonAddView, v);
                        }
                    }
                });

                if (!viewModel.getFieldPhoto().toString().equals("")) {
                    try {
                        Uri imageUri = viewModel.getFieldPhoto();
                        InputStream inputStream = appContext.getContentResolver().openInputStream(imageUri);
                        Drawable userImage = Drawable.createFromStream(inputStream, imageUri.toString());
                        holder.photoButtonAddView.setVisibility(View.GONE);

                        holder.photoImageButtonView.setBackground(userImage);
                        holder.photoImageButtonView.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(appContext, "File non trovato", Toast.LENGTH_SHORT).show();
                    }
                }

                break;


            default:
                //todo valuta cosa inserire qua
                // dati non pronti, placeholder
        }
    }

    private void setParticipantsListFooter(FieldViewHolder holder) {
        View footer = inflater.inflate(R.layout.field_partecipants_footer, null);
        footer.findViewById(R.id.input_add_partic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddParticipantClick(v, viewModel.getFieldParticipants());
                }
            }
        });
        holder.partecipantListView.addFooterView(footer);
    }

    private void setPartecipantsListHeader(FieldViewHolder holder) {
        View header = inflater.inflate(R.layout.field_partecipants_row, null);
        header.setEnabled(false);

        CircleImageView myPicture = header.findViewById(R.id.partecipant_picture);
        TextView myName = header.findViewById(R.id.partecipant_name);
        ImageButton myDeleteButton = header.findViewById(R.id.remove_partic_button);

        //todo le seguenti informazioni dovrebbero essere prelevate altrove, tipo il view model
        myPicture.setImageURI(resourceToURI(appContext, R.drawable.average_man));
        myName.setText(R.string.my_self);
        myDeleteButton.setVisibility(View.GONE);
        holder.partecipantListView.addHeaderView(header);
    }

    private Uri resourceToURI(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID));
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

    public void updateParticipants(List<Participant> fieldParticipants) {
        fieldParticipantsAdapter.updateParticipants(fieldParticipants);
    }

    private void updateLabel(View dateView, Calendar calendar) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        EditText dateText = (EditText) dateView;
        dateText.setText(sdf.format(calendar.getTime()));
    }


    public static class FieldViewHolder extends RecyclerView.ViewHolder {

        public final EditText titleFieldView;

        private final EditText periodFromView;
        private final EditText periodToView;

        private final EditText placeView;

        private final Button photoButtonAddView;
        private final ImageButton photoImageButtonView;

        private final ListView partecipantListView;

        FieldViewHolder(View itemView) {
            super(itemView);
            // mi interessa salvare solo alcuni campi
            // sicuro i partecipanti e le foto (per accedere ai bottoni)
            // forse ciò che l'utente scrive nei form. Non è chiara di chi è la responsabilità
            titleFieldView = itemView.findViewById(R.id.input_title);

            periodFromView = itemView.findViewById(R.id.input_period_from);
            periodToView = itemView.findViewById(R.id.input_period_to);

            placeView = itemView.findViewById(R.id.input_place);

            photoButtonAddView = itemView.findViewById(R.id.input_photo);
            photoImageButtonView = itemView.findViewById(R.id.input_photo_choosed);

            partecipantListView = itemView.findViewById(R.id.input_partes_list);
        }


    }
}
