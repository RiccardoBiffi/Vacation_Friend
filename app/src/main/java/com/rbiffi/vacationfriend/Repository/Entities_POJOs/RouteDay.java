package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RouteDay
        extends RouteElement
        implements StickyHeader {
    public final String currentDay;

    public RouteDay(Date currentStopDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
        currentDay = sdf.format(currentStopDay);
    }

    public Date getCurrentDayAsDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
        Date result = new Date();
        try {
            result = sdf.parse(currentDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
