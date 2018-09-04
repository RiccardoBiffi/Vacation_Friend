package com.rbiffi.vacationfriend.AppSections.VacationList.Events;


import android.app.DatePickerDialog;
import android.view.View;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.Calendar;
import java.util.List;

public interface IVacationFieldsEvents {

    void onAddPhotoClick(View button, View imageButton);

    void onDateFocus(View date, boolean hasFocus, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener);

    void onAddParticipantClick(View button, List<Participant> adapter);
}

