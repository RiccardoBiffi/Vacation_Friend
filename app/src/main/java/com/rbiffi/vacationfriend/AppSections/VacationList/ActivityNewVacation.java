package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.FieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.ParticipantAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationFieldsEvents;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Utils.FieldLists;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class ActivityNewVacation
        extends AppCompatActivity
        implements
        IVacationFieldsEvents,
        FragmentAddParticipantsDialog.IAddParticipantsListener {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.ActivityNewVacation.REPLY";
    public static final String TITLE_FIELD = "_title";
    private static final int PICK_IMAGE = 1;

    private VacationViewModel viewModel; // todo mantieni lo stato dei vari campi

    private Button confirm;
    private Button discard;
    private Button vacationImageAddButton;
    private ImageButton vacationImageButton;

    private RecyclerView vacationFieldsList;
    private FieldListAdapter fieldListAdapter;
    private RecyclerView.LayoutManager fieldLayout;

    private ParticipantAdapter participantAdapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vacation);

        restoreState(savedInstanceState);
        setupActionBar();
        setupListWithAdapter();

        confirm = findViewById(R.id.saveBottonAction);
        discard = findViewById(R.id.undoBottonAction);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                //todo delega di validare i campi obbligatori
                //todo delega di raccgoliere i risultati
                //todo costruisci la risposta
                /*
                if(TextUtils.isEmpty(vTitle.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String title = vTitle.getText().toString();
                    String notes = vNotes.getText().toString();

                    replyIntent.putExtra(EXTRA_REPLY + TITLE_FIELD, title);
                    replyIntent.putExtra(EXTRA_REPLY + NOTES_FIELD, notes);
                    setResult(RESULT_OK, replyIntent);
                }
                */
                finish(); // restituisce il risultato a chi ha chiamato l'activity
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void restoreState(Bundle savedInstanceState) {
        //todo viewmodel per mantenere i dati inseriti dall'utente
        viewModel = ViewModelProviders.of(this).get(VacationViewModel.class);
        // salva tutto, soprattutto lista partecipanti selezionati e photo vacanza
        // gli altri campi li salvo anche nel nel onSaveInstanceState xkè leggeri

        if (viewModel.getFieldTitle() == null && savedInstanceState != null) {
            String title = savedInstanceState.getString("inputTitle");
            viewModel.setFieldTitle(title);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText inputTitle = vacationFieldsList.findViewById(R.id.input_title);
        viewModel.setFieldTitle(inputTitle.getText().toString());
        outState.putString("inputTitle", inputTitle.getText().toString());
    }

    private void setupListWithAdapter() {
        vacationFieldsList = findViewById(R.id.vacationFieldsList);
        vacationFieldsList.addItemDecoration(new DividerItemDecoration(vacationFieldsList.getContext(), DividerItemDecoration.VERTICAL));
        fieldListAdapter = new FieldListAdapter(getApplicationContext(), FieldLists.getVacationFieldList(), viewModel);
        fieldListAdapter.setListener(this);
        vacationFieldsList.setAdapter(fieldListAdapter);

        fieldLayout = new LinearLayoutManager(getApplicationContext());
        vacationFieldsList.setLayoutManager(fieldLayout);
    }

    private void setupActionBar() {
        toolbar = findViewById(R.id.toolbarNewVacation);
        setSupportActionBar(toolbar); // trasforma la toolbar in una action bar

        // mostra il back alla activity precedente
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.acivity_title_new_vacation);
    }

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
            DatePickerDialog dateDialog = new DatePickerDialog(ActivityNewVacation.this, dateListener, year, month, day);
            dateDialog.show();
            date.clearFocus();
        }
    }

    @Override
    public void onAddPhotoClick(View button, View imageButton) {
        vacationImageAddButton = vacationImageAddButton == null ? (Button) button : vacationImageAddButton;
        vacationImageButton = vacationImageButton == null ? (ImageButton) imageButton : vacationImageButton;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Drawable userImage = Drawable.createFromStream(inputStream, imageUri.toString());
                vacationImageAddButton.setVisibility(View.GONE);

                vacationImageButton.setBackground(userImage);
                vacationImageButton.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File non trovato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAddParticipantClick(View button, ParticipantAdapter adapter) {
        participantAdapter = adapter;
        FragmentAddParticipantsDialog dialogFragment = new FragmentAddParticipantsDialog();
        List<Participant> participantList = participantAdapter.getParticipantList();
        dialogFragment.setSelectedParticipants(participantList);
        dialogFragment.show(getSupportFragmentManager(), "FragmentAddParticipantsDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, List<Participant> selectedParticipants) {
        participantAdapter.setParticipantList(selectedParticipants);
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
