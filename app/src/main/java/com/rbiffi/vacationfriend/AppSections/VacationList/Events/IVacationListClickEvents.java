package com.rbiffi.vacationfriend.AppSections.VacationList.Events;

import android.view.View;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.VacationLite;

public interface IVacationListClickEvents {

    void onVacationClick(VacationLite vacation);

    void onOverflowClick(View v, VacationLite vacation);
}
