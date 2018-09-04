package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.FieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.ModifyVacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Period;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Utils.FieldLists;

public class ActivityModifyVacation extends ActivityNewVacation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getActivityViewModel() {
        viewModel = ViewModelProviders.of(this).get(ModifyVacationViewModel.class);
    }

    @Override
    protected void restoreState(Bundle savedInstanceState) {
        super.restoreState(savedInstanceState);

        Intent intent = getIntent();
        Vacation current = intent.getParcelableExtra("selectedVacation");
        ModifyVacationViewModel modViewmodel = (ModifyVacationViewModel) viewModel;
        if (modViewmodel.getVacationId() == ModifyVacationViewModel.FIRST_EXECUTION && current != null) {
            modViewmodel.setVacationId(current.id);
            modViewmodel.setFieldTitle(current.title);
            modViewmodel.setFieldPeriodFrom(current.period.startDate);
            modViewmodel.setFieldPeriodTo(current.period.endDate);
            modViewmodel.setFieldPlace(current.place);
            // todo carica i partecipanti
            modViewmodel.setFieldPhoto(current.photo);
        }
    }

    @Override
    protected FieldListAdapter createFieldAdapter() {
        return new FieldListAdapter(getApplicationContext(), FieldLists.getFieldList(Vacation.class), viewModel);
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        //todo meglio se creo una classe astratta da cui eredito le parti comuni
        getSupportActionBar().setTitle(R.string.acivity_title_mod_vacation);
    }

    @Override
    protected void setupActivityButtons() {
        super.setupActivityButtons();
        confirm.setText(R.string.button_save);
    }

    @Override
    protected void buildAndSaveVacation() {
        Period period = new Period(viewModel.getFieldPeriodFrom(), viewModel.getFieldPeriodTo());
        Vacation builtVacation = new Vacation(viewModel.getFieldTitle(), period, viewModel.getFieldPlace(), viewModel.getFieldPhoto(), false);

        ModifyVacationViewModel modViewmodel = (ModifyVacationViewModel) viewModel;
        builtVacation.id = modViewmodel.getVacationId();
        modViewmodel.update(builtVacation);
        finish();
    }
}
