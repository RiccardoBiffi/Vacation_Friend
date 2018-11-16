package com.rbiffi.vacationfriend.Utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;

// viewmodel contenente le informazioni dell'utente, presenti in tutta la app dopo il login

public abstract class UserViewModel extends AndroidViewModel {

    private final Participant myself;

    protected UserViewModel(@NonNull Application application) {
        super(application);

        this.myself = new Participant(application.getString(R.string.ph_user_email),
                Uri.parse("android.resource://com.rbiffi.vacationfriend/" + R.drawable.average_man),
                application.getString(R.string.ph_name),
                application.getString(R.string.ph_lastname));
    }

    public Participant getMyself() {
        return myself;
    }
}
