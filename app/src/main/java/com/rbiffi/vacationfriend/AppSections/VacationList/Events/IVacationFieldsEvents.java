package com.rbiffi.vacationfriend.AppSections.VacationList.Events;


import android.app.DatePickerDialog;
import android.view.View;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.ParticipantAdapter;

import java.util.Calendar;

public interface IVacationFieldsEvents {

    void onAddPhotoClick(View button, View imageButton);

    void onDateFocus(View date, boolean hasFocus, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener);

    void onAddParticipantClick(View button, ParticipantAdapter adapter);
}
