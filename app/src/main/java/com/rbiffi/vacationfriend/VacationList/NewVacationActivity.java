package com.rbiffi.vacationfriend.VacationList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.FieldListAdapter;
import com.rbiffi.vacationfriend.Repository.IUserEditableObject;
import com.rbiffi.vacationfriend.Repository.Vacation;

public class NewVacationActivity extends AppCompatActivity {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.NewVacationActivity.REPLY";
    public static final String TITLE_FIELD = "_title";
    public static final String NOTES_FIELD = "_notes";

    private Button confirm;
    private Button discard;

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
}
