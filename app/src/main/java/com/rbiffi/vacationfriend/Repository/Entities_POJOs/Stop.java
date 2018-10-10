package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Date;

public class Stop extends RouteElement {

    public long vacationId;

    public String title;

    public String place;

    @NonNull
    public Date day;

    public StopTimeArrDep stopTime;

    public Uri stopIcon;

    public String notes;

    public Stop(String title, String place, @NonNull Date day, StopTimeArrDep stopTime, Uri stopIcon, String notes) {
        this.title = title;
        this.place = place;
        this.day = day;
        this.stopTime = stopTime;
        this.stopIcon = stopIcon;
        this.notes = notes;
    }

}
