package com.rbiffi.vacationfriend.VacationList;

import android.view.View;

import com.rbiffi.vacationfriend.Utils.VacationLite;

public interface IClickEvents {

    void vacationClick(VacationLite vacation);

    void overflowClick(View v, VacationLite vacation);
}
