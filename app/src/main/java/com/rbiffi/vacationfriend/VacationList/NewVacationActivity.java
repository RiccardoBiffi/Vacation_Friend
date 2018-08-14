package com.rbiffi.vacationfriend.VacationList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rbiffi.vacationfriend.R;

public class NewVacationActivity extends AppCompatActivity {

    // per rendere la risposta univoca a questa classe
    public static final String EXTRA_REPLY = "com.rbiffi.vacationfriend.NewVacationActivity.REPLY";
    public static final String TITLE_FIELD = "_title";
    public static final String NOTES_FIELD = "_notes";

    private EditText vTitle;
    private EditText vNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vacation);

        vTitle = findViewById(R.id.vacationTitle);
        vNotes = findViewById(R.id.vacationNotes);

        final Button save = findViewById(R.id.saveBottonAction);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(vTitle.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String title = vTitle.getText().toString();
                    String notes = vNotes.getText().toString();

                    replyIntent.putExtra(EXTRA_REPLY + TITLE_FIELD, title);
                    replyIntent.putExtra(EXTRA_REPLY + NOTES_FIELD, notes);
                    setResult(RESULT_OK, replyIntent);
                }
                finish(); // restituisce il risultato a chi ha chiamato l'activity
            }
        });

    }
}
