package com.rbiffi.vacationfriend.VacationList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.FieldListAdapter;
import com.rbiffi.vacationfriend.Repository.IUserEditableObject;
import com.rbiffi.vacationfriend.Repository.Vacation;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewVacationActivity
        extends AppCompatActivity
        implements
            IVacationFieldsClickEvents,
            DatePickerDialog.OnDateSetListener {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.NewVacationActivity.REPLY";
    public static final String TITLE_FIELD = "_title";
    public static final String NOTES_FIELD = "_notes";
    private static final int PICK_IMAGE = 1;

    private Button confirm;
    private Button discard;
    private Button vacationImage;

    private RecyclerView vacationFieldsList;
    private FieldListAdapter fieldListAdapter;
    private RecyclerView.LayoutManager fieldLayout;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vacation);
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

    private void setupListWithAdapter() {
        vacationFieldsList = findViewById(R.id.vacationFieldsList);
        vacationFieldsList.addItemDecoration(new DividerItemDecoration(vacationFieldsList.getContext(), DividerItemDecoration.VERTICAL));
        IUserEditableObject v = new Vacation();
        fieldListAdapter = new FieldListAdapter(getApplicationContext(), v);
        fieldListAdapter.setListener(this);
        vacationFieldsList.setAdapter(fieldListAdapter);

        fieldLayout = new LinearLayoutManager(getApplicationContext());
        vacationFieldsList.setLayoutManager(fieldLayout);

        //todo viewmodel per mantenere i dati inseriti dall'utente

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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //todo inserisci la data scelta sull'edit text corretto.
    }

    @Override
    public void onAddPhotoClick(View button) {
        vacationImage = (Button) button;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE ){
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Drawable userImage = Drawable.createFromStream(inputStream, imageUri.toString());
                vacationImage.setBackground(userImage);
                vacationImage.setText("");
                vacationImage.setCompoundDrawables(null,null,null,null);
                vacationImage.getLayoutParams().height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                vacationImage.requestLayout();

            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File non trovato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDateClick() {

    }

    @Override
    public void onAddPartecipantClick() {

    }
}
