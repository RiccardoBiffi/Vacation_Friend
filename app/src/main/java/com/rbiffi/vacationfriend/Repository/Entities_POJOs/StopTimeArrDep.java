package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import java.sql.Time;

public class StopTimeArrDep {

    public Time arrivalTime;
    public Time departureTime;

    public StopTimeArrDep(Time arrival, Time departure) {
        this.arrivalTime = arrival;
        this.departureTime = departure;
    }

    public StopTimeArrDep(String arrivalHHMMSS, String departureHHMMSS) {
        this.arrivalTime = arrivalHHMMSS != null ? Time.valueOf(arrivalHHMMSS) : null;
        this.departureTime = departureHHMMSS != null ? Time.valueOf(departureHHMMSS) : null;
    }
}
