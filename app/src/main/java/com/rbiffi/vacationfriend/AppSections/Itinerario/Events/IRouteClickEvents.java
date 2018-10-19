package com.rbiffi.vacationfriend.AppSections.Itinerario.Events;

import android.view.View;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Stop;

public interface IRouteClickEvents {

    void onOverflowClick(View v, Stop stop);
}
