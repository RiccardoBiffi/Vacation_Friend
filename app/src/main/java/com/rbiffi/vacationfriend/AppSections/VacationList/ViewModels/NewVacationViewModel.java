package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationRepository;

import java.util.ArrayList;
import java.util.List;

public class NewVacationViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    private VacationRepository repository;

    private String fieldTitle;
    private String fieldPeriodFrom;
    private String fieldPeriodTo;
    private String fieldPlace;
    private List<Participant> fieldParticipants;
    private Uri fieldPhoto;

    private int mod;
    private long vacationId;

    public static final int NEW_VACATION = 0;
    public static final int MOD_VACATION = 1;

    public NewVacationViewModel(@NonNull Application app) {
        super(app);
        repository = new VacationRepository(app);

        //inizializzo i dati

        fieldTitle = "";
        fieldPeriodFrom = "";
        fieldPeriodTo = "";
        fieldPlace = "";
        fieldParticipants = new ArrayList<>();
        fieldPhoto = Uri.parse("");

        mod = NEW_VACATION;
    }

    public void getVacationDetails(long vId) {
        repository.getVacationDetails(vId);
    }

    public void insert(Vacation v, VacationRepository.IRepositoryListener listener) {
        //la logica dell'inserimento delle vacanze è completamente gestita dal repository
        repository.addListener(listener);
        repository.insert(v);
    }

    public void update(Vacation builtVacation) {
        repository.update(builtVacation);
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldPeriodFrom(String fieldPeriodFrom) {
        this.fieldPeriodFrom = fieldPeriodFrom;
    }

    public String getFieldPeriodFrom() {
        return fieldPeriodFrom;
    }

    public void setFieldPeriodTo(String fieldPeriodTo) {
        this.fieldPeriodTo = fieldPeriodTo;
    }

    public String getFieldPeriodTo() {
        return fieldPeriodTo;
    }

    public void setFieldPlace(String fieldPlace) {
        this.fieldPlace = fieldPlace;
    }

    public String getFieldPlace() {
        return fieldPlace;
    }

    public void setFieldPhoto(Uri fieldPhoto) {
        this.fieldPhoto = fieldPhoto;
    }

    public Uri getFieldPhoto() {
        return fieldPhoto;
    }

    public void setFieldParticipants(List<Participant> fieldParticipants) {
        this.fieldParticipants = fieldParticipants;
    }

    public List<Participant> getFieldParticipants() {
        return fieldParticipants;
    }

    public void insertList(List<JoinVacationParticipant> jvps) {
        repository.insertList(jvps);
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public int getMod() {
        return mod;
    }

    public long getVacationId() {
        return vacationId;
    }

    public void setVacationId(long vacationId) {
        this.vacationId = vacationId;
    }
}
