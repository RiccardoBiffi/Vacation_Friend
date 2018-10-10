package com.rbiffi.vacationfriend.AppSections.Itinerario;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;

import com.rbiffi.vacationfriend.AppSections.Itinerario.ViewModels.NewStopViewModel;
import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Utils.ActivityEditAppObject;

import java.util.List;

public class ActivityNewStop extends ActivityEditAppObject {

    protected NewStopViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int checkFormValidity() {
        return 0;
    }

    @Override
    protected void getActivityViewModel() {
        viewModel = ViewModelProviders.of(this).get(NewStopViewModel.class);
    }

    @Override
    protected void saveDataFromIntentMaybe() {

    }

    @Override
    public String getTitleField() {
        return viewModel.getFieldTitle();
    }

    @Override
    public String getPeriodFromField() {
        return null;
    }

    @Override
    public String getPeriodToField() {
        return null;
    }

    @Override
    public String getPlaceField() {
        return viewModel.getFieldPlace();
    }

    @Override
    public Uri getPhotoField() {
        return null;
    }

    @Override
    public void saveTitleField(String title) {

    }

    @Override
    public void saveDateFromField(String dateFrom) {

    }

    @Override
    public void saveDateToField(String dateTo) {

    }

    @Override
    public void savePlaceField(String place) {

    }

    @Override
    public void savePhotoField(Uri photo) {

    }

    @Override
    protected EditFieldListAdapter createFieldAdapter() {
        return null;
    }

    @Override
    protected void collectAndSaveObject() {

    }

    @Override
    protected void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants) {

    }

    @Override
    public void onVacationOperationComplete(long rowId) {

    }
}
