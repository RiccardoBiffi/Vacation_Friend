package com.rbiffi.vacationfriend.Repository.Entities_POJOs;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {
        @Index(
                value = {
                        "vacationId",
                        "userEmail"
                }
        ),
        @Index("userEmail")
},
        primaryKeys = {
                "vacationId",
                "userEmail"
        },
        foreignKeys = {
                @ForeignKey(entity = Vacation.class,
                        parentColumns = "id",
                        childColumns = "vacationId",
                        onDelete = CASCADE),
                @ForeignKey(entity = Participant.class,
                        parentColumns = "email",
                        childColumns = "userEmail")
        })
public class JoinVacationParticipant {
    @NonNull
    public final long vacationId;
    @NonNull
    public final String userEmail;

    public JoinVacationParticipant(long vacationId, String userEmail) {
        this.vacationId = vacationId;
        this.userEmail = userEmail;
    }
}