package com.rbiffi.vacationfriend.AppSections.Home.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class VacationViewModel extends UserViewModel {

    private VacationFriendRepository repository;

    // oggetti aggiornati dal DB appena modificati
    private LiveData<Vacation> currentVacation;
    private LiveData<List<Participant>> currentParticipants;

    // per persistere lo stato
    private long vacationId;
    private String fieldTitle;
    private String fieldPeriodFrom;
    private String fieldPeriodTo;
    private String fieldPlace;
    private List<Participant> fieldParticipants;
    private Uri fieldPhoto;

    public VacationViewModel(@NonNull Application app) {
        super(app);
        repository = VacationFriendRepository.getInstance(app);

        //inizializzo i dati da persistere
        fieldTitle = "";
        fieldPeriodFrom = "";
        fieldPeriodTo = "";
        fieldPlace = "";
        fieldParticipants = new ArrayList<>();
        fieldPhoto = Uri.parse("");
    }

    public LiveData<Vacation> getCurrentVacation() {
        return currentVacation;
    }

    public long getVacationId() {
        return vacationId;
    }

    public void setVacationId(long vacationId) {
        this.vacationId = vacationId;

        // ora che conosco l'ID della vacanza, posso caricare tutte le info relative
        if (currentVacation == null)
            currentVacation = repository.getVacationDetails(vacationId);
        if (currentParticipants == null)
            currentParticipants = repository.getVacationParticipants(vacationId);

        //todo carica altre informazioni collegate alla vacanza
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldPeriodFrom() {
        return fieldPeriodFrom;
    }

    public void setFieldPeriodFrom(String fieldPeriodFrom) {
        this.fieldPeriodFrom = fieldPeriodFrom;
    }

    public String getFieldPeriodTo() {
        return fieldPeriodTo;
    }

    public void setFieldPeriodTo(String fieldPeriodTo) {
        this.fieldPeriodTo = fieldPeriodTo;
    }

    public String getFieldPlace() {
        return fieldPlace;
    }

    public void setFieldPlace(String fieldPlace) {
        this.fieldPlace = fieldPlace;
    }

    public LiveData<List<Participant>> getCurrentParticipants() {
        return currentParticipants;
    }

    public List<Participant> getParticipants() {
        return fieldParticipants;
    }

    public void setParticipants(List<Participant> participants) {
        this.fieldParticipants = participants;
    }

    public Uri getFieldPhoto() {
        return fieldPhoto;
    }

    public void setFieldPhoto(Uri fieldPhoto) {
        this.fieldPhoto = fieldPhoto;
    }
}
