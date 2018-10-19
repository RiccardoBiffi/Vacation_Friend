package com.rbiffi.vacationfriend.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.FragmentAddParticipantsDialog;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.EditAppObjectViewModel;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ParticipantsDialogViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class ActivityEditAppObject
        extends AppCompatActivity
        implements
        IVacationFieldsEvents,
        FragmentAddParticipantsDialog.IAddParticipantsListener,
        VacationFriendRepository.IRepositoryListener,
        OnItemSelectedListener {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.ActivityEditAppObject.REPLY";
    protected static final int PICK_IMAGE = 1;

    protected EditAppObjectViewModel viewModel;

    protected Toolbar toolbar;

    protected Button confirm;
    protected Button discard;

    protected Button vacationImageAddButton; // todo valuta se rimuoverli da qua
    protected ImageButton vacationImageButton; // fanno parte dell'adapter, photo field
    protected Button addParticipantsButton;

    protected TextView vacationFieldTitle;

    protected TextView vacationFieldPeriodFrom;
    protected TextView vacationFieldPeriodTo;

    protected EditText vacationFieldDate;
    protected EditText vacationFieldTimeArrival;
    protected EditText vacationFieldTimeDeparture;

    private EditText arrivalTime;
    private ViewGroup departureTime;

    protected RecyclerView vacationFieldsList;
    protected EditFieldListAdapter editFieldListAdapter;
    protected RecyclerView.LayoutManager fieldLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_object);

        getActivityViewModel();
        saveDataFromIntentMaybe();
        restoreState(savedInstanceState);
        setupListWithAdapter();
        setupActionBar();
        setupActivityButtons();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valid = checkFormValidity(); //todo restituisci un oggetto <posizione, errMsg>
                if (isFormValid(valid))
                    collectAndSaveObject();
                else
                    scrollToTop(valid);
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isFormValid(int valid) {
        return valid < 0;
    }

    protected abstract int checkFormValidity();

    private void scrollToTop(final int position) {
        vacationFieldsList.post(new Runnable() {
            @Override
            public void run() {
                vacationFieldsList.smoothScrollToPosition(position);
                if (position == 0) {
                    vacationFieldTitle.setError(getString(R.string.err_field_title));
                } else if (position == 1) {
                    if (!vacationFieldPeriodFrom.getText().toString().isEmpty())
                        vacationFieldPeriodTo.setError(getString(R.string.err_vacationlist_date_wrong));

                    if (!vacationFieldPeriodTo.getText().toString().isEmpty())
                        vacationFieldPeriodFrom.setError(getString(R.string.err_vacationlist_date_wrong));

                    if (vacationFieldPeriodFrom.getText().toString().isEmpty() &&
                            vacationFieldPeriodTo.getText().toString().isEmpty()) {
                        vacationFieldPeriodFrom.setError(getString(R.string.err_vacationlist_date_missing));
                        vacationFieldPeriodTo.setError(getString(R.string.err_vacationlist_date_missing));
                    }
                } else if (position == 2) {
                    vacationFieldDate.setError(getString(R.string.err_vacationlist_date_missing));
                } else if (position == 3) {
                    if (viewModel.getTimeMode() == 0)
                        vacationFieldTimeArrival.setError(getString(R.string.err_time_missing));
                    else
                        vacationFieldTimeDeparture.setError(getString(R.string.err_time_missing));
                }
            }
        });
    }

    protected abstract void getActivityViewModel();

    protected abstract void saveDataFromIntentMaybe();

    protected abstract void restoreState(Bundle savedInstanceState);

    private void setupListWithAdapter() {
        vacationFieldsList = findViewById(R.id.vacationFieldsList);
        vacationFieldsList.addItemDecoration(new NoFooterDividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_divider)));
        editFieldListAdapter = createFieldAdapter();
        editFieldListAdapter.setListener(this);
        vacationFieldsList.setAdapter(editFieldListAdapter);

        fieldLayout = new LinearLayoutManager(getApplicationContext());
        vacationFieldsList.setLayoutManager(fieldLayout);
    }


    protected abstract EditFieldListAdapter createFieldAdapter();

    protected void setupActionBar() {
        toolbar = findViewById(R.id.toolbarNewVacation);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    protected void setupActivityButtons() {
        confirm = findViewById(R.id.saveBottonAction);
        discard = findViewById(R.id.undoBottonAction);
    }

    public String getTitleField() {
        return viewModel.getTitle();
    }

    public void saveTitleField(String title) {
        viewModel.setTitle(title);
    }

    public String getPeriodFromField() {
        return viewModel.getPeriodFrom();
    }

    public void savePeriodFromField(String dateFrom) {
        viewModel.setPeriodFrom(dateFrom);
    }

    public String getPeriodToField() {
        return viewModel.getPeriodTo();
    }

    public void savePeriodToField(String dateTo) {
        viewModel.setPeriodTo(dateTo);
    }

    public String getPlaceField() {
        return viewModel.getPlace();
    }

    public void savePlaceField(String place) {
        viewModel.setPlace(place);
    }

    public Uri getPhotoField() {
        return viewModel.getPhoto();
    }

    public void savePhotoField(Uri photo) {
        viewModel.setPhoto(photo);
    }

    public String getDateField() {
        return viewModel.getDateField();
    }

    public void saveDateField(String date) {
        viewModel.setDateField(date);
    }

    public String getTimeArrivalField() {
        return viewModel.getTimeArrivalField();
    }

    public void saveTimeArrivalField(String arrivalTime) {
        viewModel.setTimeArrivalField(arrivalTime);
    }

    public String getTimeDepartureField() {
        return viewModel.getTimeDepartureField();
    }

    public void saveTimeDepartureField(String departureTime) {
        viewModel.setTimeDepartureField(departureTime);

    }

    public int getStopIconField() {
        return viewModel.getStopIconResourceField();
    }

    public void saveStopIconField(int icon) {
        viewModel.setStopIconResourceField(icon);
    }

    public String getNotesField() {
        return viewModel.getNotesField();
    }

    public void saveNotesField(String notes) {
        viewModel.setNotesField(notes);
    }

    protected abstract void collectAndSaveObject();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setVacationFieldTitle(EditText titleView) {
        this.vacationFieldTitle = titleView;
    }

    public void setVacationFieldPeriod(EditText periodFromView, EditText periodToView) {
        this.vacationFieldPeriodFrom = periodFromView;
        this.vacationFieldPeriodTo = periodToView;
    }

    public void setVacationFieldDate(EditText dateView) {
        this.vacationFieldDate = dateView;
    }

    public void setVacationFieldTime(EditText arrivalView, EditText departureView) {
        this.vacationFieldTimeArrival = arrivalView;
        this.vacationFieldTimeDeparture = departureView;
    }

    protected boolean checkDateConsistency(String periodFrom, String periodTo) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = format.parse(periodFrom);
            endDate = format.parse(periodTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate.compareTo(endDate) <= 0;
    }

    @Override
    public void onDateFocus(final TextView date, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener) {
        hideKeyboard(this);
        date.setError(null);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(this, dateListener, year, month, day);
        dateDialog.show();
    }

    @Override
    public void checkDate(TextView date) {
        if (date.getText().toString().isEmpty()) {
            date.setError(getString(R.string.err_field_title));
        } else {
            date.setError(null);
        }
    }

    @Override
    public void onTimeFocus(TextView time, Calendar calendar, TimePickerDialog.OnTimeSetListener dateListener) {
        hideKeyboard(this);
        time.setError(null);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timeDialog = new TimePickerDialog(this, dateListener, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
        timeDialog.show();
    }

    @Override
    public void checkTime(TextView time) {
        if (time.getText().toString().isEmpty()) {
            time.setError(getString(R.string.err_field_title));
        } else {
            time.setError(null);
        }
    }

    @Override
    public void checkPeriodConsistency(TextView periodFrom, TextView periodTo) {
        if (!periodFrom.getText().toString().isEmpty() &&
                !periodTo.getText().toString().isEmpty()
                ) {
            boolean consistent = checkDateConsistency(periodFrom.getText().toString(), periodTo.getText().toString());

            if (!consistent) {
                periodFrom.setError(getString(R.string.err_vacationlist_date_wrong));
                periodTo.setError(getString(R.string.err_vacationlist_date_wrong));
            } else {
                periodFrom.setError(null);
                periodTo.setError(null);
            }
        }
    }

    @Override
    public void onAddParticipantClick(View button, List<Participant> partecipants) {
        addParticipantsButton = (Button) button;
        hideKeyboard(this);
        ParticipantsDialogViewModel pdvm = ViewModelProviders.of(this).get(ParticipantsDialogViewModel.class);
        pdvm.setSelectedParticipants(partecipants);
        FragmentAddParticipantsDialog dialogFragment = new FragmentAddParticipantsDialog();
        dialogFragment.setSelectedParticipants(partecipants);
        dialogFragment.show(getSupportFragmentManager(), "FragmentAddParticipantsDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, List<Participant> selectedParticipants) {
        saveParticipantsAndUpdateAdapter(selectedParticipants);
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onAddPhotoClick(View button, View imageButton) {
        vacationImageAddButton = vacationImageAddButton == null ? (Button) button : vacationImageAddButton;
        vacationImageButton = vacationImageButton == null ? (ImageButton) imageButton : vacationImageButton;
        if (getPhotoField().toString().equals("")) {
            vacationImageButton.setVisibility(View.INVISIBLE);
        }

        Intent intent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    protected abstract void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants);

    @Override
    public abstract void onVacationOperationComplete(long rowId);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_time_modes) {
            String selectedSpinnerItem = (String) parent.getItemAtPosition(position);
            if (isInArrivalMode(selectedSpinnerItem)) {
                arrivalTime.setVisibility(View.VISIBLE);
                departureTime.setVisibility(View.GONE);
                viewModel.setRouteTimeMode(EditAppObjectViewModel.TIME_ARRIVAL);
            } else {
                arrivalTime.setVisibility(View.INVISIBLE);
                departureTime.setVisibility(View.VISIBLE);
                viewModel.setRouteTimeMode(EditAppObjectViewModel.TIME_DEPARTURE);
            }
        } else { // la view è il posto di partenza
            viewModel.setRouteDeparturePlacePosition(position);
        }
    }

    private boolean isInArrivalMode(String spinnerItem) {
        return spinnerItem.equals(getResources().getString(R.string.time_mode_arrival));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setArrivalTimeView(EditText arrivalTimeView) {
        this.arrivalTime = arrivalTimeView;
    }

    public void setDepartureTimeView(ViewGroup departureGroupView) {
        this.departureTime = departureGroupView;
    }

    public int getRouteDeparturePlacePosition() {
        return viewModel.getRouteDeparturePlacePosition();
    }

    public List<Participant> getParticipantsField() {
        return viewModel.getParticipants();
    }

    public int getTimeMode() {
        return viewModel.getTimeMode();
    }

}
