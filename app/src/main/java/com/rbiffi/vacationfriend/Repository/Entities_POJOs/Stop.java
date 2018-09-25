package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.Date;

class Stop {

    public long vacationId;

    public String title;

    public String place;

    @NonNull
    public Date day;

    public Time arrivalTime;

    public Time departureTime;

    public Uri icon;

    public String notes;

    public Stop(String title, String place, @NonNull Date day, Time arrivalTime, Time departureTime, Uri icon, String notes) {
        this.title = title;
        this.place = place;
        this.day = day;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.icon = icon;
        this.notes = notes;
    }

}
