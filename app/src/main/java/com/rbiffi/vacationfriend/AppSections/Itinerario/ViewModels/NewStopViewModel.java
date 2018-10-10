package com.rbiffi.vacationfriend.AppSections.Itinerario.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;

public class NewStopViewModel extends AndroidViewModel {
    // todo estende qualcosa di più comodo?
    public interface IProperties {

        String getTitleField();

        String getPlaceField();

        String getDateField();

        String getTimeArrivalField();

        String getTimeDepartureField();

        Uri getPhotoField();

        String getNotesField();

        void saveTitleField(String title);

        void savePlaceField(String place);

        void saveDateField(String date);

        void saveTimeArrivalField(String arrivalTime);

        void saveTimeDepartureField(String departureTime);

        void saveIconField(Uri icon);

        void saveNotesField(String notes);
    }

    public NewStopViewModel(@NonNull Application application) {
        super(application);
    }

    public String getFieldTitle() {
        return null;
    }

    public String getFieldPlace() {
        return null;
    }
}
