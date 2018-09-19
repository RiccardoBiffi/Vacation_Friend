package com.rbiffi.vacationfriend.AppSections.VacationList.Events;

import android.view.View;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;

public interface IVacationListClickEvents {

    void onVacationClick(Vacation vacation);

    void onOverflowClick(View v, Vacation vacation);
}
