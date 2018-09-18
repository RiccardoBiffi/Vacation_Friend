package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.FieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ModifyVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Period;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.ActivityEditAppObject;
import com.rbiffi.vacationfriend.Utils.FieldLists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityModifyVacation
        extends ActivityEditAppObject
        implements VacationFriendRepository.IRepositoryListener {

    protected ModifyVacationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void getActivityViewModel() {
        viewModel = ViewModelProviders.of(this).get(ModifyVacationViewModel.class);
    }

    @Override
    protected void saveDataFromIntentMaybe() {
        Intent intent = getIntent();
        Vacation current = intent.getParcelableExtra("selectedVacation");
        if (viewModel.getVacationId() == ModifyVacationViewModel.FIRST_EXECUTION && current != null) {
            viewModel.setVacationId(current.id);
            viewModel.setFieldTitle(current.title);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            viewModel.setFieldPeriodFrom(format.format(current.period.startDate));
            viewModel.setFieldPeriodTo(format.format(current.period.endDate));
            viewModel.setFieldPlace(current.place);
            viewModel.setFieldPartecipants(viewModel.getVacationId(), this);
            viewModel.setFieldPhoto(current.photo);
        }
    }

    @Override
    public String getTitleField() {
        return viewModel.getFieldTitle();
    }

    @Override
    public String getPeriodFromField() {
        return viewModel.getFieldPeriodFrom();
    }

    @Override
    public String getPeriodToField() {
        return viewModel.getFieldPeriodTo();
    }

    @Override
    public String getPlaceField() {
        return viewModel.getFieldPlace();
    }

    @Override
    public Uri getPhotoField() {
        return viewModel.getFieldPhoto();
    }

    @Override
    public void saveTitleField(String title) {
        viewModel.setFieldTitle(title);
    }

    @Override
    public void saveDateFromField(String dateFrom) {
        viewModel.setFieldPeriodFrom(dateFrom);
    }

    @Override
    public void saveDateToField(String dateTo) {
        viewModel.setFieldPeriodTo(dateTo);
    }

    @Override
    public void savePlaceField(String place) {
        viewModel.setFieldPlace(place);
    }

    @Override
    public void savePhotoField(Uri photo) {
        viewModel.setFieldPhoto(photo);
    }

    @Override
    protected FieldListAdapter createFieldAdapter() {
        return new FieldListAdapter(getApplicationContext(), FieldLists.getFieldList(Vacation.class), viewModel);
    }

    @Override
    protected void setupActivityButtons() {
        super.setupActivityButtons();
        confirm.setText(R.string.button_save);
    }

    @Override
    protected int checkFormValidity() {
        if (viewModel.getFieldTitle().isEmpty()) {
            return 0;
        }

        if (viewModel.getFieldPeriodFrom().isEmpty() ||
                viewModel.getFieldPeriodTo().isEmpty()) {
            return 1;
        }

        if (!checkDateConsistency(viewModel.getFieldPeriodFrom(), viewModel.getFieldPeriodTo()))
            return 1;

        return -1;
    }

    @Override
    protected void collectAndSaveObject() {
        Period period = new Period(viewModel.getFieldPeriodFrom(), viewModel.getFieldPeriodTo());
        Vacation builtVacation = new Vacation(viewModel.getFieldTitle(), period, viewModel.getFieldPlace(), viewModel.getFieldPhoto(), false);
        builtVacation.id = viewModel.getVacationId();
        viewModel.update(builtVacation);
        finish();
    }

    @Override
    protected void saveParticipantsAndUpdateAdapter(List<Participant> selectedParticipants) {
        viewModel.setFieldParticipants(selectedParticipants);
        fieldListAdapter.updateParticipants(viewModel.getFieldParticipants());
    }

    @Override
    public void onVacationOperationComplete(long rowId) {
        // vacanza aggiornata correttamente nel DB
        List<JoinVacationParticipant> jvps = new ArrayList<>();
        List<Participant> partecipants = viewModel.getFieldParticipants();
        for (Participant part :
                partecipants) {
            jvps.add(new JoinVacationParticipant(rowId, part.email));
        }
        viewModel.insertList(jvps);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY + VACATION_ID, rowId);
        setResult(RESULT_OK, replyIntent);
        finish(); // restituisce il risultato a chi ha chiamato l'activity
    }

    @Override
    public void onGetVacationDetailsComplete(Vacation v) {
        //non necessario perchè la vacanza scelta arriva dall'intent. In caso fai 2+ interfacce del repository
    }

    @Override
    public void onGetVacationParticipants(List<Participant> participants) {
        viewModel.setFieldParticipants(participants);
        fieldListAdapter.updateParticipants(viewModel.getFieldParticipants());
    }

}
