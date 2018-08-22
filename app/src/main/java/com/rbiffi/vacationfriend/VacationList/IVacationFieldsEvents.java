package com.rbiffi.vacationfriend.VacationList;


import android.app.DatePickerDialog;
import android.view.View;

import java.util.Calendar;

public interface IVacationFieldsEvents {

    void onAddPhotoClick(View button, View imageButton);

    void onDateFocus(View date, boolean hasFocus, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener);

    void onAddPartecipantClick();
}
