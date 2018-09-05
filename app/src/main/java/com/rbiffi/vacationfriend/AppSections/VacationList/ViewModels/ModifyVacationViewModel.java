package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.support.annotation.NonNull;

public class ModifyVacationViewModel extends ChangeVacationViewModel {

    public static final long FIRST_EXECUTION = -1;
    private long modifiedVacationId;

    public ModifyVacationViewModel(@NonNull Application app) {
        super(app);
        modifiedVacationId = FIRST_EXECUTION;
    }

    public long getVacationId() {
        return modifiedVacationId;
    }

    public void setVacationId(long vacationId) {
        this.modifiedVacationId = vacationId;
    }

}
