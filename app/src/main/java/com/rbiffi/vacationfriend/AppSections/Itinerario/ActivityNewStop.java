package com.rbiffi.vacationfriend.AppSections.Itinerario;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.EditAppObjectViewModel;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Utils.ActivityEditAppObject;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.Utils.FieldLists;

import java.util.List;

public class ActivityNewStop
        extends ActivityEditAppObject

{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int checkFormValidity() {
        // restituisce l'indice del primo elemento non valido, altrimenti -1
        if (viewModel.getTitle().isEmpty()) {
            return 0;
        }

        if (viewModel.getDateField().isEmpty()) {
            return 2; // todo mostra errore
        }

        if (viewModel.getTimeMode() == EditAppObjectViewModel.TIME_ARRIVAL) {
            if (viewModel.getTimeArrivalField().isEmpty()) return 3; // todo mostra errore
        } else {
            if (viewModel.getTimeDepartureField().isEmpty()) return 3; // todo mostra errore
        }

        return -1;
    }

    @Override
    protected void getActivityViewModel() {
        viewModel = ViewModelProviders.of(this).get(EditAppObjectViewModel.class);
    }

    @Override
    protected void saveDataFromIntentMaybe() {
        // utilizzato quando l'activity chiamante passa un App object, non per il NEW
    }

    @Override
    protected void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String title = savedInstanceState.getString(Constants.IN_TITLE);
            if (title != null) saveTitleField(title);

            String place = savedInstanceState.getString(Constants.IN_PLACE);
            if (place != null) savePlaceField(place);

            String day = savedInstanceState.getString(Constants.IN_DATE);
            if (day != null) saveDateField(day);

            String timeArr = savedInstanceState.getString(Constants.IN_TIMEARRIVAL);
            if (timeArr != null) saveTimeArrivalField(timeArr);

            String timeDep = savedInstanceState.getString(Constants.IN_TIMEDEPARTURE);
            if (timeDep != null) saveTimeDepartureField(timeDep);

            String icon = savedInstanceState.getString(Constants.IN_ROUTEICON);
            if (icon != null) saveIconField(Uri.parse(icon));
        }
        // else leggo tutto dal view model
    }

    @Override
    protected EditFieldListAdapter createFieldAdapter() {
        return new EditFieldListAdapter(getApplicationContext(), FieldLists.getEditFieldList(Stop.class));
    }

    @Override
    protected void collectAndSaveObject() {

    }

    @Override
    protected void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants) {
        // do nothing
    }

    @Override
    public void onVacationOperationComplete(long rowId) {

    }
}
