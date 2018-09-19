package com.rbiffi.vacationfriend.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.FragmentAddParticipantsDialog;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ParticipantsDialogViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        VacationFriendRepository.IRepositoryListener {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.ActivityNewVacation.REPLY";
    public static final String VACATION = "_vacation";
    protected static final int PICK_IMAGE = 1;

    protected Toolbar toolbar;

    protected Button confirm;
    protected Button discard;

    protected Button vacationImageAddButton; // todo valuta se rimuoverli da qua
    protected ImageButton vacationImageButton; // fanno parte dell'adapter, photo field
    protected Button addParticipantsButton;
    protected TextView vacationFieldTitle;
    protected TextView vacationFieldPeriodFrom;
    protected TextView vacationFieldPeriodTo;

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
                int valid = checkFormValidity();
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
                    vacationFieldTitle.setError(getString(R.string.err_vacationlist_title));
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
                }
            }
        });

    }


    protected abstract void getActivityViewModel();

    protected abstract void saveDataFromIntentMaybe();

    public void onSaveInstanceState(Bundle outState) {

        // non controllo se gli elementi sono nulli perché i viewmodel li inizializzano sempre
        String titleField = getTitleField();
        outState.putString("inputTitle", titleField);

        String periodFromField = getPeriodFromField();
        outState.putString("inputPeriodFrom", periodFromField);

        String periodToField = getPeriodToField();
        outState.putString("inputPeriodTo", periodToField);

        String placeField = getPlaceField();
        outState.putString("inputPlace", placeField);

        Uri photoField = getPhotoField();
        outState.putString("inputPhoto", photoField.toString());

        // la lista di partecipanti non la salvo perché può essere corposa
        // sarà solo nel viewmodel

        //todo altri campi da ripristinare, devo considerarli tutti

        super.onSaveInstanceState(outState);
    }

    public abstract String getTitleField();

    public abstract String getPeriodFromField();

    public abstract String getPeriodToField();

    public abstract String getPlaceField();

    public abstract Uri getPhotoField();

    protected void restoreState(Bundle savedInstanceState) {
        //todo salva in Constants le chiavi dei campi
        if (savedInstanceState != null) {
            String title = savedInstanceState.getString("inputTitle");
            if (title != null) saveTitleField(title);

            String dateFrom = savedInstanceState.getString("inputPeriodFrom");
            if (dateFrom != null) saveDateFromField(dateFrom);

            String dateTo = savedInstanceState.getString("inputPeriodTo");
            if (dateTo != null) saveDateToField(dateTo);

            String place = savedInstanceState.getString("inputPlace");
            if (place != null) savePlaceField(place);

            String photo = savedInstanceState.getString("inputPhoto");
            if (photo != null) savePhotoField(Uri.parse(photo));

            //todo altri campi da ripristinare, devo considerarli tutti
        }
        // else leggo tutto dal view model
    }

    public abstract void saveTitleField(String title);

    public abstract void saveDateFromField(String dateFrom);

    public abstract void saveDateToField(String dateTo);

    public abstract void savePlaceField(String place);

    public abstract void savePhotoField(Uri photo);

    private void setupListWithAdapter() {
        vacationFieldsList = findViewById(R.id.vacationFieldsList);
        vacationFieldsList.addItemDecoration(new MyDividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_divider)));
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
            date.setError(getString(R.string.err_vacationlist_title));
        } else {
            date.setError(null);
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

    protected abstract void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                savePhotoField(imageUri);
                Drawable userImage = Drawable.createFromStream(inputStream, imageUri.toString());
                vacationImageAddButton.setVisibility(View.GONE);

                vacationImageButton.setBackground(userImage);
                vacationImageButton.setVisibility(View.VISIBLE);
                vacationImageButton.requestLayout();
                vacationImageButton.getParent().requestChildFocus(vacationImageButton, vacationImageButton);

            } catch (FileNotFoundException e) {
                Toast.makeText(this, R.string.err_photo_not_found, Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED && requestCode == PICK_IMAGE) {
            if (getPhotoField() == null) {
                vacationImageButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public abstract void onVacationOperationComplete(long rowId);
}
