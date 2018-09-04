package com.rbiffi.vacationfriend.AppSections.VacationList;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.FieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ParticipantsDialogViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.MyDividerItemDecoration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public abstract class ActivityEditAppObject
        extends AppCompatActivity
        implements
        IVacationFieldsEvents,
        FragmentAddParticipantsDialog.IAddParticipantsListener,
        VacationFriendRepository.IRepositoryListener {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.ActivityNewVacation.REPLY";
    public static final String VACATION_ID = "_vacation";
    protected static final int PICK_IMAGE = 1;

    protected Toolbar toolbar;

    protected Button confirm;
    protected Button discard;

    protected Button vacationImageAddButton; // todo valuta se rimuoverli da qua
    protected ImageButton vacationImageButton; // fanno parte dell'adapter, photo field

    protected RecyclerView vacationFieldsList;
    protected FieldListAdapter fieldListAdapter;
    protected RecyclerView.LayoutManager fieldLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appobject);

        getActivityViewModel();
        saveDataFromIntentMaybe();
        restoreState(savedInstanceState);
        setupListWithAdapter();
        setupActionBar();
        setupActivityButtons();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo delega di validare i campi obbligatori
                //todo delega di raccgoliere i risultati
                //todo costruisci la risposta
                collectAndSaveObject();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        fieldListAdapter = createFieldAdapter();
        fieldListAdapter.setListener(this);
        vacationFieldsList.setAdapter(fieldListAdapter);

        fieldLayout = new LinearLayoutManager(getApplicationContext());
        vacationFieldsList.setLayoutManager(fieldLayout);
    }

    protected abstract FieldListAdapter createFieldAdapter();

    protected void setupActionBar() {
        toolbar = findViewById(R.id.toolbarNewVacation);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar

        // mostra il back alla activity precedente
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setActionBarTitle();
    }

    protected abstract void setActionBarTitle();

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

    @Override
    public void onDateFocus(final View date, boolean hasFocus, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener) {
        if (hasFocus) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dateDialog = new DatePickerDialog(this, dateListener, year, month, day);
            dateDialog.show();
        }
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
    public void onAddParticipantClick(View button, List<Participant> partecipants) {
        ParticipantsDialogViewModel pdvm = ViewModelProviders.of(this).get(ParticipantsDialogViewModel.class);
        pdvm.setSelectedParticipants(partecipants);
        FragmentAddParticipantsDialog dialogFragment = new FragmentAddParticipantsDialog();
        dialogFragment.setSelectedParticipants(partecipants);
        dialogFragment.show(getSupportFragmentManager(), "FragmentAddParticipantsDialog");
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
    public abstract void onInsertComplete(long rowId);

}
