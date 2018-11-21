package com.rbiffi.vacationfriend.AppSections.Itinerario;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.EditAppObjectViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Utils.ActivityEditAppObject;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.Utils.FieldLists;

import java.util.List;

public class ActivityNewStop
        extends ActivityEditAppObject
{

    private static final String ROUTE_STOP = "_routeStop";

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
            return 2;
        }

        if (viewModel.getTimeMode() == EditAppObjectViewModel.TIME_ARRIVAL) {
            if (viewModel.getTimeArrivalField().isEmpty()) return 3;
        } else {
            if (viewModel.getTimeDepartureField().isEmpty()) return 3;
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
    protected String getFieldState() {
        String result = "";
        result += viewModel.getTitle();
        result += viewModel.getPlace();
        result += viewModel.getDateField();
        result += viewModel.getTimeMode();
        if (viewModel.getTimeMode() == EditAppObjectViewModel.TIME_ARRIVAL) {
            result += viewModel.getTimeArrivalField();
        } else {
            result += viewModel.getTimeDepartureField();
            result += viewModel.getRouteDeparturePlacePosition();
        }
        result += viewModel.getStopIconResourceField();
        result += viewModel.getNotesField();
        return result;
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

            int icon = savedInstanceState.getInt(Constants.IN_ROUTEICON);
            if (icon != 0) saveStopIconField(icon);

            String notes = savedInstanceState.getString(Constants.IN_NOTES);
            if (notes != null) saveNotesField(notes);
        }
        // else leggo tutto dal view model
    }

    @Override
    protected EditFieldListAdapter createFieldAdapter() {
        return new EditFieldListAdapter(getApplicationContext(), FieldLists.getEditFieldList(Stop.class));
    }

    @Override
    protected void collectAndSaveObject() {
        // demo
        //todo dovrei raccogliere i dati creando l'opportuno oggetto da salvare nell'DB
        onVacationOperationComplete(0);
    }

    @Override
    protected void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants) {
        // do nothing
    }

    @Override
    public void onVacationOperationComplete(long rowId) {
        //todo questo metodo dovrebbe essere chiamato dal DB a salvataggio concluso

        Stop builtStop = null;

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY + ROUTE_STOP, builtStop);
        setResult(RESULT_OK, replyIntent);
        Toast.makeText(this, R.string.demo_new_stop, Toast.LENGTH_SHORT).show();
        finish(); // restituisce il risultato a chi ha chiamato l'activity
    }
}
