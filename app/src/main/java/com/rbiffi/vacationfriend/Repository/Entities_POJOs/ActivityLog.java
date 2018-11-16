package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.net.Uri;

import java.util.Date;

public class ActivityLog {

    private final long id;
    public final long vacationId; // la vacanza di riferimento
    public final Uri participantPhoto; // todo sostituisci con email, da cui recupero la foto
    public final String logText;
    public final Date logDateTime;

    public ActivityLog(long id, long vacationId, Uri photo, String logText, Date logDateTime) {
        this.id = id;
        this.vacationId = vacationId;
        this.participantPhoto = photo;
        this.logText = logText;
        this.logDateTime = logDateTime;
    }

}
