package com.rbiffi.vacationfriend.AppSections.Home.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ActivityLog;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Discussion;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Step;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vehicle;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.Converters;
import com.rbiffi.vacationfriend.Utils.UserViewModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VacationViewModel extends UserViewModel {

    private VacationFriendRepository repository;

    // oggetti aggiornati dal DB quando modificati
    private LiveData<Vacation> currentVacation;
    private LiveData<List<Participant>> currentParticipants;
    private LiveData<List<Discussion>> discussions;
    private LiveData<List<ActivityLog>> activityLog;
    private LiveData<List<Step>> currentRoute;

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

        // test todo recuperare da repository le discussioni con LiveData
        if (discussions == null)
            discussions = testCreateDiscussions();

        // test todo recuperare da repository le attività con LiveData
        if (activityLog == null)
            activityLog = testCreateActivityLogs();

        // test todo recuperare da repository l'itinerario con LiveData
        if (currentRoute == null)
            currentRoute = testCreateRoute(); // repository.getVacationRoute(vacationId)

        //todo carica altre informazioni collegate alla vacanza
    }

    private LiveData<List<Discussion>> testCreateDiscussions() {
        MutableLiveData<List<Discussion>> mld = new MutableLiveData<>();

        mld.setValue(null);
        return mld;
    }

    private LiveData<List<ActivityLog>> testCreateActivityLogs() {
        MutableLiveData<List<ActivityLog>> mall = new MutableLiveData<>();
        List<ActivityLog> all = new ArrayList<>();

        ActivityLog al = new ActivityLog(0, vacationId, Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.average_man),
                getApplication().getResources().getString(R.string.ph_activitylog_edit_period), new Date());
        all.add(al);
        al = new ActivityLog(1, vacationId, Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.average_man),
                getApplication().getResources().getString(R.string.ph_activitylog_edit_participants), new Date());
        all.add(al);
        al = new ActivityLog(2, vacationId, Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.average_man),
                getApplication().getResources().getString(R.string.ph_activitylog_edit_vacation), new Date());
        all.add(al);

        mall.setValue(all);
        return mall;
    }

    private LiveData<List<Step>> testCreateRoute() {
        MutableLiveData<List<Step>> msl = new MutableLiveData<>();
        List<Step> sl = new ArrayList<>();

        Uri uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_route_home_white_24dp);
        Stop stop = new Stop("Appartamento", "via Lanterna 45, Villaggio vacanze, Croazia",
                Converters.timestampToDate("2018-10-01"), null, Time.valueOf("12:30:00"),
                uri, "Note: tanta fame!");
        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_directions_car_white_24dp);
        Vehicle vehicle = new Vehicle("Automobile", uri);
        Step step = new Step();
        step.setStopPlace(stop);
        step.setVehicleToNextStop(vehicle);
        sl.add(step);

        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_route_restaurant_white_24dp);
        stop = new Stop("Ristorante", "via Turismo 12, Rovigno, Croazia",
                Converters.timestampToDate("2018-10-01"), Time.valueOf("12:45:00"), Time.valueOf("14:30:00"),
                uri, "Gnam!");
        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_directions_walk_white_24dp);
        vehicle = new Vehicle("Camminata", uri);
        step = new Step();
        step.setStopPlace(stop);
        step.setVehicleToNextStop(vehicle);
        sl.add(step);

        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_route_beach_access_white_24dp);
        stop = new Stop("Spiaggia", "via Acquatica 97, Pola, Croazia",
                Converters.timestampToDate("2018-10-01"), Time.valueOf("15:10:00"), Time.valueOf("19:00:00"),
                uri, "Nuotata con scogli");
        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_train_white_24dp);
        vehicle = new Vehicle("Treno", uri);
        step = new Step();
        step.setStopPlace(stop);
        step.setVehicleToNextStop(vehicle);
        sl.add(step);

        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_route_hotel_white_24dp);
        stop = new Stop("Appartamento", "via Lanterna 45, Villaggio vacanze, Croazia",
                Converters.timestampToDate("2018-10-01"), Time.valueOf("22:00:00"), null,
                uri, "Tanta nanna");
        vehicle = null;
        step = new Step();
        step.setStopPlace(stop);
        step.setVehicleToNextStop(vehicle);
        sl.add(step);

        uri = Converters.stringToUri("android.resource://com.rbiffi.vacationfriend/" + R.drawable.ic_route_hotel_white_24dp);
        stop = new Stop("Appartamento", "via Lanterna 45, Villaggio vacanze, Croazia",
                Converters.timestampToDate("2018-10-03"), null, Time.valueOf("09:00:00"),
                uri, "New day of fun!");
        vehicle = null;
        step = new Step();
        step.setStopPlace(stop);
        step.setVehicleToNextStop(vehicle);
        sl.add(step);

        msl.setValue(sl);
        return msl;
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

    public LiveData<List<Discussion>> getDiscussions() {
        return discussions;
    }

    public LiveData<List<ActivityLog>> getActivityLog() {
        return activityLog;
    }

    public LiveData<List<Step>> getRoute() {
        return currentRoute;
    }
}
