package com.rbiffi.vacationfriend.AppSections.VacationList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rbiffi.vacationfriend.AppSections.VacationList.Adapters.EditFieldListAdapter;
import com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels.EditAppObjectViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Period;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.Utils.FieldLists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityModifyVacation
        extends ActivityNewVacation
        implements VacationFriendRepository.IRepositoryListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void getActivityViewModel() {
        viewModel = ViewModelProviders.of(this).get(EditAppObjectViewModel.class);
    }

    @Override
    protected void saveDataFromIntentMaybe() {
        Intent intent = getIntent();
        Vacation current = intent.getParcelableExtra(Constants.PARCEL_SELECTED_VACATION);
        if (viewModel.getVacationId() == EditAppObjectViewModel.FIRST_EXECUTION && current != null) {
            viewModel.setVacationId(current.id);
            viewModel.setTitle(current.title);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            viewModel.setPeriodFrom(format.format(current.period.startDate));
            viewModel.setPeriodTo(format.format(current.period.endDate));
            viewModel.setPlace(current.place);
            viewModel.loadFieldParticipants(viewModel.getVacationId()).observe(this, new Observer<List<Participant>>() {
                @Override
                public void onChanged(@Nullable List<Participant> participants) {
                    viewModel.setParticipants(participants);
                    editFieldListAdapter.updateParticipants(viewModel.getParticipants());
                }
            });
            viewModel.setPhoto(current.photo);
        }
    }


    @Override
    protected EditFieldListAdapter createFieldAdapter() {
        return new EditFieldListAdapter(getApplicationContext(), FieldLists.getEditFieldList(Vacation.class));
    }

    @Override
    protected void setupActivityButtons() {
        super.setupActivityButtons();
        confirm.setText(R.string.button_save);
    }


    @Override
    protected void collectAndSaveObject() {
        Period period = new Period(viewModel.getPeriodFrom(), viewModel.getPeriodTo());
        Vacation builtVacation = new Vacation(viewModel.getTitle(), period, viewModel.getPlace(), viewModel.getPhoto(), false);
        builtVacation.id = viewModel.getVacationId();
        viewModel.update(builtVacation, this);
        finish();
    }


    @Override
    public void onVacationOperationComplete(long rowId) {
        // vacanza aggiornata correttamente nel DB
        List<JoinVacationParticipant> jvps = new ArrayList<>();
        List<Participant> partecipants = viewModel.getParticipants();
        for (Participant part :
                partecipants) {
            jvps.add(new JoinVacationParticipant(rowId, part.email));
        }
        viewModel.insertList(jvps);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY + VACATION, rowId);
        setResult(RESULT_OK, replyIntent);
        finish(); // restituisce l'id della vacanza modificata a chi ha chiamato l'activity
    }

}
