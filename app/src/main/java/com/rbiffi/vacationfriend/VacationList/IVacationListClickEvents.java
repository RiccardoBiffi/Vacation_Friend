package com.rbiffi.vacationfriend.VacationList;

import android.view.View;

import com.rbiffi.vacationfriend.Utils.VacationLite;

public interface IVacationListClickEvents {

    void onVacationClick(VacationLite vacation);

    void onOverflowClick(View v, VacationLite vacation);
}
