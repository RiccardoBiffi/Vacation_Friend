package com.rbiffi.vacationfriend.VacationList;


import android.app.DatePickerDialog;
import android.view.View;

import com.rbiffi.vacationfriend.Repository.ParticipantAdapter;

import java.util.Calendar;

public interface IVacationFieldsEvents {

    void onAddPhotoClick(View button, View imageButton);

    void onDateFocus(View date, boolean hasFocus, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener);

    void onAddParticipantClick(View button, ParticipantAdapter adapter);
}
