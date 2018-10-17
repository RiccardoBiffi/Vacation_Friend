package com.rbiffi.vacationfriend.AppSections.VacationList.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.JoinVacationParticipant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;

import java.util.ArrayList;
import java.util.List;

public class EditAppObjectViewModel extends AndroidViewModel {

    //warning: non salvare Activity, Fragment, View o contesti in quanto cambiano più spesso dei dati
    //nb: il ViewModel non sostituisce l'istanza salvata da onSaveInstanceState perché non sopravvive al kill del processo

    private VacationFriendRepository repository;

    public static final long FIRST_EXECUTION = -1;
    private long modifiedVacationId;

    private LiveData<List<Participant>> currentParticipants;

    private String title;
    private String periodFrom;
    private String periodTo;
    private String place;
    private List<Participant> participants;
    private Uri photo;

    private String routeDay;
    private String routeArrivalTime;
    private String routeDepartureTime;
    private Uri routeIcon;
    private String stopNotes;


    EditAppObjectViewModel(@NonNull Application app) {
        super(app);
        repository = VacationFriendRepository.getInstance(app);

        //inizializzo i dati
        modifiedVacationId = FIRST_EXECUTION;
        title = "";
        periodFrom = "";
        periodTo = "";
        place = "";
        participants = new ArrayList<>();
        photo = Uri.parse("");
        routeDay = "";
        routeArrivalTime = "";
        routeDepartureTime = "";
        routeIcon = Uri.parse("");
        stopNotes = "";
    }

    public void insert(Vacation v, VacationFriendRepository.IRepositoryListener listener) {
        //la logica dell'inserimento delle vacanze è completamente gestita dal repository
        repository.addListener(listener);
        repository.insert(v);
    }

    public void update(Vacation builtVacation, VacationFriendRepository.IRepositoryListener listener) {
        repository.addListener(listener);
        repository.update(builtVacation);
    }

    public long getVacationId() {
        return modifiedVacationId;
    }

    public void setVacationId(long vacationId) {
        this.modifiedVacationId = vacationId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public Uri getPhoto() {
        return photo;
    }

    public LiveData<List<Participant>> loadFieldParticipants(long vacationId) {
        if (currentParticipants == null)
            currentParticipants = repository.getVacationParticipants(vacationId);
        return currentParticipants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void insertList(List<JoinVacationParticipant> jvps) {
        repository.insertParticipants(jvps);
    }

    public String getDateField() {
        return routeDay;
    }

    public void setDateField(String date) {
        this.routeDay = date;
    }

    public String getTimeArrivalField() {
        return routeArrivalTime;
    }

    public void setTimeArrivalField(String arrivalTime) {
        this.routeArrivalTime = arrivalTime;
    }

    public String getTimeDepartureField() {
        return routeDepartureTime;
    }

    public void setTimeDepartureField(String departureTime) {
        this.routeDepartureTime = departureTime;
    }

    public Uri getIconField() {
        return routeIcon;
    }

    public void setIconField(Uri icon) {
        this.routeIcon = icon;
    }

    public String getNotesField() {
        return stopNotes;
    }

    public void setNotesField(String notes) {
        this.stopNotes = notes;
    }
}