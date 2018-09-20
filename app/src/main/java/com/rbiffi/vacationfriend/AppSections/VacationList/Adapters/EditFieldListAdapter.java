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

import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ChangeVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Utils.ActivityEditAppObject;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.Utils.EditTextValidator;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    Classe per gestire le Recyclerview contenenti i campi degli oggetti da creare/modificare
    al'interno della app. Ogni volta che si crea o modifica un elemento, questo adapter specifica
    quale view usare e cosa succede al suo interno, per ogni campo dell'oggetto.
 */

public class EditFieldListAdapter extends RecyclerView.Adapter<EditFieldListAdapter.FieldViewHolder> {

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

    private ActivityEditAppObject listener;

    public EditFieldListAdapter(Context applicationContext, List<String> fieldList, ChangeVacationViewModel viewModel) {
        inflater = LayoutInflater.from(applicationContext);

        this.appContext = applicationContext;
        this.fieldList = fieldList;
        this.viewModel = viewModel;
    }

    public void setListener(ActivityEditAppObject listener) {
        this.listener = listener;
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                view = inflater.inflate(R.layout.input_title, parent, false);
                break;
            case VIEW_TYPE_PERIOD:
                view = inflater.inflate(R.layout.input_period, parent, false);
                break;
            case VIEW_TYPE_PLACE:
                view = inflater.inflate(R.layout.input_place, parent, false);
                break;
            case VIEW_TYPE_PARTICIPANTS:
                view = inflater.inflate(R.layout.input_partecipants, parent, false);
                break;
            case VIEW_TYPE_PHOTO:
                view = inflater.inflate(R.layout.input_photo, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.input_title, parent, false);
                break;
        }
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FieldViewHolder holder, int position) {
        final String field = fieldList.get(position);
        switch (field) {


            case Constants.F_TITLE:
                listener.setVacationFieldTitle(holder.titleFieldView);
                holder.titleFieldView.setText(viewModel.getFieldTitle());
                holder.titleFieldView.requestFocus();
                //todo open keyboard
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
                            listener.saveTitleField(holder.titleFieldView.getText().toString());
                        }
                    }
                });

                holder.titleFieldView.setOnFocusChangeListener(new EditTextValidator(holder.titleFieldView) {
                    @Override
                    public void validate(final TextView textView, String text) {
                        if (text.isEmpty()) {
                            textView.setError(appContext.getString(R.string.err_vacationlist_title));
                        }
                    }
                });

                break;


            case Constants.F_PERIOD:
                listener.setVacationFieldPeriod(holder.periodFromView, holder.periodToView);
                final Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(holder.periodFromView, calendar);
                        listener.saveDateFromField(holder.periodFromView.getText().toString());
                        listener.checkPeriodConsistency(holder.periodFromView, holder.periodToView);
                    }
                };
                final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(holder.periodToView, calendar);
                        listener.saveDateToField(holder.periodToView.getText().toString());
                        listener.checkPeriodConsistency(holder.periodFromView, holder.periodToView);
                    }
                };

                holder.periodFromView.setInputType(InputType.TYPE_NULL);
                holder.periodFromView.setText(viewModel.getFieldPeriodFrom());
                holder.periodFromView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            v.performClick();
                        } else {
                            listener.checkDate((TextView) v);
                            listener.checkPeriodConsistency((TextView) v, holder.periodFromView);
                        }
                    }
                });
                holder.periodFromView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDateFocus((TextView) v, calendar, fromDateListener);
                    }
                });

                holder.periodToView.setInputType(InputType.TYPE_NULL);
                holder.periodToView.setText(viewModel.getFieldPeriodTo());
                holder.periodToView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            v.performClick();
                        } else {
                            listener.checkDate((TextView) v);
                            listener.checkPeriodConsistency(holder.periodFromView, (TextView) v);
                        }
                    }
                });
                holder.periodToView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDateFocus((TextView) v, calendar, toDateListener);
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
                            listener.savePlaceField(holder.placeView.getText().toString());
                        }
                    }
                });
                break;


            case Constants.F_PARTECIP:
                fieldParticipantsAdapter = new ParticipantAdapter(appContext, R.layout.input_partecipants_row,
                        viewModel.getFieldParticipants(), ParticipantAdapter.MODE_VERTICAL);

                setParticipantsListHeader(holder);
                setParticipantsListFooter(holder);
                holder.partecipantListView.setAdapter(fieldParticipantsAdapter);
                break;


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
                // dati non pronti, placeholder
        }
    }

    private void setParticipantsListFooter(FieldViewHolder holder) {
        View footer = inflater.inflate(R.layout.input_partecipants_footer, null);
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

    private void setParticipantsListHeader(FieldViewHolder holder) {
        View header = inflater.inflate(R.layout.input_partecipants_row, null);
        header.setEnabled(false);

        View participant_row = header.findViewById(R.id.participant_row_element);
        CircleImageView myPicture = header.findViewById(R.id.partecipant_picture);
        TextView myName = header.findViewById(R.id.partecipant_name);
        ImageButton myDeleteButton = header.findViewById(R.id.remove_partic_button);

        //todo le seguenti informazioni dovrebbero essere prelevate altrove, tipo il view model
        myPicture.setImageURI(resourceToURI(appContext, R.drawable.average_man));
        myName.setText(R.string.my_self);
        myDeleteButton.setVisibility(View.GONE);
        participant_row.setFocusable(false);
        participant_row.setClickable(false);

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

        private final ListView partecipantListView;

        private final Button photoButtonAddView;
        private final ImageButton photoImageButtonView;


        FieldViewHolder(View itemView) {
            super(itemView);
            titleFieldView = itemView.findViewById(R.id.input_title);

            periodFromView = itemView.findViewById(R.id.input_period_from);
            periodToView = itemView.findViewById(R.id.input_period_to);

            placeView = itemView.findViewById(R.id.input_place);

            partecipantListView = itemView.findViewById(R.id.input_partes_list);

            photoButtonAddView = itemView.findViewById(R.id.input_photo);
            photoImageButtonView = itemView.findViewById(R.id.input_photo_choosed);

        }

        public EditText getTitleHolder() {
            return this.titleFieldView;
        }
    }

}
