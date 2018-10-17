package com.rbiffi.vacationfriend.AppSections.VacationList.Events;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

import java.util.Calendar;
import java.util.List;

public interface IVacationFieldsEvents {

    void onAddPhotoClick(View button, View imageButton);

    void onDateFocus(TextView date, Calendar calendar, DatePickerDialog.OnDateSetListener dateListener);

    void checkDate(TextView date);

    void onTimeFocus(TextView time, Calendar calendar, TimePickerDialog.OnTimeSetListener dateListener);

    void checkTime(TextView time);

    void checkPeriodConsistency(TextView periodFrom, TextView periodTo);

    void onAddParticipantClick(View button, List<Participant> adapter);
}

