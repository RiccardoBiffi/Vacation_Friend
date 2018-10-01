package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RouteDay
        extends RouteElement
        implements StickyHeader {
    public String currentDay;

    public RouteDay(Date currentStopDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEE dd MMMMMMM yyyy", Locale.getDefault());
        currentDay = sdf.format(currentStopDay);
    }
}
